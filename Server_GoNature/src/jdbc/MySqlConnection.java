package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {

	private Connection connection = null;
	private static MySqlConnection instance = null;
	private static DBConnectionDetails dbDetails;

	/*
	 * * This method is trying to connect to mySQL database, using jdbc driver. This
	 * method is being called from server, and return it's connection.
	 * 
	 * @param db - class which contains the required information for database
	 * (hostname,username,password)
	 * 
	 * @return if connected successfully -> return the new Connection. else ->
	 * return null.
	 */
	/**
	 * private constructor
	 * 
	 * @throws SQLException           If got SQL error
	 * @throws ClassNotFoundException If failed to create jdbc driver
	 * @throws InstantiationException if failed to connect to the database
	 * @throws IllegalAccessException if failed to connect to the database
	 */
	private MySqlConnection() throws SQLException {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			System.out.println("Driver definition failed");
		}

		try {
			String url = "jdbc:mysql://127.0.0.1/" + dbDetails.getName() + "?serverTimezone=Asia/Jerusalem";
			connection = DriverManager.getConnection(url, dbDetails.getUsername(), dbDetails.getPassword());
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			connection = null;
			throw ex;
		}

	}

	/**
	 * This function returns an MysqlConnection instance
	 * 
	 * @return MysqlConnection object
	 * 
	 * @throws SQLException           If got SQL error
	 * @throws ClassNotFoundException If failed to create JDBC driver
	 * @throws InstantiationException if failed to connect to the database
	 * @throws IllegalAccessException if failed to connect to the database
	 */
	public static MySqlConnection getInstance() {
		if (instance == null) {
			try {
				instance = new MySqlConnection();
			} catch (SQLException e) {
				instance = null;
			}
		}
		return instance;
	}

	/**
	 * This function returns a connection to the DB
	 * 
	 * @return Connection object
	 */
	public Connection getConnection() {
		return this.connection;
	}

	public void closeConnection() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setDBConnectionDetails(DBConnectionDetails details) {
		dbDetails = details;
	}

}
