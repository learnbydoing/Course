import java.sql.*;
import java.text.NumberFormat;

public class Assignment7 {
	public static void main(String args[]) {

		final int MAX_COLUMN_LENGTH = 25; //change it to set the width of columns
		
        // Load the database driver
        // NOTE: This block is necessary for 10g and earlier
        // (JDBC 3.0), but not for 11g or later (JDBC 4.0)
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            System.out.println("Successfully Loaded class \"oracle.jdbc.OracleDriver\".");
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        if (args==null||args.length<2) {
        	System.out.println("Error: Please provide user credentials to connenct to the database server.\n");
        	return;
        }
        
        // define common JDBC objects
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            // Connect to the database
            String dbUrl =
                "jdbc:oracle:thin:@cdmoracledb.cti.depaul.edu:1521:def";
            String username = args[0];
            String password = args[1];
            connection = DriverManager.getConnection(
                dbUrl, username, password);

            System.out.println("Successfully connected to server.\n");
            System.out.println("------------------------------------------------------");
            
            // Create vendors7
            statement = connection.createStatement();
            statement.execute("CREATE TABLE vendors7 AS (SELECT * FROM vendors)");
            System.out.println("Table vendors7 created.\n");
            
            // Find the maximum vendor id
            String query = "SELECT MAX(vendor_id) AS max_vendor_id FROM vendors7";
            rs = statement.executeQuery(query);

            System.out.print("Try to find the maximum vendor id...");

            long maximumid = 1;
            if(rs.next()) {
            	maximumid = rs.getLong("max_vendor_id");
                System.out.print(" -> The current id is : " + maximumid+"\n");
            }
            rs.close();
            
            maximumid++;
            // Insert a new vendor
            query =
                "INSERT INTO vendors7 " +
                "(vendor_id,vendor_name,vendor_address1,vendor_address2,vendor_city, " +
                " vendor_state,vendor_zip_code,vendor_phone,vendor_contact_last_name, " +
                " vendor_contact_first_name,default_terms_id,default_account_number) " +
                " VALUES ( " + maximumid + "," +
                " 'Rong Zhuang, Inc.','2241 S Archer','Apt 3','Chicago','IL','60616', " +
                " '(312) 478-6579','Zhuang','Rong',3,500)";
            int ret = statement.executeUpdate(query);
            
            if (ret == 1)
                System.out.println("New vendor '"+maximumid+"' has been inserted to table vendors7.\n");
            else {
            	System.out.println("Error ocurrs when creating new vendor: " + maximumid + " !");
            	return;
            }
            
            // Display the newly created vendor
            //query = "SELECT * FROM vendors7 WHERE vendor_id = " + maximumid;
            query = "SELECT VENDOR_ID, VENDOR_NAME, VENDOR_CITY, VENDOR_STATE " 
                    + "FROM vendors7 "
                    + "WHERE vendor_id = " + maximumid;
            rs = statement.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            int colSize;
            
            //print column line
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rsmd.getColumnName(i));
                colSize = rsmd.getColumnDisplaySize(i);
                if (colSize < rsmd.getColumnName(i).length())
                	colSize = 1;
                else
                	colSize = colSize - rsmd.getColumnName(i).length();
                
                if (colSize+rsmd.getColumnName(i).length()>MAX_COLUMN_LENGTH)
                	colSize = MAX_COLUMN_LENGTH-rsmd.getColumnName(i).length();

                for (int j = 0; j < colSize; j++)
                    System.out.print(" ");
            }
            System.out.println("");
            
            //print separator line
            for (int i = 1; i <= columnCount; i++) {
                colSize = rsmd.getColumnDisplaySize(i);
                if (colSize < rsmd.getColumnName(i).length())
                	colSize = rsmd.getColumnName(i).length() + 1;

                if (colSize>MAX_COLUMN_LENGTH)
                	colSize = MAX_COLUMN_LENGTH;

                for (int j = 0; j < colSize; j++) {
                	if (j==colSize-1)
                		System.out.print(" ");
                	else
                		System.out.print("-");
                }
            }
            System.out.println("");
            
            //print data line
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {                    
                    System.out.print(rs.getString(i));          
                    
                    //add space for alignment
                    colSize = rsmd.getColumnDisplaySize(i);

                    if (colSize < rsmd.getColumnName(i).length())
                    	colSize = rsmd.getColumnName(i).length() + 1 - rs.getString(i).length();
                    else
                    	colSize = colSize - rs.getString(i).length();
                   
                    if (colSize+rs.getString(i).length()>MAX_COLUMN_LENGTH)
                    	colSize = MAX_COLUMN_LENGTH-rs.getString(i).length();

                    for (int j = 0; j < colSize; j++) {
                    	System.out.print(" ");
                    }
                }
                System.out.println("");
            }                        
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {        	
            try {
            	if (statement!=null) {
	            	statement.execute("DROP TABLE vendors7");
	                System.out.println("\nTable vendors7 dropped.");
            	}
                
                if (rs != null)
                    rs.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch(SQLException e) {
                e.printStackTrace();
            }        	
        }
    }	
}
