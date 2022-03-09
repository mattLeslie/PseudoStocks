package mtleslie_CSCI201L_Assignment4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class JDBCConnector {
	
	public static void cleanPortfolio(Integer pID) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
			st = conn.prepareStatement("DELETE FROM portfolios WHERE portfolios_id = " + pID);
			st.executeUpdate();
		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in cleanPortfolio.");
			System.out.println(e.getMessage());
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}
	}
	
	public static boolean checkIfOwned(String userID, String ticker) {
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		boolean owned = false;
		
		Integer uID = null;
		try {
			uID = Integer.parseInt(userID);
		}
		catch(NumberFormatException nfe) {
			System.out.println("could not parse user_id");
			return owned;
		}
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
			st = conn.prepareStatement("SELECT * FROM portfolios WHERE user_id = " + uID);
			rs = st.executeQuery();
			while(rs.next()) {
				String ownedStock = rs.getString("ticker");
				if(ownedStock.equals(ticker)){
					owned = true;
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in getPortfolio.");
			System.out.println(e.getMessage());
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}

		return owned;
	}
	
	public static boolean addToPortfolio(String userID, String ticker, Integer quantity, Double price) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		boolean added = false;
		boolean valid = true;
		Integer uID = null;
		
		java.util.Date date = new Date();
		Object time = new java.sql.Timestamp(date.getTime());
		
		try {
			uID = Integer.parseInt(userID);
		}
		catch(NumberFormatException nfe) {
			System.out.println("could not parse user_id");
			valid = false;
		}
		try
		{
			if(valid) {
				Class.forName("com.mysql.cj.jdbc.Driver"); 
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
				st = conn.prepareStatement("INSERT INTO portfolios (user_id, shares, cost, ticker, datetime) VALUES(" + uID + "," + quantity + "," + price + ",\"" + ticker +"\",\"" + time + "\")");
				st.executeUpdate();	
				added = true;
			}
		} 
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in addFavorite.");
			System.out.println(e.getMessage());
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}
		if(!added) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public static Double getCash(String userID) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		Double cash = null;
		Integer uID = null;
		try {
			uID = Integer.parseInt(userID);
		}
		catch(NumberFormatException nfe) {
			System.out.println("could not parse user_id");
		}
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
			st = conn.prepareStatement("SELECT cash FROM users WHERE user_id = " + uID);
			rs = st.executeQuery();
			rs.next();
			cash = rs.getDouble("cash");
	

		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in getFavorites.");
			System.out.println(e.getMessage());
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}
		return cash;
	}
	
	public static HashMap<String, Integer> getPortfolio(String userID){
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		HashMap<String, Integer> results = new HashMap<String, Integer>();
		
		Integer uID = null;
		try {
			uID = Integer.parseInt(userID);
		}
		catch(NumberFormatException nfe) {
			System.out.println("could not parse user_id");
			return results;
		}
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
			st = conn.prepareStatement("SELECT ticker, shares FROM portfolios WHERE user_id = " + uID + " ORDER BY ticker");
			rs = st.executeQuery();
			while(rs.next()) {
				String ticker = rs.getString("ticker");
				Integer shares = rs.getInt("shares");
				if(results.containsKey(ticker)){
					results.put(ticker, shares + (results.get(ticker)));
				}
				else {
					results.put(ticker, shares);
				}
			}
			return results;
		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in getPortfolio.");
			System.out.println(e.getMessage());
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}

		return results;

	}

	public static Double getTotalCost(String userID, String ticker) {
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		Double totCost = 0.0;
		
		Integer uID = null;
		try {
			uID = Integer.parseInt(userID);
		}
		catch(NumberFormatException nfe) {
			System.out.println("could not parse user_id");
		}
		
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
			st = conn.prepareStatement("SELECT * FROM portfolios WHERE user_id = " + uID + " AND ticker = \"" +ticker + "\"");
			rs = st.executeQuery();
			while(rs.next()) {
				Double cost = rs.getDouble("cost");
				Integer shares = rs.getInt("shares");
				totCost += (cost*shares);
			}
		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in cleanPortfolio.");
			System.out.println(e.getMessage());
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}
		
		return totCost;
	}
	
	public static boolean updatePortfolio(String userID, String ticker, Integer quantity) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		Integer uID = null;
		boolean updated = false;
		
		try {
			uID = Integer.parseInt(userID);
		}
		catch(NumberFormatException nfe) {
			System.out.println("could not parse user_id");
		}

		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
			st = conn.prepareStatement("SELECT * FROM portfolios WHERE user_id =" + uID + " AND ticker= \"" + ticker +"\" ORDER BY datetime");
			rs = st.executeQuery();
			while(rs.next() && quantity > 0) {
				Integer pID = rs.getInt("portfolios_id");
				Integer shares = rs.getInt("shares");
				
				if(shares > quantity) {
					shares = shares - quantity;
					quantity = 0;
				}
				else if(shares == quantity) {
					shares = shares-quantity;
					quantity = 0;
					cleanPortfolio(pID);
				}
				else {
					quantity = quantity - shares;
					shares = 0;
					cleanPortfolio(pID);
				}
				PreparedStatement st2 = conn.prepareStatement("UPDATE portfolios SET shares = " + shares + " WHERE portfolios_id = " + pID);
				st2.executeUpdate();
				st2.close();
			}

			updated = true;

		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in getFavorites.");
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}
		return updated;
	}

	public static boolean sellShares(String userID, String ticker, Integer quantity, Double price) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		boolean sold = false;
		
		Integer uID = null;
		try {
			uID = Integer.parseInt(userID);
		}
		catch(NumberFormatException nfe) {
			System.out.println("could not parse user_id");
			return false;
		}

		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
			st = conn.prepareStatement("SELECT cash FROM users WHERE user_id = " + uID);
			rs = st.executeQuery();
			rs.next();
			Double cash = rs.getDouble(1);
			cash += (quantity * price);
			PreparedStatement st2 = conn.prepareStatement("UPDATE users SET cash = " + cash + " WHERE user_id = " + uID);
			st2.executeUpdate();
			sold = true;
			updatePortfolio(userID, ticker, quantity);
			st2.close();
			
		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in getFavorites.");
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}
		return sold;
	}
	
	public static boolean buyShares(String userID, String ticker, Integer quantity, Double price) {

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		boolean bought = false;
		
		Integer uID = null;
		try {
			uID = Integer.parseInt(userID);
		}
		catch(NumberFormatException nfe) {
			System.out.println("could not parse user_id");
			return false;
		}

		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
			st = conn.prepareStatement("SELECT cash FROM users WHERE user_id = " + uID);
			rs = st.executeQuery();
			rs.next();
			Double cash = rs.getDouble(1);
			if((quantity * price) > cash) {
				bought = false;
			}
			else {
				cash -= (quantity * price);
				PreparedStatement st2 = conn.prepareStatement("UPDATE users SET cash = " + cash + " WHERE user_id = " + uID);
				st2.executeUpdate();
				bought = true;
				st2.close();
				addToPortfolio(userID, ticker, quantity, price);
			}
		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in getFavorites.");
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}
		return bought;
	}
	
	public static boolean addFavorite(String userID, String ticker) {

		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		boolean added = false;
		boolean valid = true;
		Integer uID = null;
		try {
			uID = Integer.parseInt(userID);
		}
		catch(NumberFormatException nfe) {
			System.out.println("could not parse user_id");
			valid = false;
		}
		try
		{
			if(valid) {
				Class.forName("com.mysql.cj.jdbc.Driver"); 
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
				st = conn.prepareStatement("INSERT INTO favorites (user_id, ticker) VALUES(" + uID + ",\"" + ticker +"\")");
				st.executeUpdate();	
				added = true;
			}
		} 
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in addFavorite.");
			System.out.println(e.getMessage());
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}
		if(!added) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public static boolean removeFavorite(String userID, String ticker) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		boolean added = false;
		boolean valid = true;
		Integer uID = null;
		try {
			uID = Integer.parseInt(userID);
		}
		catch(NumberFormatException nfe) {
			System.out.println("could not parse user_id");
			valid = false;
		}
		try
		{
			if(valid) {
				Class.forName("com.mysql.cj.jdbc.Driver"); 
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
				st = conn.prepareStatement("SELECT * FROM favorites WHERE user_id = " + uID + " AND ticker = \"" + ticker + "\"");
				rs = st.executeQuery();
				rs.next();
				Integer fID = rs.getInt("favorites_id");
				
				PreparedStatement st2 = conn.prepareStatement("DELETE FROM favorites WHERE favorites_id = " + fID);
				st2.executeUpdate();
				st2.close();
				added = true;
			}
		} 
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in removeFavorite.");
			System.out.println(e.getMessage());
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}
		if(!added) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public static ArrayList<String> getFavorites(String userID){
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		ArrayList<String> tickers = new ArrayList<String>();
		
		Integer uID = null;
		try {
			uID = Integer.parseInt(userID);
		}
		catch(NumberFormatException nfe) {
			System.out.println("could not parse user_id");
			return tickers;
		}
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
			st = conn.prepareStatement("SELECT ticker FROM favorites WHERE user_id = " + uID + " ORDER BY ticker");
			rs = st.executeQuery();
			while(rs.next()) {
				tickers.add(rs.getString("ticker"));
			}
		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in getFavorites.");
			System.out.println(e.getMessage());
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}
		return tickers;
	}
	
	public static int validate(String username, String password) {
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
			st = conn.prepareStatement("SELECT user_id, username, password FROM users");
			rs = st.executeQuery();
			String existingUsername = null;
			String existingPassword = null;
			while(rs.next()) {
				existingUsername = rs.getString("username");
				existingPassword = rs.getString("password");
				if(existingUsername.equals(username) && existingPassword.equals(password)){
					int uID = rs.getInt("user_id");
					return uID;
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in validate.");
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}
		return -1;
	}
	
	public static boolean validateGmail(String email) {
		
		boolean unique = true;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
			st = conn.prepareStatement("SELECT email FROM users");
			rs = st.executeQuery();
			String existingEmail = null;
			while(rs.next()) {
				existingEmail = rs.getString("email");
				if(existingEmail.equals(email)){
					unique = false;
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in checkUniqueness.");
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}
		return unique;
	}
	
	public static int loginGoogleUser(String email) {
		int uID = -1;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
			st = conn.prepareStatement("SELECT user_id, email FROM users WHERE google = TRUE");
			rs = st.executeQuery();
			String existingEmail = null;
			while(rs.next()) {
				existingEmail = rs.getString("email");
				if(existingEmail.equals(email)){
					uID = rs.getInt("user_id");
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in loginGoogleUser.");
			System.out.println(e.getMessage());
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}
		
		if(uID == -1) {
			return addGoogleUser(email);
		}
		else {
			return uID;
		}
	}
	
	public static int addGoogleUser(String email) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
			st = conn.prepareStatement("INSERT INTO users (email,username,cash,google) VALUES ('" + email + "','" + email + "'," + 50000+",TRUE)");
			st.executeUpdate();
		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in addGoogleUser.");
			System.out.println(e.getMessage());
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}
		return loginGoogleUser(email);
	}
	
	
	public static int addUser(String email, String username, String password)
	{
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		int userID = -1;
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
			st = conn.prepareStatement("INSERT INTO users (email,username,password,cash,google) VALUES ('" + email + "','" + username + "','" + password + "'," + 50000+",FALSE)");
			st.executeUpdate();
			rs = st.executeQuery("SELECT LAST_INSERT_ID()");
			rs.next();
			userID = rs.getInt(1);
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in addUser.");
			System.out.println(e.getMessage());
		}
		finally
		{
			closeConnection(conn, st, rs);
		}
		
		return userID;
	}
	
	public static boolean check_uniqueness(String email, String username) {
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		boolean isUnique = true;
		
		
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/salstocks_db?user=root&password=root");
			st = conn.prepareStatement("SELECT username, email FROM users");
			rs = st.executeQuery();
			String existingUsername = null;
			String existingEmail = null;
			while(rs.next()) {
				existingUsername = rs.getString("username");
				existingEmail = rs.getString("email");
				if(existingUsername.equals(username) || existingEmail.equals(email)){
					return false;
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception occured accessing database in checkUniqueness REAL.");
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Class not found exception occured");
		}
		finally
		{
			closeConnection(conn, st, rs);
		}
		return isUnique;
	}
	
	public static void closeConnection(Connection conn, PreparedStatement st, ResultSet rs) {
		try
		{
			if (rs != null)
			{
				rs.close();
			}
			if (st != null)
			{
				st.close();
			}
			if (conn != null)
			{
				conn.close();
			}
		}
		catch (SQLException e)
		{
			System.out.println("Error closing connection.");
		}
	}
}
