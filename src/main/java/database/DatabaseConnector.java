package database;

import java.sql.*;

public abstract class DatabaseConnector <T> {
	
	//TO REMOVE
	private long startTime;
	
	private String username;
	private String password;
	String connectionURL;
	
	public DatabaseConnector(String database, String username, String password, boolean wholeURL) {
		if (wholeURL)
			connectionURL = database;
		else
			connectionURL = "jdbc:mysql://mysql-abizitate-abi-zitate.a.aivencloud.com:16763/" + database + "?sslmode=require&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Atlantic/Azores&characterEncoding=utf8&useUnicode=true";
		this.username = username;
		this.password = password;
	}
	
	public DatabaseConnector() {
		this("abizitate", "avnadmin", "AVNS_YqUmaxCG8x1hj3bklPt", false);
	}
	
	public void runQuery(String query) throws SQLException {
		start();
		System.out.println(query.replace("\n", "").replace("\r", ""));
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try (Connection connection = DriverManager.getConnection(connectionURL, username, password); 
				PreparedStatement statement = connection.prepareStatement(query); 
				ResultSet result = statement.executeQuery()) {
			} 
			catch (SQLException e) { 
				throw e; 
			}
		} 
		catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} 
		stop();
	}
	
	/**
	 * 
	 * @Return either the row count for SQL Data Manipulation Language (DML) statements or 0 for SQL statements that return nothing or -1 if an error occured
	 * 
	 */
	public int runUpdate(String query) throws SQLException {
		start();
		System.out.println(query.replace("\n", "").replace("\r", ""));
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try (Connection connection = DriverManager.getConnection(connectionURL, username, password); 
				PreparedStatement statement = connection.prepareStatement(query); 
				)
			{
				stop();
				return statement.executeUpdate();
			} 
			catch (SQLException e) { 
				throw e; 
			}
		} 
		catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			return -1;
		} 
		
	}
	
	public T runQueryAndGet(String query) throws SQLException {
		start();
		System.out.println(query.replace("\n", "").replace("\r", ""));
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try (Connection connection = DriverManager.getConnection(connectionURL, username, password); 
				PreparedStatement statement = connection.prepareStatement(query); 
				ResultSet result = statement.executeQuery()) {	
				stop();
				return processResult(result);
			} 
			catch (SQLException e) { 
				throw e;
			}
		} 
		catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} 
		return null;
	}
	
	public <X> X runQueryAndGetSingleValue(String query, Class<X> type) throws SQLException {
		start();
		System.out.println(query.replace("\n", "").replace("\r", ""));
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try (Connection connection = DriverManager.getConnection(connectionURL, username, password); 
				PreparedStatement statement = connection.prepareStatement(query); 
				ResultSet result = statement.executeQuery()) {
				if (result.next() == false)
					return null;
				stop();
				return (X) result.getObject(1);
			} 
			catch (SQLException e) { 
				throw e;
			}
		} 
		catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} 
		return null;
	}
	
	
	protected abstract T processResult(ResultSet result);
	
	private void start() {
		startTime = System.currentTimeMillis();
	}
	
	private void stop() {
		long endTime = System.currentTimeMillis();
		System.out.println("Previous query took " + (endTime-startTime) + " ms.");
	}
	
	public static void main(String[] args) {
		try {
			Table t = new TableReceiver().runQueryAndGet("SELECT * From user WHERE user_username =  'jonric'");
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
