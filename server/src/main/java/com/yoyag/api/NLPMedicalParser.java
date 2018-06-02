/**
 * 
 */
package com.yoyag.api;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.TOP;
import org.xml.sax.SAXException;

import com.yoyag.api.ctakes.Pipeline;

/**
 * @author ben
 *
 */
public class NLPMedicalParser implements Parser {
	
	private static final Logger LOGGER = Logger.getLogger(NLPMedicalParser.class);
	static AnalysisEngine pipeline;
	
	public NLPMedicalParser() throws ServletException {
		LOGGER.debug("Initilizing NLPMedicalParser");
		AggregateBuilder aggregateBuilder;
		try {
			aggregateBuilder = Pipeline.getAggregateBuilder();
			pipeline = aggregateBuilder.createAggregate();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	/* (non-Javadoc)
	 * @see com.yoyag.api.Parser#parseInput(com.yoyag.api.Input)
	 */
	@Override
	public void parseInput(Input in) {
		NewInput input;
		try {
			input = (NewInput)in;
		} catch (Exception e) {
			LOGGER.fatal("Wrong input to NLPMedicalParser");
			return;
		}
		try {
			/*
			 * Set the document text to process And run the cTAKES pipeline
			 */
			FreetextOutput out = new FreetextOutput();
			String text = (String)input.getData().get("text");
			JCas jcas = pipeline.newJCas();
			jcas.setDocumentText(text);
			pipeline.process(jcas);
			String customer_id = input.getToken();
			List<String> symptoms = formatResults(jcas, input.getUserID(), (String)input.getData().get("location"), customer_id, out);
			jcas.reset();
			String result = parseSymptoms(symptoms);
			out.setContent(result);
			out.setTimestamp(input.getTimestamp());
			out.setUserID(input.getUserID());
			String query;
			
			//preparing query
			//query = "INSERT INTO patientData (ts, userID, token) VALUES ('" + input.getTimestamp() + "','" + input.getUserID() + "','" + input.getToken() + "');";
			//query =  "UPDATE patientData SET ts = '" + input.getTimestamp() + "', token = '" + input.getToken() + "' where userID = '" + input.getUserID() + "';";
			query =  "UPDATE patientData SET ts = '" + input.getTimestamp() + "' where userID = '" + input.getUserID() + "';";

			//running query
			runQuery(query);
			
			new MedicalSummaryHandler().handle(out);
//			LOGGER.info(result);
			
		} catch (Exception e) {
			LOGGER.fatal(e);
			return;
		}
	}
	
	public List<String> formatResults(JCas jcas, String userID, String location, String customer_id, FreetextOutput out) throws SAXException, IOException, SQLException {
		LOGGER.info("Starting to process results");
		List<String> symptoms = new ArrayList<String>();
		for (TOP annotation : JCasUtil.selectAll(jcas)) {
			extractFeatures(symptoms, (FeatureStructure)annotation);
		}
		symptoms = new ArrayList<>(new HashSet<String>(symptoms));
		String query;
		
		StringBuilder sb = new StringBuilder();
		for (String s : symptoms) {
			sb.append(s);
			sb.append(",");
		}
		String stringSymptoms = sb.toString();
		System.out.println(stringSymptoms);
		//preparing query
		//query =  "UPDATE patientData  SET symptoms = '" + stringSymptoms + "' where userID = '" + userID + "';";
		query = "INSERT INTO patientData (userID, customer_id, symptoms, location) VALUES ('" + userID + "','" + customer_id + "','" + stringSymptoms + "', '" + location + "');";
		
		System.out.println(query);
		
		//running query
		runQuery(query);
		
		query = "select last_insert_id();";
		String currentToken = runSelectQuery(query); 
		out.setSessionID(currentToken);
		return symptoms;
	}
	
//	public String formatResults(JCas jcas) throws SAXException, IOException { // JCAS to XML string
//		StringBuffer sb = new StringBuffer();
////		Collection<TOP> annotations = JCasUtil.selectAll(jcas);
//		ByteArrayOutputStream output = new ByteArrayOutputStream();
//		XmiCasSerializer.serialize(jcas.getCas(), output);
//		sb.append(output.toString());
//		output.close();
//		return sb.toString();
//	}
	
	private void extractFeatures(List<String> symptoms, FeatureStructure fs) {
		List<?> plist = fs.getType().getFeatures();
		for (Object obj : plist) {
			if (obj instanceof Feature) {
				Feature feature = (Feature) obj;
				String val = "";
				if (feature.getRange().isPrimitive()) {
					val = fs.getFeatureValueAsString(feature);
				} else if (feature.getRange().isArray()) {
					// Flatten the Arrays
					FeatureStructure featval = fs.getFeatureValue(feature);
					if (featval instanceof FSArray) {
						FSArray valarray = (FSArray) featval;
						for (int i = 0; i < valarray.size(); ++i) {
							FeatureStructure temp = valarray.get(i);
							extractFeatures(symptoms, temp);
						}
					}
				}
				if (feature.getName() != null
						&& val != null
						&& val.trim().length() > 0
						&& !"confidence".equalsIgnoreCase(feature
								.getShortName())) {
					if ("preferredText".equals(feature.getShortName())) {
						symptoms.add(val);
					}
				}
			}
		}
	}
	
	private String parseSymptoms(List<String> symptoms) {
		StringBuilder sb = new StringBuilder();
		sb.append("The patient reported the following symptoms:\n");
		int i;
		for (i = 0 ; i < symptoms.size() ; i++) {
			sb.append(symptoms.get(i));
			if (i < symptoms.size()-1)
				sb.append(", ");
			else
				sb.append(".");
		}
		return sb.toString();
	}
	
	public static void createDBConnection() {
		// creating db connection
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        }
	}
	
	public static int runQuery(String query) {
		// creating db connection
		createDBConnection();
		
		Connection conn = null;
		try {
		    conn =
			    	   DriverManager.getConnection("jdbc:mysql://localhost:3306/yoyagDB?" +
			                    "user=javauser&password=javaDBuser1!&useSSL=false");
//			       DriverManager.getConnection("jdbc:mysql://193.106.55.122:2222/yoyagDB?" +
//			                                   "user=javauser&password=javaDBuser1!&useSSL=false");
		    //creating statements and running the query
		    Statement stmt = conn.createStatement();
		    stmt.executeUpdate(query);
			stmt.close();
			conn.close();
			
			return 1;
		   
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		    
		    return 2;
		}
	}
	
	public static String runSelectQuery(String query) throws SQLException {
		// creating db connection
		createDBConnection();
		
		Connection conn = null;
		try {
		    conn =
			    	   DriverManager.getConnection("jdbc:mysql://localhost:3306/yoyagDB?" +
			                    "user=javauser&password=javaDBuser1!&useSSL=false");
//			       DriverManager.getConnection("jdbc:mysql://193.106.55.122:2222/yoyagDB?" +
//			                                   "user=javauser&password=javaDBuser1!&useSSL=false");
		    //creating statements and running the query
		    
		    java.sql.PreparedStatement preparedStatement = null;
	        //String query = "select season from seasonTable where league_name=?";

	        preparedStatement = conn.prepareStatement(query);

	        //preparedStatement.setString(1, league);
	        ResultSet rs = preparedStatement.executeQuery();
	       
	        String result = null;
	        if(rs.next())
	        	result = rs.getString(1);
		    
//		    Statement stmt = conn.createStatement();
//		    stmt.executeUpdate(query);
			//stmt.close();
			conn.close();
			
			return result;
		   
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		    
		    return "Error";
		}
		
		
	}
}


