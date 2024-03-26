package jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import jdbc.DatabaseResponse;
import jdbc.MySqlConnection;
import logic.Order;
import utils.enums.OrderStatusEnum;
import utils.enums.OrderTypeEnum;
import utils.enums.ParkNameEnum;

public class OccasionalQueries {
	/**
	 * Gets an occasional order and checks if it's in DB
	 * 
	 * @param order - the order to search for, must include the orderId
	 * @return on Success: returns Order_Found_Successfully
	 *         on Failure: returns Such_Order_Does_Not_Exists
	 *         exception: returns Exception_Was_Thrown
	 */
	
	//NOTICE : NOT USED THAT QUERY!!
	public DatabaseResponse FetchOccasioanlOrderById(Order order) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM occasionalvisits WHERE OrderId = ?;");
			stmt.setInt(1, order.getOrderId());
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return DatabaseResponse.Such_Order_Does_Not_Exists;
			}

			order.setOrderId(rs.getInt(1));
			order.setParkName(ParkNameEnum.fromParkId(rs.getInt(2)));
			order.setEnterDate(rs.getTimestamp(3).toLocalDateTime());
			order.setExitDate(rs.getTimestamp(4).toLocalDateTime());
			order.setStatus(OrderStatusEnum.fromString(rs.getString(5)));
			order.setEmail(rs.getString(6));
			order.setTelephoneNumber(rs.getString(7));
			order.setFirstName(rs.getString(8));
			order.setLastName(rs.getString(9));
			order.setOrderType(OrderTypeEnum.fromString(rs.getString(10)));
			order.setNumberOfVisitors(rs.getInt(11));
			order.setPrice(rs.getDouble(12));

			return DatabaseResponse.Order_Found_Successfully;

		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return DatabaseResponse.Exception_Was_Thrown;
		}
	}
	
	/**
	 *
	 * @return returns the amount of occasional visits in the system
	 */
	public int ReturnTotalOccasionalVisits() {
		int occasionalVisits = 0;
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) AS visitsCount FROM occasionalvisits");
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return occasionalVisits;
			}

			occasionalVisits = rs.getInt(1);
			return occasionalVisits;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return occasionalVisits;
	}
	
	/**
	 *
	 * @return returns the amount of occasional visits in park at the moment
	 */
	
	//NOTICE : NOT USED THAT QUERY!!
	public int ReturnTotalOccasionalVisitsInPark() {
		int occasionalInPark = 0;
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"SELECT COUNT(*) AS OccasionalInPark FROM occasionalvisits WHERE OrderStatus = 'In Park'");
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return occasionalInPark;
			}

			occasionalInPark = rs.getInt(1);
			return occasionalInPark;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return occasionalInPark;
	}
	
	
	/**
	 * Gets an occasional order and status to update, updates the order with the requested status
	 * 
	 * @param order - the order to search for, must include the orderId
	 * @param status - status to update order with
	 * @return on Success: returns Order_ExitDate_Updated
	 *         on Failure: returns Failed
	 *         exception: returns Exception_Was_Thrown
	 */
	public boolean UpdateOccasionalOrderStatus(Order order, OrderStatusEnum status) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con
					.prepareStatement("Update occasionalvisits SET OrderStatus = ? WHERE OrderId = ?");
			stmt.setString(1, status.toString());
			stmt.setInt(2, order.getOrderId());
			int rs = stmt.executeUpdate();

			// if the query ran successfully, but returned as empty table.
			if (rs == 0) {
				return false;
			}
			
			return true;

		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return false;
		}
	}
	
	
	/**
	 * Gets an occasional order and exit date time, updates the order exit date time accordingly
	 * 
	 * @param order - the order to search for, must include the orderId
	 * @param exitDate - the date time to update
	 * @return on Success: returns Order_ExitDate_Updated
	 *         on Failure: returns Failed
	 *         exception: returns Exception_Was_Thrown
	 */
	
	//NOTICE : NOT USED THAT QUERY!!
	public DatabaseResponse UpdateOrderExitDate(Order order, LocalDateTime exitDate) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con
					.prepareStatement("UPDATE occasionalvisits SET ExitDate = ? WHERE (OrderId = ?)");
			stmt.setString(1, exitDate.toString());
			stmt.setInt(2, order.getOrderId());
			int rs = stmt.executeUpdate();
			// if the query ran successfully, but returned as empty table.
			if (rs == 0) {
				return DatabaseResponse.Failed;
			}
			return DatabaseResponse.Order_ExitDate_Updated;

		} catch (SQLException ex) {
			return DatabaseResponse.Exception_Was_Thrown;
		}
	}
	
	public DatabaseResponse insertOccasionalOrder(Order order) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"INSERT INTO occasionalvisits (OrderId, ParkId, EnterDate, ExitDate, OrderStatus, Email, Phone, FirstName, LastName, OrderType, Amount, Price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			int newOrderId = ReturnTotalOccasionalVisits() + 1;
			order.setOrderId(newOrderId);

			stmt.setInt(1, newOrderId);
			stmt.setInt(2, order.getParkName().getParkId());
			stmt.setString(3, order.getEnterDate().toString());
			stmt.setString(4, order.getExitDate().toString());
			stmt.setString(5, order.getStatus().toString());
			stmt.setString(6, order.getEmail());
			stmt.setString(7, order.getTelephoneNumber());
			stmt.setString(8, order.getFirstName());
			stmt.setString(9, order.getLastName());
			stmt.setString(10, order.getOrderType().toString());
			stmt.setInt(11, order.getNumberOfVisitors());
			stmt.setDouble(12, order.getPrice());

			int rs = stmt.executeUpdate();

			// if the query ran successfully, but returned as empty table.
			if (rs == 0) {
				return DatabaseResponse.Failed;
			}
			return DatabaseResponse.Order_Added_Into_Table;

		} catch (SQLException ex) {
			return DatabaseResponse.Exception_Was_Thrown;
		}
	}

	//NOTICE : NOT USED THAT QUERY!!
	public ArrayList<Order> getAllOccasionalOrdersInPark(int parkId) {
		LocalDate today = LocalDateTime.now().toLocalDate();
		ArrayList<Order> foundOrders = new ArrayList<>();
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"SELECT * FROM occasionalvisits WHERE ParkId = ? AND Date(EnterDate) = ?  AND OrderStatus = 'In Park';");
			stmt.setInt(1, parkId);
			stmt.setString(2, today.toString());
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return null;
			}
			rs.first();
			while (rs.next()) {
				Order order = new Order(rs.getInt(1), ParkNameEnum.fromParkId(rs.getInt(2)),
						LocalDateTime.parse(rs.getString(3)), LocalDateTime.parse(rs.getString(4)),
						OrderStatusEnum.fromString(rs.getString(5)), rs.getString(6), rs.getString(7), rs.getString(8),
						rs.getString(9), OrderTypeEnum.fromString(rs.getString(10)), rs.getInt(11), rs.getDouble(12));
				foundOrders.add(order);
			}
			return foundOrders;

		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}


}
