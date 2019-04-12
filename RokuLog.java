/**
 *	@author Rafael, Varsh, Danilo, Sidharth
 *	@date 11-04-2019
 *
 *	This program retain logs of all the actions that are happening
 *	to a file/folder that are being tracked by the Roku applicaton
 */

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.Timestamp;
import java.util.Date;

public class RokuLog {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		File f = null; //file attribute to handle the file to be logged
		String fileType = null, fileName = null, fileAbsolutePath = null, currentAction = null; //Parameters of the file to be logged
		
		Date date= new Date(); //To get the current timestamp
		long time = date.getTime(); //Getting the current timestamp
		String currentDate = "'" + new java.sql.Date(time) + "'"; //Setting date to be passed to sql server
		String currentTime = "'" + new java.sql.Time(time) + "'"; //Setting time to be passed to sql server
		int markedID = 1, baseID = 0; //The main event ID and source ID to be entered in the SQL Database
		boolean isMark = false, isDelete = false, isRename = false; //Flags to use for event specific instruction
		
		Connection connection = null; //To establish connection to SQL Server
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
        
		
		switch(Integer.parseInt(args[0])) { //Determining what operation is to be logged. First argument is always thhe operation type
		case 0: //For operation Marking
			currentAction = "'MARK'";
			f = new File(args[1]); //As there could be only file source but no destination, the next argument is the file/folder to be logged
			isMark = true; //Setting mark flag event
			break;
		case 1: //For file copy operation
		case 11: //For folder copy operation
			currentAction = "'COPY'";
			break;
		case 2: //For file cut/move operation
		case 22: //For folder cut/move operation
			currentAction = "'MOVE'";
			break;
		case 3: //For file or folder rename operation
			currentAction = "'RENAME'";
			isRename = true;  //Setting rename flag event
			break;
		case 4: //For file or folder delete operation
			currentAction = "'DELETE'";
			f = new File(args[1]); //As there could be only file source but no destination, the next argument is the file/folder to be logged
			isDelete = true;  //Setting delete flag event
		}
		
		if(!isMark) { //Only for non-marking events
			if(!isDelete && !isRename) { //If it's not mark, delete and rename the events have a source and destination full file/folder paths
				f = new File(args[2]);
			} else if(isRename) { //If rename event it has format of source file/folder path and only renamed file/folder name
				f = new File((new File(args[1])).getParent() + "\\" + args[2]); //Adding the parent path to the renamed file/folder name and pointing to that file
			}
			try{
				//Getting the latest event ID associated with the source file/folder path
				resultOfStatement = statement.executeQuery("SELECT TOP 1 FILES_ID FROM FILES "
						+ "WHERE FILES_FULL_PATH = '" + args[1] + "' " //Second argument for any event other than marking is the source file/folder path
						+ "ORDER BY FILES_START_DATE DESC, FILES_START_TIME DESC "); //Sorting to get the latest event only
				if (resultOfStatement.next()) { //Verifying that we did get some result
					baseID = resultOfStatement.getInt("FILES_ID"); //Getting the latest event ID
				} else { //If no result found then we are not currently logging the said file/folder
					System.exit(0);
				}
			} catch (SQLException e) { //Catching exception for statement execution
	            e.printStackTrace(); //Printing the error
			}
		}
		
		if(f.isFile()) { //Checking if the targeted path is a file or not
			fileType = "'FILE'";
		} else if(f.isDirectory()) { //If not file checking if the targeted path is a folder or not
			fileType = "'DIR'";
		} else { //If both scenario failed then that means the the path does not exist in the system
			System.out.println(f.getAbsolutePath()); //Printing the path for user debugging
			System.out.println("File not found!!!"); //Printing the error message
			System.exit(0);
		}
		
		//Setting file parameters
		fileName = "'" + f.getName() + "'";
		fileAbsolutePath = "'" + f.getAbsolutePath() + "'";
		
		try{
			resultOfStatement = statement.executeQuery("SELECT TOP 1 FILES_ID FROM FILES ORDER BY FILES_ID desc"); //Getting the last created event ID
        	if (resultOfStatement.next()) { //Checking if there was any event ID
        		markedID = resultOfStatement.getInt("FILES_ID") + 1; //Adding 1 to create a new event ID
        	} else { //There was no prior event ID
        		markedID = 1; //The logging starts from 1
        	}
        	
        	if(isMark) { //Only for marking event
        		baseID = markedID; //The source base ID will be the same as the event ID as there is no other source for this event
        	}
        	
        	// insert the data
        	statement.executeUpdate("INSERT INTO FILES "
        			+ "VALUES ("
        				+ markedID +", "
        				+ fileName + ", "
        				+ fileAbsolutePath + ", "
        				+ fileType + ", "
        				+ currentDate + ", "
        				+ currentTime + ", "
        				+ currentAction + ", "
        				+ baseID
        			+ ")");
		} catch (SQLException e) { //Catching exception for statement execution
            e.printStackTrace(); //Printing the error
        } finally { //Closing the result, statement and connection
        	try { resultOfStatement.close(); } catch (Exception e) { /* ignored */ }
            try { statement.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
        
        System.out.println("end"); //Printing end just for debugging
        System.exit(0);
	}

}
