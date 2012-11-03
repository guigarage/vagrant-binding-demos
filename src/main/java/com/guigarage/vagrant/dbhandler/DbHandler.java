package com.guigarage.vagrant.dbhandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class DbHandler {

	private String jdbcString;
	
	Connection connection;
	
	public DbHandler(String server, String db, String user, String password) throws ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		jdbcString = "jdbc:mysql://" + server + "/" + db + "?"
				+ "user=" + user + "&password=" + password;
	}
	
	public void connect() throws SQLException {
		connection = DriverManager
				.getConnection(jdbcString);
	}
	
	public void createMyTable() throws SQLException {
		Statement statement = connection.createStatement();
		String table = "CREATE TABLE mytable (data_entry  VARCHAR(254))";
		statement.executeUpdate(table);
		statement.close();
	}
	
	public void insertRow() throws SQLException {
		Statement statement = connection.createStatement();
		statement.executeUpdate("INSERT INTO mytable VALUES(\"" + UUID.randomUUID().toString() + "\")");
		statement.close();
	}
	
	public int getRowCount() throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement
				.executeQuery("SELECT COUNT(*) FROM mytable");
		resultSet.next();
		int ret =  resultSet.getInt(1);
		statement.close();
		return ret;
	}
	
	public void close() throws SQLException {
		connection.close();
		connection = null;
	}
}
