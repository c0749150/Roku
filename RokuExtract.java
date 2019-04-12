/**
 *	@author Rafael, Varsh, Danilo, Sidharth
 *	@date 11-04-2019
 *
 *	This program extract all the actions that have happened
 *	to a file/folder that have been tracked by the Roku applicaton
 */
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RokuExtract {

	private static Connection connection = null; //To establish connection to SQL Server. The connection will stay open throughout the program
	
	public static void main(String[] args) { //There is only one argument to be taken which should be a file/folder path
		// TODO Auto-generated method stub
		File f = new File(args[0]); //file attribute to handle the file off which the structure is to be displayed. 
		String fileAbsolutePath = f.getAbsolutePath(); //Getting the absolute path of the file
		
		int filesID = 0; //Event ID argument
		String hierarchy = ""; //To be used to display hierarchy in the structure
		
		Statement statement = null; //To Execute statements in SQL Server
		ResultSet resultOfStatement = null; //To hold and access the result from executed statement
		
        String connectionUrl = //Creating the URL string to connect to SQL Server
        		"jdbc:sqlserver://localhost:1433;" //Using JDBC on a local machine on port 1433
        		+ "databaseName=RokuDB;" //RokuDB being target DB
        		+ "user=TestLogin;" //Username is TestLogin
        		+ "password=Asdf1234;"; //Password for the user is Asdf1234
        		//+ "integratedSecurity=true;"
        		//+ "authenticationScheme=JavaKerberos;";
        try{
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); //Locating and using the JDBC driver
        	connection = DriverManager.getConnection(connectionUrl); //Establishing the connection
        	if (connection != null) { //Verifying that the connection was successful
                System.out.println("Connection Successful!");
        	}
        	else { //If connection was not established the program would just end
        		System.exit(0);
        	}
        	statement = connection.createStatement(); //Establishing statement attribute which is to be used to execute a statement
        } catch (SQLException e) { //Catching an exception for if the statement attribute or connection was not able to establish
			// TODO Auto-generated catch block
			e.printStackTrace(); //Printing the error
		} catch (ClassNotFoundException e) { //Catching an exception for if the JDBC driver was not found
			// TODO Auto-generated catch block
			e.printStackTrace(); //Printing the error
		}
        
        try{
        	//Getting the latest event ID associated with the source file/folder path
			resultOfStatement = statement.executeQuery("SELECT TOP 1 FILES_ID FROM FILES "
					+ "WHERE FILES_FULL_PATH = '" + fileAbsolutePath + "' " //Using the absolute path got
					+ "ORDER BY FILES_START_DATE DESC, FILES_START_TIME DESC "); //Sorting to get the latest event only
			if (resultOfStatement.next()) { //Verifying that we did get some result
				filesID = resultOfStatement.getInt("FILES_ID"); //Getting the latest event ID
			} else { //If no result found then we are not currently logging the said file/folder
				System.exit(0);
			}
		} catch (SQLException e) { //Catching exception for statement execution
            e.printStackTrace(); //Printing the error
		}
        
        try{
        	/*
        	 * This step could be optimized by not using SQL but directly displaying the absolute path
        	 * But was done none the less to verify that the database was returning something
        	 */
        	hierarchy = "1"; //Starting the hierarchy structure with 1
        	resultOfStatement = statement.executeQuery("SELECT * FROM FILES " //Getting the current file/folder path
    				+ "WHERE FILES_ID = '" + filesID + "' ");
    		if(resultOfStatement.next()) { //If resulted something displaying the current file/folder path
    			System.out.println(hierarchy + ": " +  resultOfStatement.getString("FILES_FULL_PATH"));
    		}
        	getStructure(filesID, hierarchy + ".", 1); //Recursive function to create a structure
		} catch (SQLException e) { //Catching exception for statement execution
            e.printStackTrace(); //Printing the error
		} finally { //Closing the result, statement and connection
        	try { resultOfStatement.close(); } catch (Exception e) { /* ignored */ }
            try { statement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
        try { //Entering a pause so that user can see the output
        	System.out.print("Press enter to exit...");
			System.in.read(); //Waiting for the enter key
		} catch (IOException e) { //Catching if some error occurred while waiting for an enter key
			// TODO Auto-generated catch block
			e.printStackTrace(); //Printing the error
		}
        System.exit(0); //Exiting the system
	}

	//Recursive function to display the hierarchy structure
	private static void getStructure(int filesID, String hierarchy, int level) { //Event ID, Current hierarchy and current hierarchy level
		// TODO Auto-generated method stub
		int iherarchy = 1, i = 0; //Iterators
		Statement statement = null; //To Execute statements in SQL Server
		ResultSet resultOfStatement = null; //To hold and access the result from executed statement
		try {
			statement = connection.createStatement(); //Establishing statement attribute which is to be used to execute a statement
			resultOfStatement = statement.executeQuery("SELECT * FROM FILES " //Getting the file/folder path associated with the event ID
					+ "WHERE BASE_ID = '" + filesID + "' AND FILES_ACTION <> 'MARK'" //Excluding the Mark files as that can led to infinite loop
					+ "ORDER BY FILES_START_DATE ASC, FILES_START_TIME ASC "); //Sorting to get the closest event first
		
			while (resultOfStatement.next()) { //If resulted something displaying the file/folder paths and finding their structure too
				//Tree structure patterns
				System.out.print("|");
				for(i=1;i<level;i++) {
					System.out.print("   |");
				}
				
				//Structure result
				//If want to add details to be displayed, add here
				System.out.println("___" + hierarchy + iherarchy + ": " + resultOfStatement.getString("FILES_FULL_PATH") + " Action: " + resultOfStatement.getString("FILES_ACTION"));
				getStructure(resultOfStatement.getInt("FILES_ID"), hierarchy + iherarchy + ".", level+1); //Recursively calling the function to get all the deep tree structure
				iherarchy++; //Increasing the iterator to display hierarchy correctly
			}
		} catch (SQLException e1) { //Catching exception for statement execution
			// TODO Auto-generated catch block
			e1.printStackTrace(); //Printing the error
		} finally { //Closing the result and statement
			try { statement.close(); } catch (Exception e) { /* ignored */ }
			try { resultOfStatement.close(); } catch (Exception e) { /* ignored */ }
		}
	}

}
