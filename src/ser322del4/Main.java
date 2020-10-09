package ser322del4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public Main() {
    }    
    
    public static void main(String[] args) {
    	BufferedReader br;
		Connection conn = null;
		
		try {	
			String _url = args[0];
	
			// Step 1: Load the JDBC driver
			Class.forName(args[3]);
	
			// Step 2: make a connection
			conn = DriverManager.getConnection(_url, args[1], args[2]);
			dropTables(conn);
			createTables(conn);
			
			while(true) {
		        System.out.println("Computer Database");
		        System.out.println("");
		        System.out.println("Menu Options:");
		        System.out.println("1. add gpu");
		        System.out.println("2. add cpu");
		        System.out.println("3. add ram ");
		        System.out.println("4. add motherboard");
		        System.out.println("5. add brand");
		        System.out.println("6. delete gpu");
		        System.out.println("7. delete cpu");
		        System.out.println("8. delete ram ");
		        System.out.println("9. delete motherboard");
		        System.out.println("10. delete brand");
		        System.out.println("11. update gpu");
		        System.out.println("12. update cpu");
		        System.out.println("13. update ram ");
		        System.out.println("14. update motherboard");
		        System.out.println("15. update brand");
		        System.out.println("16. select all gpus");
		        System.out.println("17. select all cpus");
		        System.out.println("18. select all ram");
		        System.out.println("19. select all motherboards");
		        System.out.println("20. select all brands");
		        System.out.println("21. special query 1 (Select all brands that have an entry in each table)");
		        System.out.println("22. special query 2 (Select motherboards with GPUs with matching PCI Slots)");
		        System.out.println("23. special query 3	(Select CPUs with matching motherboard chip sockes");
		        System.out.println("24. sepcial query 4 (Select all motherboards that can support an Intel chip");
		        System.out.println("25. special query 5 (Select all AMD GPU");
		        System.out.println("26. *** STOP PROGRAM ***");
		        System.out.print("Please select an option from 1-26\r\n");        
		        
		        br = new BufferedReader(new InputStreamReader(System.in));        
		        
		        try {
		            int input = Integer.parseInt(br.readLine());            
		            
		            if(input < 1 || input > 26) {
		                System.out.println("You have entered an invalid selection, please try again\r\n");
		            } else if(input == 26) {
		                System.out.println("You have quit the program\r\n");
		                System.exit(1);
		            } else {
		                System.out.println("You have entered " + input + "\r\n");
		                
		                switch(input) {
		                	case 1:
		                		insertGpu(conn);
		                		break;
		                	case 2:
		                		insertCpu(conn);
		                		break;
		                	case 3:
		                		insertRam(conn);
		                		break;
		                	case 4:
		                		insertMotherboard(conn);
		                		break;	                		
		                	case 5:
	                			insertBrand(conn);
		                		break;
		                	case 6:
		                		deleteGpu(conn);
		                		break;
		                	case 7:
		                		deleteCpu(conn);
		                		break;
		                	case 8:
		                		deleteRam(conn);
		                		break;
		                	case 9:
		                		deleteMotherboard(conn);
		                		break;
		                	case 10:
		                		deleteBrand(conn);
		                		break;
		                		
//		                		*** NEED TO IMPLEMENT THE UPDATE METHODS ***
		                		
		                	case 16:
		                		selectGpus(conn);
		                		break;
		                	case 17:
		                		selectCpus(conn);
		                		break;
		                	case 18:
		                		selectRam(conn);
		                		break;
		                	case 19:
		                		selectMotherboards(conn);
		                		break;
		                	case 20:
		                		selectBrands(conn);
		                		break;
		                	case 21:
		                		special1(conn);
		                		break;
		                	case 22:
		                		special2(conn);
		                		break;
		                	case 23:
		                		special3(conn);
		                		break;
		                	case 24:
		                		special4(conn);
		                		break;
		                	case 25:
		                		special5(conn);
		                		break;
		                		
		                		
	                		default:
	                			System.out.println("*** Method not implemented yet ***");
		                }
		            }
		        } catch (Exception ioe) {
		            System.out.println("Error trying to read your input!\r\n");
		            System.exit(1);
		        }
		        
		        System.out.print("Press enter to continue...");        
	        	br = new BufferedReader(new InputStreamReader(System.in));
	        	try {
	        		br.readLine();
	        	}
	        	catch (Exception e) {
	        		e.printStackTrace();
	        	}
		    }
		}
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
    }
    
    public static Integer getInt(String prompt) {
        System.out.println(prompt);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));        
		Integer input = 0;
        try{
            input = Integer.parseInt(br.readLine());
        }
        catch (Exception e){
        	e.printStackTrace();
        }
    	return input;
    }   
    
    public static BigDecimal getDec(String prompt) {
        System.out.println(prompt);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));        
		BigDecimal input = new BigDecimal(0);
		
        try {
            input = new BigDecimal(br.readLine());
        }
        catch (Exception e){
        	e.printStackTrace();
        }
    	return input;
    }   
    
    public static String getStr(String prompt) {
        System.out.println(prompt);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));        
		String input = "";
		
        try {
            input = br.readLine();
        }
        catch (Exception e){
        	e.printStackTrace();
        }
        
        System.out.println("provided input: '" + input + "'");
    	return input;
    }

	public static void pprintResultSet(ResultSet rs) {
		ResultSetMetaData metaData = null;
		
		try {
			metaData = rs.getMetaData();

			pprintLineEntryHeader(metaData);
			
			List<Map.Entry<String, String>> row = new ArrayList<>();
			while (rs.next()) {
				for(int i = 1; i <= metaData.getColumnCount(); i++) {
					row.add(pprintLineEntry(rs, metaData, i));
				}
				pprintLine(row);
				row = new ArrayList<>();
			}
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
	
	public static void pprintLine(List<Map.Entry<String, String>> content) {
		System.out.print("| ");
		for(int i = 0; i < content.size(); i++) {
			System.out.print(String.format(content.get(i).getKey(), content.get(i).getValue()));
			System.out.print(" | ");;
		}
		
		System.out.println("");
	}

	public static void pprintLineEntryHeader(ResultSetMetaData metaData) {
		try {
			int width = 1;
			List<Map.Entry<String, String>> header = new ArrayList<>();
			Map.Entry<String, String> e = null;
			
			for(int i = 1; i <= metaData.getColumnCount(); i++) {
				String f = "%" + (metaData.getColumnDisplaySize(i) + 2) + "s";
				String s = metaData.getColumnLabel(i);
				e = new AbstractMap.SimpleEntry<>(f, s);
				header.add(e);
				width += metaData.getColumnDisplaySize(i) + 5;
			}

			pprintLine(header);
			
			for(int i = 0; i < width; i++) {
				System.out.print("-");
			}
			System.out.println("");
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public static Map.Entry<String, String> pprintLineEntry(ResultSet rs, ResultSetMetaData metaData, int i) {
		Map.Entry<String, String> e = null;
		
		try {
			
			String f = "%" + (metaData.getColumnDisplaySize(i) + 2) + "s";
					
			String s = getColumnAsString(rs, metaData, i);
			e = new AbstractMap.SimpleEntry<>(f, s);
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		
		return e;
	}
	
	public static String getColumnAsString(ResultSet rs, ResultSetMetaData metaData, int i) {
		String s = "";
		
		try {
			switch(metaData.getColumnType(i)) {
				case Types.VARCHAR:
				case Types.CHAR:
					s = rs.getString(i);
					break;
				case Types.DECIMAL:
				case Types.FLOAT:
				case Types.DOUBLE:
					s = "" + rs.getFloat(i);
					break;
				default:
					s = "" + rs.getInt(i);
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		
		return s;
	}
    
    public static void createTables(Connection conn) {
    	ResultSet rs = null;
		PreparedStatement stmt = null;
		

		ArrayList<String> queries = new ArrayList<>();

		queries.add("CREATE TABLE brand (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,name VARCHAR(50));");
		queries.add("CREATE TABLE ram (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,brandId INT NOT NULL,type VARCHAR(50),memorySize INT,price DOUBLE,FOREIGN KEY(brandId) REFERENCES brand(id));");
		queries.add("CREATE TABLE cpu (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,brandId INT NOT NULL,type VARCHAR(50),clockSpeed DOUBLE,cores INT,socketType VARCHAR(50),power DOUBLE,price DOUBLE,FOREIGN KEY(brandId) REFERENCES brand(id));");
		queries.add("CREATE TABLE gpu (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,brandId INT NOT NULL,series VARCHAR(50),memorySize int,ramType VARCHAR(50),clockSpeed DOUBLE,power DOUBLE,pciSlot VARCHAR(50),price DOUBLE,FOREIGN KEY(brandId) REFERENCES brand(id));");
		queries.add("CREATE TABLE motherboard (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,brandId INT NOT NULL,name VARCHAR(50),cpuSocket VARCHAR(50),numRamSlots INT,ramType VARCHAR(50),maxRam int,layout VARCHAR(50),pciSlot VARCHAR(50),price DOUBLE,FOREIGN KEY(brandId) REFERENCES brand(id));");

		for(String q : queries) {
			try {
	//			stmt = conn.prepareStatement("INSERT INTO CUSTOMER SET CUSTID=?, NAME=?, QUANTITY=?, PID=(select PRODUCT.PRODID from PRODUCT where PRODUCT.PRODID = ? limit 1)");
				stmt = conn.prepareStatement(q);
	//		    stmt.setInt(1, Integer.parseInt(customer_id));
	//		    stmt.setString(2, name);
	//		    stmt.setInt(3, Integer.parseInt(quantity));
	//		    stmt.setInt(4, Integer.parseInt(product_id));
	
			    if (stmt.executeUpdate() > 0) {
			    	System.out.println("SUCCESS");
			    }
			}
			catch (Exception exc)
			{
				exc.printStackTrace();
			}
		}
//		finally {  // ALWAYS clean up your DB resources
//			try {
//				if (rs != null)
//					rs.close();
//				if (stmt != null)
//					stmt.close();
//			}
//			catch (SQLException se) {
//				se.printStackTrace();
//			}
//		}

    }
    
    public static void dropTables(Connection conn) {
		PreparedStatement stmt = null;

		ArrayList<String> queries = new ArrayList<>();

		queries.add("DROP TABLE IF EXISTS ram CASCADE;");
		queries.add("DROP TABLE IF EXISTS cpu CASCADE;");
		queries.add("DROP TABLE IF EXISTS gpu CASCADE;");
		queries.add("DROP TABLE IF EXISTS motherboard CASCADE;");
		queries.add("DROP TABLE IF EXISTS brand CASCADE;");
		
		for(String q : queries) {
			try {
				stmt = conn.prepareStatement(q);
	
			    if (stmt.executeUpdate() > 0) {
			    	System.out.println("SUCCESS");
			    }
			}
			catch (Exception exc)
			{
				exc.printStackTrace();
			}
		}

		// ALWAYS clean up your DB resources
		try {
			if (stmt != null)
				stmt.close();
		}
		catch (SQLException se) {
			se.printStackTrace();
		}
	
    }

    // 1 insert a GPU
    public static void insertGpu(Connection conn) {
		PreparedStatement stmt = null;
		
		try {
	
			String query = "INSERT INTO gpu (series, memorySize, ramType, clockSpeed, power, pciSlot, price, brandId) VALUES(?, ?, ?, ?, ?, ?, ?, (SELECT id FROM brand WHERE name = ?));";
			stmt = conn.prepareStatement(query);
			
	        System.out.println("Please enter the following for your GPU:"); 
	        stmt.setString(1, getStr("Series (string): "));  
		    stmt.setInt(2, getInt("Memory Size (int):"));
	        stmt.setString(3, getStr("RAM Type (string): ")); 
	        stmt.setBigDecimal(4, getDec("Clock Speed (decimal):")); 
	        stmt.setBigDecimal(5, getDec("Power (decimal): ")); 
	        stmt.setString(6, getStr("PCI Slot (string): ")); 
	        stmt.setBigDecimal(7, getDec("Price (decimal): ")); 
	        stmt.setString(8, getStr("Brand (string):"));
        

		    if (stmt.executeUpdate() > 0) {
		    	System.out.println("SUCCESS");
		    }
		}
		catch (SQLException se) {
			se.printStackTrace();
			System.out.println("\nError, please make sure that you entered everything correctly and that the brand exists before.");
		}	
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
		finally {
			// ALWAYS clean up your DB resources
			try {
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException se) {
				se.printStackTrace();
			}	
	    }
    }

    // 2 insert a CPU
    public static void insertCpu(Connection conn) {
		PreparedStatement stmt = null;
		
		try {
	
			String query = "INSERT INTO cpu (type, clockSpeed, cores, socketType, power, price, brandId) VALUES (?, ?, ?, ?, ?, ?, (SELECT id FROM brand WHERE name = ?));";
			stmt = conn.prepareStatement(query);
			
			System.out.println("Please enter the following for your CPU:"); 
	        stmt.setString(1, getStr("Type (string): "));  
	        stmt.setBigDecimal(2, getDec("Clock Speed (decimal):"));
		    stmt.setInt(3, getInt("Cores (int):"));
	        stmt.setString(4, getStr("Socket Type (string): "));  
	        stmt.setBigDecimal(5, getDec("Power (decimal): ")); 
	        stmt.setBigDecimal(6, getDec("Price (decimal): ")); 
	        stmt.setString(7, getStr("Brand (string):"));
        

		    if (stmt.executeUpdate() > 0) {
		    	System.out.println("SUCCESS");
		    }
		}
		catch (SQLException se) {
			se.printStackTrace();
			System.out.println("\nError, please make sure that you entered everything correctly and that the brand exists before.");
		}	
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
		finally {
			// ALWAYS clean up your DB resources
			try {
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException se) {
				se.printStackTrace();
			}	
	    }
    }

    // 3 insert a Ram
    public static void insertRam(Connection conn) {
		PreparedStatement stmt = null;
		
		try {
	
			String query = "INSERT INTO ram (type, memorySize, price, brandId) VALUES (?, ?, ?, (SELECT id FROM brand WHERE name = ?));";
			stmt = conn.prepareStatement(query);
			
			System.out.println("Please enter the following for your RAM:"); 
	        stmt.setString(1, getStr("Type (string): "));  
		    stmt.setInt(2, getInt("Memory Size (int):"));
	        stmt.setBigDecimal(3, getDec("Price (decimal): ")); 
	        stmt.setString(4, getStr("Brand (string):"));
        

		    if (stmt.executeUpdate() > 0) {
		    	System.out.println("SUCCESS");
		    }
		}
		catch (SQLException se) {
			se.printStackTrace();
			System.out.println("\nError, please make sure that you entered everything correctly and that the brand exists before.");
		}	
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
		finally {
			// ALWAYS clean up your DB resources
			try {
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException se) {
				se.printStackTrace();
			}	
	    }
    }

    // 4 insert a Motherboard
    public static void insertMotherboard(Connection conn) {
		PreparedStatement stmt = null;
		
		try {
	
			String query = "INSERT INTO motherboard(name, cpuSocket, numRamSlots, ramType, maxRam, layout, pciSlot, price, brandId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, (SELECT id FROM brand WHERE name = ?));";
			stmt = conn.prepareStatement(query);
			
			System.out.println("Please enter the following for your motherboard:"); 
	        stmt.setString(1, getStr("Name (string): ")); 
	        stmt.setString(2, getStr("CPU Socket (string): "));  
		    stmt.setInt(3, getInt("Number of RAM Slots(int):"));
	        stmt.setString(4, getStr("Type of RAM (string): ")); 
		    stmt.setInt(5, getInt("Max RAM in GB (int):"));  
	        stmt.setString(6, getStr("Layout (string): "));
	        stmt.setString(7, getStr("PCI Slot (string): "));   
	        stmt.setBigDecimal(8, getDec("Price (decimal): ")); 
	        stmt.setString(9, getStr("Brand (string):"));
        

		    if (stmt.executeUpdate() > 0) {
		    	System.out.println("SUCCESS");
		    }
		}
		catch (SQLException se) {
			se.printStackTrace();
			System.out.println("\nError, please make sure that you entered everything correctly and that the brand exists before.");
		}	
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
		finally {
			// ALWAYS clean up your DB resources
			try {
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException se) {
				se.printStackTrace();
			}	
	    }
    }
    
    // 5 insert a Brand
    public static void insertBrand(Connection conn) {
		PreparedStatement stmt = null;
		
		try {
			String query = "INSERT INTO brand SET name=?";
			stmt = conn.prepareStatement(query);
			
	        System.out.println("Please enter the following for your Brand:"); 
	        stmt.setString(1, getStr("Name(string): "));  
        

		    if (stmt.executeUpdate() > 0) {
		    	System.out.println("SUCCESS");
		    }
		}
		catch (SQLException se) {
			se.printStackTrace();
			System.out.println("\nError, please make sure that you entered everything correctly.");
		}	
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
		finally {
			// ALWAYS clean up your DB resources
			try {
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException se) {
				se.printStackTrace();
			}	
	    }
    }

    // 6 delete a GPU
    public static void deleteGpu(Connection conn) {
		PreparedStatement stmt = null;
		
		try {
	
			String query = "DELETE FROM gpu WHERE id = ?;";
			stmt = conn.prepareStatement(query);
			int id = getInt("Please enter the id of the GPU you would like to delete or -1 to return to the menu (int):");
			
		    stmt.setInt(1, id);
		    
		    if (id >= 0 && stmt.executeUpdate() > 0) {
		    	System.out.println("SUCCESS");
		    }
		    else {
		    	System.out.println("No row was deleted, returning to menu");
		    }
		}
		catch (SQLException se) {
			se.printStackTrace();
			System.out.println("\nError, please make sure that you entered everything correctly and that the brand exists before.");
		}	
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
		finally {
			// ALWAYS clean up your DB resources
			try {
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException se) {
				se.printStackTrace();
			}	
	    }
    }

    // 7 delete a CPU
    public static void deleteCpu(Connection conn) {
		PreparedStatement stmt = null;
		
		try {
	
			String query = "DELETE FROM cpu WHERE id = ?;";
			stmt = conn.prepareStatement(query);
			int id = getInt("Please enter the id of the CPU you would like to delete or -1 to return to the menu (int):");
			
		    stmt.setInt(1, id);
		    
		    if (id >= 0 && stmt.executeUpdate() > 0) {
		    	System.out.println("SUCCESS");
		    }
		    else {
		    	System.out.println("No row was deleted, returning to menu");
		    }
		}
		catch (SQLException se) {
			se.printStackTrace();
			System.out.println("\nError, please make sure that you entered everything correctly and that the brand exists before.");
		}	
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
		finally {
			// ALWAYS clean up your DB resources
			try {
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException se) {
				se.printStackTrace();
			}	
	    }
    }

    // 8 delete a RAM
    public static void deleteRam(Connection conn) {
		PreparedStatement stmt = null;
		
		try {
	
			String query = "DELETE FROM ram WHERE id = ?;";
			stmt = conn.prepareStatement(query);
			int id = getInt("Please enter the id of the RAM you would like to delete or -1 to return to the menu (int):");
			
		    stmt.setInt(1, id);
		    
		    if (id >= 0 && stmt.executeUpdate() > 0) {
		    	System.out.println("SUCCESS");
		    }
		    else {
		    	System.out.println("No row was deleted, returning to menu");
		    }
		}
		catch (SQLException se) {
			se.printStackTrace();
			System.out.println("\nError, please make sure that you entered everything correctly and that the brand exists before.");
		}	
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
		finally {
			// ALWAYS clean up your DB resources
			try {
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException se) {
				se.printStackTrace();
			}	
	    }
    }

    // 9 delete a Motherboard
    public static void deleteMotherboard(Connection conn) {
		PreparedStatement stmt = null;
		
		try {
	
			String query = "DELETE FROM motherboard WHERE id = ?;";
			stmt = conn.prepareStatement(query);
			int id = getInt("Please enter the id of the Motherboard you would like to delete or -1 to return to the menu (int):");
			
		    stmt.setInt(1, id);
		    
		    if (id >= 0 && stmt.executeUpdate() > 0) {
		    	System.out.println("SUCCESS");
		    }
		    else {
		    	System.out.println("No row was deleted, returning to menu");
		    }
		}
		catch (SQLException se) {
			se.printStackTrace();
			System.out.println("\nError, please make sure that you entered everything correctly and that the brand exists before.");
		}	
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
		finally {
			// ALWAYS clean up your DB resources
			try {
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException se) {
				se.printStackTrace();
			}	
	    }
    }

    // 10 delete a Brand
    public static void deleteBrand(Connection conn) {
		PreparedStatement stmt = null;
		
		try {
	
			String query = "DELETE FROM brand WHERE id = ?;";
			stmt = conn.prepareStatement(query);
			int id = getInt("Please enter the id of the Brand you would like to delete or -1 to return to the menu (int):");
			
		    stmt.setInt(1, id);
		    
		    if (id >= 0 && stmt.executeUpdate() > 0) {
		    	System.out.println("SUCCESS");
		    }
		    else {
		    	System.out.println("No row was deleted, returning to menu");
		    }
		}
		catch (SQLException se) {
			se.printStackTrace();
			System.out.println("\nError, please make sure that you entered everything correctly and that the brand exists before.");
		}	
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
		finally {
			// ALWAYS clean up your DB resources
			try {
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException se) {
				se.printStackTrace();
			}	
	    }
    }
    
    // 16, select all GPUs
    public static void selectGpus(Connection conn) {
    	ResultSet rs = null;
		Statement stmt = null;
		
		System.out.println("GPUs: ");
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM gpu;");
			pprintResultSet(rs);	
			
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
    
    // 17, select all CPUs
    public static void selectCpus(Connection conn) {
    	ResultSet rs = null;
		Statement stmt = null;

		System.out.println("CPUs: ");
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM cpu;");
			pprintResultSet(rs);	
			
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
    
    // 18, select all RAM
    public static void selectRam(Connection conn) {
    	ResultSet rs = null;
		Statement stmt = null;

		System.out.println("RAM: ");
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ram;");
			pprintResultSet(rs);	
			
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
    
    // 19, select all Motherboard
    public static void selectMotherboards(Connection conn) {
    	ResultSet rs = null;
		Statement stmt = null;

		System.out.println("Motherboards: ");
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM motherboard;");
			pprintResultSet(rs);	
			
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}

    // 20, select all Brands
    public static void selectBrands(Connection conn) {
		Statement stmt = null;
		ResultSet rs = null;

		System.out.println("Brands: ");
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM brand;");

			pprintResultSet(rs);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
    
    // 21, Special Select #1 -- Returns all brands that have an entry in each table
    public static void special1(Connection conn) {
		Statement stmt = null;
		ResultSet rs = null;

		System.out.println("Returning all brand ids that have an entry in each table: ");
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT id as brand " + 
									"FROM brand " + 
									"WHERE id IN (SELECT DISTINCT b.id " + 
									"                 FROM brand b, motherboard m, ram r, cpu c, gpu g " + 
									" WHERE b.id = m.brandId AND b.id = r.brandId AND b.id  =c.brandId AND b.id = g.brandId)");

			pprintResultSet(rs);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
    
    // 22, Special Select #2 -- Select motherboards GPU ids with matching PCI slots
    public static void special2(Connection conn) {
		Statement stmt = null;
		ResultSet rs = null;

		System.out.println("Returning all Motherboard and GPU ids with matching PCI slots: ");
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT m.id as mobo, g.id as gpu " + 
									"FROM motherboard m, gpu g " + 
									"WHERE g.pciSlot <= m.pciSlot;");

			pprintResultSet(rs);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
    
    // 23, Special Select #3 -- Select CPU and Motherboard IDs with matching chip sockets
    public static void special3(Connection conn) {
		Statement stmt = null;
		ResultSet rs = null;

		System.out.println("Returning all CPU and Motherboard IDs with matching chip sockets: ");
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT m.id as mobo, c.id as cpu " + 
									"FROM motherboard m, cpu c " + 
									"WHERE m.cpuSocket = c.socketType;");	

			pprintResultSet(rs);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
    
    // 24, Special Select #4 -- Select all motherboards that can support an Intel chip
    public static void special4(Connection conn) {
		Statement stmt = null;
		ResultSet rs = null;

		System.out.println("Returning all motherboard ids that can support an Intel chip: ");
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT m.id as mobo " + 
									"FROM motherboard m, cpu c " + 
									"WHERE m.cpuSocket IN (SELECT socketType " + 
									"      FROM cpu " + 
									"      WHERE brandId = (SELECT id " + 
									"                 FROM brand " + 
									"                  WHERE name = 'Intel'));");	

			pprintResultSet(rs);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
    
    // 25, Special Select #5 -- Select all AMD GPUs
    public static void special5(Connection conn) {
		Statement stmt = null;
		ResultSet rs = null;

		System.out.println("Returning all AMD GPU ids: ");
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT g.id as gpu " + 
									"FROM gpu g, brand b " + 
									"WHERE g.brandid = b.id AND b.name = 'AMD';");	

			pprintResultSet(rs);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
}