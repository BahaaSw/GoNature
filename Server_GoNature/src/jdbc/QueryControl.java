package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import gui.controller.ServerGuiController;
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
	
//	public static Object runQuery(Connection conn,QueryType action) {
//		
//		switch(action) {
//		case Insert:
//			return null;
//		case Select:
//			return null;
//		case Update:
//			return null;
//		default:
//			return null;
//		}
//	}
//	
	public static DBReturnOptions updateUserInDB(User user, ServerGuiController serverController) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt =con.prepareStatement("UPDATE accounts SET password = ?, Email = ?, phone = ?, acctype = ? WHERE username = ?");
			stmt.setString(1, user.getPassword());
			stmt.setString(2, user.getEmailAddress());
			stmt.setString(3, user.getPhoneNumber());
			stmt.setString(4, user.getUserType().name());
			stmt.setString(5, user.getUsername());
			
			int rs = stmt.executeUpdate();
			
			if(rs==0) {
				return DBReturnOptions.User_Does_Exists;
			}
			
			return DBReturnOptions.Success;
		}catch(SQLException ex) {
			serverController.printToLogConsole("Query for search for user failed");
			return DBReturnOptions.Exception_Was_Thrown;
		}
		// any other exception occurred
		catch(Exception e) {
			serverController.printToLogConsole(e.getMessage());
			return DBReturnOptions.Exception_Was_Thrown;
		}
	}
	
