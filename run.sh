#! /bin/bash

javac src/ser322del4/Main.java 

cp src/ser322del4/Main.class classes/ser322del4/Main.class

java -cp lib/mysql-connector-java-8.0.21.jar:classes ser322del4.Main "jdbc:mysql://localhost/del4?autoReconnect=true&useSSL=false&useLegacyDatetimCode=false&serverTimezone=America/New_York" root root com.mysql.cj.jdbc.Driver
