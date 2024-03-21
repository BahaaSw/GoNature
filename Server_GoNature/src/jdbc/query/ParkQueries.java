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

public class ParkQueries {

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
	
//	public DatabaseResponse isParkFullAtDate()
//	{
//		
//	}
//	
//	public DatabaseResponse updateIfParkFull()
//	{
//		
//	}
	
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
	
	/**
	 * @param order
	 * @param direction - if true means the customer is entering the park, false
	 *                  means customer is exiting the park
	 * @return
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