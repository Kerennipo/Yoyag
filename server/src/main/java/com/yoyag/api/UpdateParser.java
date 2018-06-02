/**
 * 
 */
package com.yoyag.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author ben
 *
 */
public class UpdateParser implements Parser {

	/* (non-Javadoc)
	 * @see com.yoyag.api.Parser#parseInput(com.yoyag.api.Input)
	 */
	@Override
	public void parseInput(Input in) {
		UpdateInput uIn = (UpdateInput)in;
		System.out.println("DEBUG: got update request");
		
		String query =  "UPDATE patientData SET diagnosis = '" + uIn.getDiagnosis()+ "' where token = '" + uIn.getSessionID() + "';";
		System.out.println("DEBUG: update query='" + query + "'");
		runQuery(query);
		
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
}
