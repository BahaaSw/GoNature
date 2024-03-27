package jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import jdbc.DatabaseResponse;
import jdbc.MySqlConnection;
import jdbc.QueryType;
import logic.Order;
import logic.Park;
import logic.Request;
import utils.enums.OrderStatusEnum;
import utils.enums.ParkNameEnum;

/**
 * Handles database queries related to parks, including retrieving park details by ID or name,
 * updating park parameters, and managing park fullness and capacity.
 * Authors: Nadav Reubens, Gal Bitton, Tamer Amer, Rabea Lahham, Bahaldeen Swied, Ron Sisso
 * @version 1.0.0
 */

public class ParkQueries {
	
    /**
     * Retrieves a park's details by its ID and updates the passed Park object.
     *
     * @param park A Park object with the parkId set. The method fills this object with the retrieved park details.
     * @return true if the park details were successfully retrieved and the Park object was updated; false otherwise.
     */

	public boolean getParkById(Park park) {
		
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM parks WHERE ParkId = ?");
			stmt.setInt(1, park.getParkId());
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return false;
			}
			
			park.setParkId(rs.getInt(1));
			park.setParkName(ParkNameEnum.fromParkId(rs.getInt(1)));
			park.setCurrentMaxCapacity(rs.getInt(3));
			park.setCurrentEstimatedStayTime(rs.getInt(4));
			park.setCurrentEstimatedReservedSpots(rs.getInt(5));
			park.setCurrentInPark(rs.getInt(6));
			park.setPrice(rs.getInt(7));
			
