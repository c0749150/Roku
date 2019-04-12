# Roku
Roku Application

Requirements:

1. Microsoft SQL Server
	1.1 RokuDB
	1.2 Files table
	1.3 Login
		1.3.1 Username - TestLogin & Password - Asdf1234
		1.3.2 RokuDB ownership
		1.3.3 Server admin privilage
		1.3.4 Turn on login via username
	1.4 Microsoft JDBC (to connect to SQL Server)
		1.4.1 Unzip to C:\Program Files\Microsoft JDBC Driver 7.2 for SQL Server
			1.4.1.1 Requires to be run as administrator
		1.4.2 Configure CLASSPATH
2. Oracle java jdk
	2.1 JDK version 12 used here
	2.2 Configure JAVA_HOME
		2.2.1 This enables to change jdk whenever required

Environment variables:

1. JAVA_HOME
C:\Program Files\Java\jdk-12

2. CLASSPATH
C:\Program Files\Microsoft JDBC Driver 7.2 for SQL Server\sqljdbc_7.2\enu\mssql-jdbc-7.2.1.jre11.jar
C:\Test folder

This program assumes that the Roku application is in C:\Test folder, 
if you want to install in any other place you will have to change this directory path in every single file.