//	public static DBReturnOptions searchForUser(User user,ServerGuiController serverController) {
//
//	try {
//		Connection con = MySqlConnection.getInstance().getConnection();
//		PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE username = ?");
//		// create the requested query
//		stmt.setString(1, user.getUsername());
//		ResultSet rs = stmt.executeQuery();
//		
//		// if the query run successfully, but returned as empty table.
//		if(!rs.next()) {
//			return DBReturnOptions.User_Not_Exists;
//		}
//		
//		if(!user.getPassword().equals(rs.getString(3))) {
//			return DBReturnOptions.Password_Incorrect;
//		}
//		
////		int isConnected= rs.getInt(6);
////		if(isConnected==1) {
////			user.setAsLoggedIn();
////			return DBReturnOptions.UserAlreadyLoggedIn;
////		}
////		else
////			user.setAsLoggedOut();
//		
//		user.setUserId(rs.getString(1));
//		user.setPassword(rs.getString(3));
//		user.setFirstName(rs.getString(4));
//		user.setLastName(rs.getString(5));
//		String userType = rs.getString(6);
//		String parkName = rs.getString(7);
//		user.setEmailAddress(rs.getString(8));
//		user.setPhoneNumber(rs.getString(9));
//
//		// update user type
//		UserTypeEnum type = UserTypeEnum.fromString(userType);
//		ParkNameEnum park = ParkNameEnum.fromString(parkName);
//		user.setUserType(type);
//		user.setRelatedPark(park);
//		// create new Order instance and initialize it with relevant data.
//		
//		return DBReturnOptions.Success;
//		
//	}catch(SQLException ex) {
//		serverController.printToLogConsole("Query for search for user failed");
//		return DBReturnOptions.Exception_Was_Thrown;
//	}
//	// any other exception occurred
//	catch(Exception e) {
//		serverController.printToLogConsole(e.getMessage());
//		return DBReturnOptions.Exception_Was_Thrown;
//	}
//}
//
//	public static DBReturnOptions changeIsConnectedFlagForUser(Connection conn, User user, boolean isConnected,ServerGuiController serverController) {
//		try {
//			PreparedStatement stmt = conn.prepareStatement("UPDATE accounts SET isconnected = ? WHERE username = ?;");
//			// create the requested query
//			String flag = (isConnected)?"1":"0";
//			stmt.setString(1, flag);
//			stmt.setString(2, user.getUsername());
//			int rs = stmt.executeUpdate();
//			
//			// if the query run successfully, but returned as empty table.
//			if(rs==0) {
//				return DBReturnOptions.Failed;
//			}
//			else {
//				if(isConnected) {
//					user.setAsLoggedIn();
//					return DBReturnOptions.UserChangedToConnected;
//				}
//				else {
//					user.setAsLoggedOut();
//					return DBReturnOptions.UserChangedToDisconnected;
//				}
//			}
//			
//		}catch(SQLException ex) {
//			serverController.printToLogConsole("Query for update user connection failed");
//			return DBReturnOptions.ExceptionWasThrown;
//		}
//		// any other exception occurred
//		catch(Exception e) {
//			serverController.printToLogConsole(e.getMessage());
//			return DBReturnOptions.ExceptionWasThrown;
//		}
//	}
//	
//	public static DBReturnOptions addNewUserToDatabase(Connection conn, User user, ServerGuiController serverController) {
//		try {
//			String values = String.format("('%s','%s','%s','%s','%s',%d)",user.getUsername(),user.getPassword(),user.getEmailAddress(),user.getPhoneNumber()
//					,user.getUserType().toString(),user.getIsLoggedIn());
//			PreparedStatement stmt = conn.prepareStatement("INSERT INTO accounts (username,password,Email,phone,acctype,isconnected) Values "+ values);
//			int rs = stmt.executeUpdate();
//			
//			if(rs==0) {
//				return DBReturnOptions.Failed;
//			}
//			else 
//				if(user.getUserType()==UserTypeEnum.Guide) {
//					return DBReturnOptions.GuideNotApproveYet;
//				}
//				else if(user.getUserType()==UserTypeEnum.Visitor) {
//					return DBReturnOptions.Success;
//				}
//			return DBReturnOptions.Failed;
//			
//		}catch(SQLException ex) {
//			serverController.printToLogConsole("Query for update user connection failed");
//			return DBReturnOptions.ExceptionWasThrown;
//		}
//		// any other exception occurred
//		catch(Exception e) {
//			serverController.printToLogConsole(e.getMessage());
//			return DBReturnOptions.ExceptionWasThrown;
//		}
//		
//	}
//	
//	/**
//	 * This method is executing query to database in order to get the requested Order. 
//	 * @param conn - The Connection instance with the database.
//	 * @param id - The required order ID.
//	 * @param serverController - The server controller -> in order to print to log screen in server gui.
//	 * @return if found order in database -> return the requested Order instance.
//	 *         else -> return null.
//	 */
//	public static Order searchOrder(Connection conn, Integer id, ServerGuiController serverController) {
//		Statement stmt;
//		try {
//			// create the requested query
//			stmt=conn.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT * From Orders WHERE order_number = "+id.toString()+";");
//			
//			// if the query run successfully, but returned as empty table.
//			if(!rs.next()) {
//				return null;
//			}
//			
//			// create new Order instance and initialize it with relevant data.
//			Order retOrder = new Order();
//			retOrder.setParkName(rs.getString(1));
//			retOrder.setOrderNumber(Integer.parseInt(rs.getString(2)));
//			retOrder.setTimeOfVisit(rs.getString(3));
//			retOrder.setNumberOfVisitors(Integer.parseInt(rs.getString(4)));
//			retOrder.setTelephoneNumber(rs.getString(5));
//			retOrder.setEmail(rs.getString(6));
//			
//			return retOrder;
//			
//		}catch(SQLException ex) {
//			serverController.printToLogConsole("Query for search for order failed");
//			return null;
//		}
//		// any other exception occurred
//		catch(Exception e) {
//			serverController.printToLogConsole(e.getMessage());
//			return null;
//		}
//		
//	}
//	
//	/**
//	 * This method is trying to update the old order in Database with new order details.
//	 * @param conn - The Connection instance with the database.
//	 * @param orderNumber - The old order ID.
//	 * @param parkName - The new park name. (can be either old).
// 	 * @param phoneNumber - The new phone number. (can be either old).
//	 * @param serverController - The server controller -> in order to print to log screen in server gui.
//	 * @return if updated successfully -> return true.
//	 *         else -> return false.
//	 */
//	public static boolean updateOrder(Connection conn,Integer orderNumber, String parkName, String phoneNumber, ServerGuiController serverController) {
//		Statement stmt;
//		try {
//			stmt = conn.createStatement();
//			stmt.executeUpdate(String.format("UPDATE Orders SET park_name = '%s', phone_number = '%s' WHERE order_number = %d;",
//					parkName,phoneNumber,orderNumber));
//			return true;
//		}catch(SQLException ex) {
//			serverController.printToLogConsole("Query for update order failed");
//			ex.printStackTrace();
//			return false;
//		}
//	}
	
	
}
