package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import gui.controller.ServerScreenController;
import javafx.application.Platform;

/**
 * This class manages the connection to a MySQL database using JDBC.
 * It provides methods to establish a connection, retrieve the connection object,
 * and close the connection.
 * TamerAmer, GalBitton, RabeaLahham, BahaldeenSwied, RonSisso, NadavReubens.
 */
public class MySqlConnection {

	private Connection connection = null;
	private static MySqlConnection instance = null;
	private static DBConnectionDetails dbDetails;
	private static ServerScreenController controller;
	
	
	private MySqlConnection() throws SQLException {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			Platform.runLater((()->controller.printToLogConsole("Driver definition succeed")));
		} catch (Exception ex) {
			Platform.runLater((()->controller.printToLogConsole("Driver definition failed")));
		}

		try {
			String url = "jdbc:mysql://127.0.0.1/" + dbDetails.getName() + "?serverTimezone=Asia/Jerusalem&allowLoadLocalInfile=true";
			connection = DriverManager.getConnection(url, dbDetails.getUsername(), dbDetails.getPassword());
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

		} catch (SQLException ex) {
			Platform.runLater(()->{
				controller.printToLogConsole("SQLException: " + ex.getMessage());
				controller.printToLogConsole("SQLState: " + ex.getSQLState());
				controller.printToLogConsole("VendorError: " + ex.getErrorCode());
			});
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
     * Returns an instance of MySqlConnection, allowing the setting of the ServerScreenController.
     * 
     * @param serverController The ServerScreenController instance to set
     * @return MySqlConnection object
     * @throws SQLException           If an SQL error occurs during connection
     * @throws ClassNotFoundException If the JDBC driver class cannot be found
     * @throws InstantiationException If an instance of the JDBC driver cannot be created
     * @throws IllegalAccessException If access to the JDBC driver is denied
     */
	public static MySqlConnection getInstance(ServerScreenController serverController) {
		if (instance == null) {
			try {
				controller=serverController;
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
     * Closes the connection to the database.
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
     * Sets the database connection details.
     * 
     * @param details The database connection details to set
     */
	public static void setDBConnectionDetails(DBConnectionDetails details) {
		dbDetails = details;
	}

}
