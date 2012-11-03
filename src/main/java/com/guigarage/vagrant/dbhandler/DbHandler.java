package com.guigarage.vagrant.dbhandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

/**
 * @author hendrikebbers Simple DbHandler that can connect to a mySql database
 *         and create some dummy data. The class is used in
 *         {@link DbHandlerTest} as an example for UnitTests with
 *         Vagrant-Binding
 */
public class DbHandler {

	private String jdbcString;

	private Connection connection;

	/**
	 * Creates a new DbHandler for a mysql database
	 * @param server host/ip of the server where mysql is running
	 * @param db mysql database name
	 * @param user username
	 * @param password password for the jdbc connection
	 * @throws ClassNotFoundException if mysql jdbc driver not in classpath
	 */
	public DbHandler(String server, String db, String user, String password)
			throws ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		jdbcString = "jdbc:mysql://" + server + "/" + db + "?" + "user=" + user
				+ "&password=" + password;
	}

	/**
	 * connect to the database
	 * @throws SQLException
	 */
	public void connect() throws SQLException {
		connection = DriverManager.getConnection(jdbcString);
	}

	/**
	 * creates the table "mytable" on the database
	 * @throws SQLException
	 */
	public void createMyTable() throws SQLException {
		Statement statement = connection.createStatement();
		String table = "CREATE TABLE mytable (data_entry  VARCHAR(254))";
		statement.executeUpdate(table);
		statement.close();
	}

	/**
	 * inserts a new row with dummy data into the "mytable" table
	 * @throws SQLException
	 */
	public void insertRow() throws SQLException {
		Statement statement = connection.createStatement();
		statement.executeUpdate("INSERT INTO mytable VALUES(\""
				+ UUID.randomUUID().toString() + "\")");
		statement.close();
	}

	/**
	 * @return the actual rowcount in the "mytable" table
	 * @throws SQLException
	 */
	public int getRowCount() throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement
				.executeQuery("SELECT COUNT(*) FROM mytable");
		resultSet.next();
		int ret = resultSet.getInt(1);
		statement.close();
		return ret;
	}

	/**
	 * closes the connection to the database
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		connection.close();
		connection = null;
	}
}
