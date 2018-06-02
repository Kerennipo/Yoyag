/**
 * 
 */
package com.yoyag.api;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Ben Surkiss
 *
 */
@RestController
public class RestAPIController {
	private static final Logger LOGGER = Logger.getLogger(RestAPIController.class);
	private IPHandler ipHandler;
	private static final Map<String, Parser> parsersFactory = new HashMap<>(); 
    
    @PostConstruct
    public void init() throws ServletException, IOException {
        LOGGER.info("Initializing API controller");
        ipHandler = new IPHandler();
        parsersFactory.put("NLP", new NLPMedicalParser());
        parsersFactory.put("Simple", new SimpleMedicalParser());
    }
    
	@RequestMapping(value = "/post", method = RequestMethod.POST)
    public ResponseEntity<String> post(@RequestBody NewInput input, HttpServletRequest request) throws ServletException {
		String location = ipHandler.getLocation(request);
		input.getData().put("location", location);
		Parser p = getParserForInput(input);
		p.parseInput(input);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> update(@RequestBody UpdateInput input) {
		Parser updateParser = new UpdateParser();
		updateParser.parseInput(input);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = "/getStatistics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Integer>> getStatistics(StatisticsInput input) {
		Map<String, Integer> list = getStatiscsFromDB(input);
		return new ResponseEntity<Map<String, Integer>>(list, HttpStatus.OK);
	}
	
	private Map<String, Integer> getStatiscsFromDB(StatisticsInput input) {
		String query = "";
		
		return null; //TODO
	}
	
	private Parser getParserForInput(NewInput input) throws ServletException {
//		return new SimpleMedicalParser();
		return parsersFactory.get("NLP");
	}
	
	public static void createDBConnection() {
		// creating db connection
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
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
