package clientIDCreator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import javax.naming.NamingException;

public class IDCreator {

	public static void main(String[] args) throws SQLException, NamingException {
		
//		System.out.println("-------- MySQL JDBC Connection Testing ------------");
//
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//		} catch (ClassNotFoundException e) {
//			System.out.println("Where is your MySQL JDBC Driver?");
//			e.printStackTrace();
//			return;
//		}
//
//		System.out.println("MySQL JDBC Driver Registered!");
//		Connection connection = null;
//
////		try {
////			connection = DriverManager
////			.getConnection("jdbc:mysql://193.106.55.122:2222/yoyagDB?" +
////                    "user=project22&password=yoyagmysqlpwd!");
//		try {
//			connection = DriverManager
//			.getConnection("jdbc:mysql://localhost:3306/yoyagDB?" +
//                    "user=javauser&password=javaDBuser1!&useSSL=false");
////		try {
////			connection = DriverManager
////			.getConnection("jdbc:mysql://193.106.55.122:2222/yoyagDB?" +
////                    "user=root&password=yoyagmysqlpwd!&useSSL=false&max_allowed_packet=268435456");
//		} catch (SQLException e) {
//			System.out.println("Connection Failed! Check output console");
//			e.printStackTrace();
//			return;
//		}
//
//		if (connection != null) {
//			System.out.println("You made it, take control your database now!");
//		} else {
//			System.out.println("Failed to make connection!");
//		}
	  
		
		customerSighUp("TEST1","1.1.1.1");
		
		System.out.println("all good");
	}
	
	public static void customerSighUp(String customerName, String server_address) {
		String query;
		query = "SELECT name FROM customers where name = '" + customerName + "'";
		int exists = runSelectQuery(query);
		if (exists == 0) { //results are empty -> there is no such customer. need to create id			
			//getting current time to insert into table
			java.util.Date dt = new java.util.Date();
			java.text.SimpleDateFormat sdf = 
			     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dt);
		
			
			//generating id
			String id = gengerateID();
			
			//creating query
			query = "INSERT INTO customers VALUES ('" + customerName + "','" + id + "','" + currentTime + "','" + server_address + "');";
			
			//running query
			runQuery(query);
		}
		else if (exists == 1){ //customer exists
			System.out.println("Customer already exists. Please contact our support");
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
//		       DriverManager.getConnection("jdbc:mysql://193.106.55.122:2222/yoyagDB?" +
//		                                   "user=javauser&password=javaDBuser1!&useSSL=false");
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
	
	public static String gengerateID() {
		
		int leftLimit = 65; // letter 'a'
	    int rightLimit = 90; // letter 'z'
	    int targetStringLength = 6;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();
	    
	    //making sure the generated ID doesn't exist in the table
	    String query = "SELECT customer_id FROM customers WHERE customer_id = '" + generatedString +"';";
	    int exists = runSelectQuery(query);
	    if (exists == 0) { //results are empty -> there is no such id
	    		return generatedString;
		}
		else if (exists == 1){ //id exists
			while(exists == 1) {
				for (int i = 0; i < targetStringLength; i++) {
			        int randomLimitedInt = leftLimit + (int) 
			          (random.nextFloat() * (rightLimit - leftLimit + 1));
			        buffer.append((char) randomLimitedInt);
			    }
			    generatedString = buffer.toString();
			}
		}
	    
	    System.out.println(generatedString);
		
	    return generatedString;
	}
	
}
