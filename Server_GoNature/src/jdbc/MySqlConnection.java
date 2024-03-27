package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Manages the connection to a MySQL database using JDBC. This class implements the Singleton pattern
 * to ensure that only one connection instance is used throughout the application. It provides methods
 * to connect to the database, get the instance of the connection, and close the connection when necessary.
 * Authors: Nadav Reubens, Gal Bitton, Tamer Amer, Rabea Lahham, Bahaldeen Swied, Ron Sisso
 */

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
	
    /**
     * Closes the current database connection and nullifies the Singleton instance.
     * This method should be called to release the database resources explicitly when they are no longer needed.
     */
	public void closeConnection() {
		try {
			this.connection.close();
			instance=null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    /**
     * Sets the database connection details used by MySqlConnection to establish the database connection.
     * This method should be called before obtaining the MySqlConnection instance for the first time.
     *
     * @param details The DBConnectionDetails object containing the database name, username, and password.
     */
	public static void setDBConnectionDetails(DBConnectionDetails details) {
		dbDetails = details;
	}

}