			return true;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for park failed");
			return false;
		}
	}
	
	//NOTICE : NOT USED THAT QUERY!!
    /**
     * Retrieves a park's details by its name. This method is not used and may be deprecated.
     *
     * @param park A Park object with the parkName set. The method fills this object with the retrieved park details.
     * @return A DatabaseResponse indicating the outcome of the operation, such as success or failure.
     */
	public DatabaseResponse getParkByName(Park park) {
		
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM parks WHERE ParkName = ?");
			stmt.setInt(1, park.getParkName().getParkId());
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return DatabaseResponse.Such_Park_Does_Not_Exists;
			}
			
			park.setParkId(rs.getInt(1));
			park.setParkName(ParkNameEnum.fromParkId(rs.getInt(2)));
			park.setCurrentMaxCapacity(rs.getInt(3));
			park.setCurrentEstimatedStayTime(rs.getInt(4));
			park.setCurrentEstimatedReservedSpots(rs.getInt(5));
			park.setCurrentInPark(rs.getInt(6));
			park.setPrice(rs.getInt(7));
			
			return DatabaseResponse.Park_Found_Successfully;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for park failed");
			return DatabaseResponse.Failed;
		}
	}
	
	//NOTICE : NOT USED THAT QUERY!!
    /**
     * Retrieves a list of all park names. This method is not used and may be deprecated.
     *
     * @param parkList An ArrayList of ParkNameEnum to be filled with the names of all parks.
     * @return A DatabaseResponse indicating the outcome of the operation, such as success or park list creation.
     */
	public DatabaseResponse getParksNames(ArrayList<ParkNameEnum> parkList) {
		
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT ParkName FROM parks");
			
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return DatabaseResponse.Park_Table_Is_Empty;
			}
			
			while(rs.next())
			{
				parkList.add(ParkNameEnum.fromParkId(rs.getInt(2)));
			}
			
			return DatabaseResponse.Park_List_Names_Is_Created;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for park failed");
			return DatabaseResponse.Failed;
		}
	}
	
	
	//NOTICE : NOT USED THAT QUERY!!
    /**
     * Retrieves the price for a given park. This method is not used and may be deprecated.
     *
     * @param park A Park object for which the price will be retrieved. The method updates this object with the price.
     * @return A DatabaseResponse indicating the outcome of the operation, such as success or failure.
     */
	public DatabaseResponse returnParkPrice(Park park)
	{
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT PRICE FROM parks WHERE ParkName = ?");
			stmt.setInt(1, park.getParkName().getParkId());
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return DatabaseResponse.Such_Park_Does_Not_Exists;
			}
			
			park.setParkId(rs.getInt(1));
			park.setParkName(ParkNameEnum.fromParkId(rs.getInt(2)));
			park.setCurrentMaxCapacity(rs.getInt(3));
			park.setCurrentEstimatedStayTime(rs.getInt(4));
			park.setCurrentEstimatedReservedSpots(rs.getInt(5));
			park.setCurrentInPark(rs.getInt(6));
			park.setPrice(rs.getInt(7));
			
			return DatabaseResponse.Park_Price_Returned_Successfully;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for park failed");
			return DatabaseResponse.Failed;
		}
	}
	
    /**
     * Updates a specific parameter in the park's record based on a Request object.
     *
     * @param request A Request object containing the new value for a parameter, the park ID, and the parameter to update.
     * @return A DatabaseResponse indicating the outcome of the update operation.
     */
	public DatabaseResponse InsertNewValueInRequestedPark(Request request)
	{
		try {

			String columnName = request.getRequestType().getValue(); // to get the field we want to update

			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE parks SET " + columnName + " = ? WHERE ParkId = ?");

			stmt.setInt(1, request.getNewValue());
			stmt.setInt(2, request.getParkId());
			int rs = stmt.executeUpdate();

			// if the query ran successfully, but returned as empty table.
			if (rs==0) {
				return DatabaseResponse.Such_Park_Does_Not_Exists;
			}
			return DatabaseResponse.Park_Parameter_Updated_Successfully;

		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for park failed");
			return DatabaseResponse.Failed;
		}
	}
	
	//NOTICE : NOT USED THAT QUERY!!
    /**
     * Retrieves the current capacity and number of visitors in the park.
     * This method is not used and may be deprecated.
     *
     * @param parkId The ID of the park.
     * @return An array of two integers: the first is the maximum capacity, and the second is the current number of visitors in the park.
     */
	public int[] returnCapacityCurrentInParkForPark(int parkId) {
		int[] values = new int[2];

		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement selectStmt = con
					.prepareStatement("SELECT MaxCapacity, CurrentInPark From parks WHERE ParkId = ?; ");
			selectStmt.setInt(1, parkId);
			ResultSet rs = selectStmt.executeQuery();

			if (!rs.next()) {
				return null;
			}
			values[0] = rs.getInt("MaxCapacity");
			values[1] = rs.getInt("CurrentInPark");
			return values;

		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}

	}
	
    /**
     * Updates the park full dates table to mark a park as full on a specific date.
     *
     * @param operation The type of operation (Insert or Update) to perform on the park full dates table.
     * @param date The date on which the park was full.
     * @param parkName The name of the park.
     * @return true if the update was successful; false otherwise.
     */
	public boolean updateParkFullDateTable(QueryType operation, LocalDate date, String parkName) {

		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			if (operation.name().equals("Insert")) {
				PreparedStatement insertStmt = con
						.prepareStatement("INSERT INTO parkfulldates (Date, ?) VALUES (?, 1);");
				insertStmt.setString(1, parkName);
				insertStmt.setString(2, date.toString());
				int insertRS = insertStmt.executeUpdate();
				if (insertRS == 0) {
					return false;
				}
				return true;
			} else if (operation.name().equals("Update")) {
				PreparedStatement updateStmt = con.prepareStatement("UPDATE parkfulldates SET ? = 1 WHERE (Date = ?);");
				updateStmt.setString(1, parkName);
				updateStmt.setString(2, date.toString());
				int updateRS = updateStmt.executeUpdate();
				if (updateRS == 0) {
					return false;
				}
			}
			return true;

		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	//NOTICE : NOT USED THAT QUERY!!
    /**
     * Checks if a park is full on a specific date and updates the park full dates table accordingly.
     * This method is not used and may be deprecated.
     *
     * @param parkId The ID of the park.
     * @param date The date to check for fullness.
     * @return A DatabaseResponse indicating the outcome of the check, such as the park being full or not full.
     */
	public DatabaseResponse checkParkIsFull(int parkId, LocalDate date) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement selectStmt = con
					.prepareStatement("SELECT ParkName, MaxCapacity, CurrentInPark From parks WHERE ParkId = ?;");
			selectStmt.setInt(1, parkId);
			ResultSet rs = selectStmt.executeQuery();

			if (!rs.next()) {
				return DatabaseResponse.Such_Park_Does_Not_Exists;
			}

			rs.previous();
			int maxCapacity = rs.getInt("MaxCapacity");
			int currentInPark = rs.getInt("CurrentInPark");
			selectStmt.close();
			if (maxCapacity == currentInPark) {
				PreparedStatement updateStmt = con.prepareStatement("SELECT * FROM parkfulldates WHERE Date = ?;");
				updateStmt.setString(1, date.toString());
				ResultSet updateRS = updateStmt.executeQuery();
				if (!updateRS.first()) {
					if (!updateParkFullDateTable(QueryType.Insert, date, rs.getString("ParkName"))) {
						return DatabaseResponse.Park_Reached_Full_Capacity_Updated_Failed;
					}
				}
				if (!updateParkFullDateTable(QueryType.Update, date, rs.getString("ParkName"))) {
					return DatabaseResponse.Park_Reached_Full_Capacity_Updated_Failed;
				}
				return DatabaseResponse.Park_Is_Full;
			}
			return DatabaseResponse.Park_Is_Not_Full;

		} catch (SQLException ex) {
			ex.printStackTrace();
			return DatabaseResponse.Failed;
		}
	}
	
	
	//NOTICE : NOT USED THAT QUERY!!
    /**
     * Updates the number of visitors currently in the park based on an order's entry or exit. 
     * If the direction is true, it indicates that the customers associated with the order are entering the park, 
     * and the current in-park visitor count is incremented by the order's visitor count. 
     * If false, it indicates that the customers are exiting, and the count is decremented.
     *
     * Additionally, this method updates the order status to either In_Park (if entering) or Completed (if exiting).
     *
     * @param order The order object containing details such as the park ID and the number of visitors.
     * @param direction A boolean value indicating the direction of the update: true for entry, false for exit.
     * @return A DatabaseResponse enum indicating the outcome of the operation. It can denote successful update, 
     *         failure due to SQL issues, or other specific reasons like the park does not exist.
     */
	public DatabaseResponse updateCurrentInParkValue(Order order, boolean direction) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement selectStmt = con.prepareStatement("SELECT CurrentInPark FROM parks WHERE (ParkId = ?);");
			selectStmt.setInt(1, order.getParkName().getParkId());
			ResultSet rs = selectStmt.executeQuery();

			if (!rs.next()) {
				return DatabaseResponse.Such_Park_Does_Not_Exists;
			}

			rs.previous();
			int currentInParkUpdated = rs.getInt("CurrentInPark");
			if (direction) {
				currentInParkUpdated += order.getNumberOfVisitors();
				QueryControl.orderQueries.updateOrderStatus(order, OrderStatusEnum.In_Park); // update the order to be in park
			} else {
				currentInParkUpdated -= order.getNumberOfVisitors();
				QueryControl.orderQueries.updateOrderStatus(order, OrderStatusEnum.Completed); // update the order to be completed
			}
			selectStmt.close();
			rs.close();
			PreparedStatement updateStmt = con.prepareStatement("UPDATE parks SET CurrentInPark = ? WHERE ParkId = ?;");
			updateStmt.setInt(1, currentInParkUpdated);
			int updateRS = updateStmt.executeUpdate();

			if (updateRS == 0) {
				return DatabaseResponse.Current_In_Park_Update_Failed;
			}
			return DatabaseResponse.Current_In_Park_Updated_Successfully;

		} catch (SQLException ex) {
			ex.printStackTrace();
			return DatabaseResponse.Failed;
		}
	}
	

}