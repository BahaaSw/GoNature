package jdbc.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import gui.controller.ServerScreenController;
import logic.User;
import utils.enums.ParkNameEnum;
import utils.enums.UserTypeEnum;


/**
 * 
 * @Author NadavReubens
 * @Author Gal Bitton
 * @Author Tamer Amer
 * @Author Rabea Lahham
 * @Author Bahaldeen Swied
 * @Author Ron Sisso
 * @version 1.0.0 
 */

/**
 * The DB Controller class is the way our application "talk" with the database.
 * This class use "mysql-connector-java-8.0.13".
 */
public class QueryControl {
	
	private ServerScreenController serverController;
	
	public static OrderQueries orderQueries = new OrderQueries();
	public static CustomerQueries customerQueries = new CustomerQueries();
	public static EmployeeQueries employeeQueries = new EmployeeQueries();
	public static ParkQueries parkQueries = new ParkQueries();
	public static ReportsQueries reportsQueries = new ReportsQueries();
	public static RequestQueries requestsQueries = new RequestQueries();
	public static NotificationQueries notificationQueries = new NotificationQueries();
	
	
	public QueryControl(ServerScreenController serverController) {
		this.serverController=serverController;
	}
	
}
