package clientIDCreator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class getEndPoint {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		returnEndPoint("YCWYBK");

		System.out.println("ok");
		
	}
	
	public static String returnEndPoint(String customer_id) {
		String query;
		query = "SELECT server_address FROM customers where customer_id = '" + customer_id + "'";
		int exists = runSelectQuery(query);
		if (exists == 1){ //id is valid
			return getSelectOutput(query);
		}
		else { //results are empty -> there is no such id		
			return "ID NOT VALID";
		}
	}
	
	public static void createDBConnection() {
		// creating db connection
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        }
	}
	public static int runSelectQuery(String query) {
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
		    ResultSet rs = stmt.executeQuery(query);
		    
		    //check if no results
		    if (rs.next() == false) {
		        //System.out.println("ResultSet in empty in Java");
		    		rs.close();
				stmt.close();
				conn.close();
		        return 0;
		     } 
		    
		    rs.close();
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
	
	public static String getSelectOutput(String query) {
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
		    ResultSet rs = stmt.executeQuery(query);
		    
		    //check if no results
		    if (rs.next() == false) {
		        //System.out.println("ResultSet in empty in Java");
		    		rs.close();
				stmt.close();
				conn.close();
		        return "No Results";
		     } 
		    String str = rs.getString(1);
		    
		    rs.close();
			stmt.close();
			conn.close();
			
			System.out.println(str);
			return str;
		   
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		    
		    return "ERROR";
		}
	}
}
