SER 322 Group Project Deliverable #4

This is a program to be ran in the terminal which stores computer hardware information in a database.

Requirments:

- Java
- Must have MySQL connector.  We've used mysql-connector-java-8.0.21.jar for development
- Must have a database created in MySQL, using 'del4' as the database for development
	this can be done with the following command in MySQL:
		CREEATE DATABASE del4;
- Must have a classes/ser322del4/ directory to put the compiled class files in


How to run the program:

1) 
cd into this directory using your terminal

2) 
Compile the code by running the following bash command:
	javac src/ser322del4/Main.java
	
3)  
Move the compiled code into the classes/ser322del4/ directory:
	cp src/ser322del4/Main.class classes/ser322del4/Main.class

4)
Run the program in the terminal like so:
	java -cp lib/mysql-connector-java-8.0.21.jar:classes ser322del4.Main "jdbc:mysql://localhost/del4?autoReconnect=true&useSSL=false&useLegacyDatetimCode=false&serverTimezone=America/New_York" root root com.mysql.cj.jdbc.Driver
	