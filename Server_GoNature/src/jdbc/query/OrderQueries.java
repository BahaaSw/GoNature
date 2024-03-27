package jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import jdbc.DatabaseResponse;
import jdbc.MySqlConnection;
import logic.Order;
import logic.Park;
import utils.enums.OrderStatusEnum;
import utils.enums.OrderTypeEnum;
import utils.enums.ParkNameEnum;
import utils.enums.UserTypeEnum;

/**
 * Facilitates all database interactions related to orders, including fetching, updating, and deleting orders,
 * checking available spots in parks, and handling notifications for waiting list orders.
 * Authors: Nadav Reubens, Gal Bitton, Tamer Amer, Rabea Lahham, Bahaldeen Swied, Ron Sisso
 */
public class OrderQueries {

	private ParkQueries parkQueries = new ParkQueries();
	
	public OrderQueries() {
	}

	/**
	 * Gets an order and checks if it's in DB
	 * 
	 * @param order - the order to search for. *already initialized*
	 * @return on Success: returns Order_Found_Successfully
	 *         on Failure: returns Such_Order_Does_Not_Exists
	 *         exception: returns Exception_Was_Thrown
	 */
	public DatabaseResponse fetchOrderByOrderID(Order order) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con
					.prepareStatement("SELECT * FROM preorders WHERE orderId = ? AND OrderStatus!='Cancelled' AND OrderStatus!='Completed' AND OrderStatus!='Time Passed'");
			stmt.setInt(1, order.getOrderId());
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return DatabaseResponse.Such_Order_Does_Not_Exists;
			}

			order.setOrderId(rs.getInt(1));
			order.setParkName(ParkNameEnum.fromParkId(rs.getInt(2)));
			order.setUserId(String.format("%d", rs.getInt(3)));
			order.setOwnerType(UserTypeEnum.fromString(rs.getString(4)));
			order.setEnterDate(rs.getTimestamp(5).toLocalDateTime());
			order.setExitDate(rs.getTimestamp(6).toLocalDateTime());
			order.setPaid(rs.getBoolean(7));
			order.setStatus(OrderStatusEnum.fromString(rs.getString(8)));
			order.setEmail(rs.getString(9));
			order.setTelephoneNumber(rs.getString(10));
			order.setFirstName(rs.getString(11));
			order.setLastName(rs.getString(12));
			order.setOrderType(OrderTypeEnum.fromString(rs.getString(13)));
			order.setNumberOfVisitors(rs.getInt(14));
			order.setPrice(rs.getDouble(15));

			return DatabaseResponse.Order_Found_Successfully;

		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return DatabaseResponse.Exception_Was_Thrown;
		}
	}

	/**
	 * gets an order and checks if the current date & time is available for a
	 * specific park
	 * 
	 * @param order - contains the order EnterDate and parkId
	 * @return The amount of visitors that are currently in the park, returns -1 in
	 *         case of failure
	 */
	public Integer[] checkAvailableSpotInParkAtSpecificHour(LocalDateTime timeToCheck,Integer parkId) {
		Integer[] amountAndCapacity = new Integer[2];

		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
				    "SELECT " +
				        "(SELECT SUM(Amount) " +
				        "FROM preorders " +
				        "WHERE EnterDate <= ? AND " +
				              "ExitDate > ? AND " +
				              "(OrderStatus = 'Wait Notify' OR " +
				              "OrderStatus = 'Notified Waiting List' OR "+
				               "OrderStatus = 'Notified' OR " +
				               "OrderStatus = 'Confirmed' OR " +
				               "OrderStatus = 'In Park') " +
				               "AND parkId = ?) AS Count, " +
				        "p.MaxCapacity,p.ReservedSpots " +
				    "FROM parks p " +
				    "WHERE p.parkId = ?;"
				);
			stmt.setString(1, timeToCheck.toString());
			stmt.setString(2, timeToCheck.toString());
			stmt.setInt(3,parkId);
			stmt.setInt(4, parkId);

			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				amountAndCapacity[0] = null;
				amountAndCapacity[1]=rs.getInt(2)-rs.getInt(3);
				return amountAndCapacity;
			}

			amountAndCapacity[0] = rs.getInt(1);
			amountAndCapacity[1]=rs.getInt(2)-rs.getInt(3);
			return amountAndCapacity;

		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		
	}
	
	public ArrayList<LocalDateTime> searchForAvailableDates7DaysForward (Order order){
		ArrayList<LocalDateTime> availableDates = new ArrayList<LocalDateTime>();
		int parkId = order.getParkName().getParkId();
		LocalDateTime enterTime = order.getEnterDate();
		int amountOfVisitors = order.getNumberOfVisitors();
		
		for(int i=0;i<7;i++) {
			if(isThisDateAvailable(parkId, enterTime.plusDays(i), amountOfVisitors)) {
				availableDates.add(enterTime.plusDays(i));
			}
		}
		return availableDates;
	}
	
	public boolean isThisDateAvailable(int parkId,LocalDateTime enterTime,int amountOfVisitors){
		Park requestedPark = new Park(parkId);
		boolean foundPark = parkQueries.getParkById(requestedPark);
		if(foundPark) {
			double estimatedVisitTime = requestedPark.getCurrentEstimatedStayTime();
			// Split the estimatedVisitTime into whole days and fractional day components
			long estimatedVisitTimeInHours = (long) (estimatedVisitTime);
			
			LocalDateTime exitTime = enterTime.plusHours(estimatedVisitTimeInHours);
			
			// Assuming a check every hour
			long frequencyInHours = 1;

			// Calculate the duration between enterTime and exitTime in hours
			long hoursBetween = java.time.Duration.between(enterTime, exitTime).toHours();

			// Calculate the number of checks needed
			long numberOfChecks = hoursBetween / frequencyInHours;
			
			for(int hour=0;hour<numberOfChecks;hour++) {
				Integer[] ret = checkAvailableSpotInParkAtSpecificHour(enterTime.plusHours(hour),parkId);
				if(ret[0]!=null && ret[0]+amountOfVisitors>ret[1])
					return false;	
			}
			return true;
		}
		return false;
	}


	/**
	 * Searches an order in DB using ownerId, ownerId is the user which owns the
	 * order
	 * 
	 * @param order - contains the ownerId
	 * @return on success returns Order_Found_Successfully 
	 *         on failure returns Such_Order_Does_Not_Exists 
	 *         exception: returns Excpetion_Was_Thrown
	 */
	
	//NOTICE : NOT USED THAT QUERY!!
	public DatabaseResponse fetchOrderByOwnerID(Order order) {

		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con
					.prepareStatement("SELECT * FROM preorders WHERE ownerId = ? AND OrderStatus!='Cancelled'");
			stmt.setString(1, order.getUserId());
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return DatabaseResponse.Such_Order_Does_Not_Exists;
			}

			order.setOrderId(rs.getInt(1));
			order.setParkName(ParkNameEnum.fromParkId(rs.getInt(2)));
			order.setUserId(String.format("%d", rs.getInt(3)));
			order.setOwnerType(UserTypeEnum.fromString(rs.getString(4)));
			order.setEnterDate(rs.getTimestamp(5).toLocalDateTime());
			order.setExitDate(rs.getTimestamp(6).toLocalDateTime());
			order.setPaid(rs.getBoolean(7));
			order.setStatus(OrderStatusEnum.fromString(rs.getString(8)));
			order.setEmail(rs.getString(9));
			order.setTelephoneNumber(rs.getString(10));
			order.setFirstName(rs.getString(11));
			order.setLastName(rs.getString(12));
			order.setOrderType(OrderTypeEnum.fromString(rs.getString(13)));
			order.setNumberOfVisitors(rs.getInt(14));
			order.setPrice(rs.getDouble(15));

			return DatabaseResponse.Order_Found_Successfully;

		} catch (SQLException ex) {
			return DatabaseResponse.Exception_Was_Thrown;
		}
	}

	/**
	 * gets an order and changes its status according statusToUpdate in DB, also
	 * changes the status in the given entity order.
	 * 
	 * @param order - the order where the change of its status will be made
	 * @return on success returns Order_Status_Updated 
	 *         on failure returns Such_Order_Does_Not_Exists
	 *         on exception returns Excpetion_Was_Thrown
	 */
	public boolean updateOrderStatus(Order order, OrderStatusEnum statusToUpdate) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE preorders SET OrderStatus = ?,PayStatus = '1' WHERE (OrderId = ?);");
			stmt.setString(1, statusToUpdate.toString());
			stmt.setInt(2, order.getOrderId());
			int rs = stmt.executeUpdate();

			// if the query ran successfully, but returned as empty table.
			if (rs == 0) {
				return false;
			}
			order.setStatus(statusToUpdate);
			order.setLastStatusUpdatedTime(LocalDateTime.now().toString());

			return true;

		} catch (SQLException ex) {
			return false;
		}
	}

	public DatabaseResponse checkIfNewOrderAvailableAtRequestedDate(Order order) {
		Park requestedPark = new Park(order.getParkName().getParkId());
		DatabaseResponse response=DatabaseResponse.Requested_Date_Is_Available;
		boolean foundPark  = parkQueries.getParkById(requestedPark);
		if(foundPark) {
			if(order.getNumberOfVisitors()>requestedPark.getCurrentMaxCapacity()) {
				return DatabaseResponse.Number_Of_Visitors_More_Than_Max_Capacity;
			}
			double estimatedVisitTime = requestedPark.getCurrentEstimatedStayTime();
			// Split the estimatedVisitTime into whole days and fractional day components
			long estimatedVisitTimeInHours = (long) (estimatedVisitTime);
			
			LocalDateTime enterTime = order.getEnterDate();
			LocalDateTime exitTime = enterTime.plusHours(estimatedVisitTimeInHours);
			order.setExitDate(exitTime);
			order.setPrice(requestedPark.getPrice());
			
			// Assuming a check every hour
			long frequencyInHours = 1;

			// Calculate the duration between enterTime and exitTime in hours
			long hoursBetween = java.time.Duration.between(enterTime, exitTime).toHours();

			// Calculate the number of checks needed
			long numberOfChecks = hoursBetween / frequencyInHours;
			
			for(int hour=0;hour<numberOfChecks;hour++) {
				Integer[] ret = checkAvailableSpotInParkAtSpecificHour(order.getEnterDate().plusHours(hour),order.getParkName().getParkId());
				if(ret[0]!=null && ret[0]+order.getNumberOfVisitors()>ret[1])
					return DatabaseResponse.Current_Date_Is_Full;	
			}
			
			order.setPrice(requestedPark.getPrice());
			return DatabaseResponse.Requested_Date_Is_Available;
			
		}
		
		return response;
	}
	
	/**
	 * Gets an order and adds it to the pre-order table in DB
	 * 
	 * @param order - the requested order to add in DB
	 * @return on Success: returns Order_Added_Into_Table 
	 *         on Failure: returns Failed
	 *         exception: returns Exception_Was_Thrown
	 */
	public synchronized boolean insertOrderIntoDB(Order order) {

		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"INSERT INTO preorders (ParkId, OwnerId, OwnerType, EnterDate, ExitDate, PayStatus, OrderStatus, Email, Phone, FirstName, LastName, OrderType, Amount, Price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
			
			
			stmt.setInt(1, order.getParkName().getParkId());
			stmt.setString(2, order.getUserId());
			stmt.setString(3, order.getOwnerType().name());
			stmt.setString(4, order.getEnterDate().toString());
			LocalDate date = order.getEnterDate().toLocalDate();
			LocalTime time = order.getExitDate().toLocalTime();
			LocalDateTime newExitTime = date.atTime(time);
			order.setExitDate(newExitTime);;
			stmt.setString(5, order.getExitDate().toString());
			int isPaid = order.isPaid() ? 1 : 0;
			stmt.setInt(6, isPaid); // insert as not paid yet
			stmt.setString(7, order.getStatus().toString());
			stmt.setString(8, order.getEmail());
			stmt.setString(9, order.getTelephoneNumber());
			stmt.setString(10, order.getFirstName());
			stmt.setString(11, order.getLastName());
			stmt.setString(12, order.getOrderType().toString());
			stmt.setInt(13, order.getNumberOfVisitors());
			stmt.setDouble(14, order.getPrice());
			int rs = stmt.executeUpdate();

			// if the query ran successfully, but returned as empty table.
			if (rs == 0) {
				return false;
			}
			
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
			    if (generatedKeys.next()) {
			        long orderId = generatedKeys.getLong(1); // Retrieve the first field in the ResultSet
			        order.setOrderId((int)orderId);
			    }
			}catch(SQLException ex) {
				return false;
			}
			
			return true;

		} catch (SQLException ex) {
			return false;
		}
	}
	
	/**
	 * Gets an order and updates its phone number in the DB
	 * @param order - must be already initialized with the updated phone number
	 * @return on success returns Order_PhoneNumber_Updated
	 *  	   on Failure: returns Failed
	 *         exception: returns Exception_Was_Thrown
	 */
	
	//NOTICE : NOT USED THAT QUERY!!
	public DatabaseResponse updateOrderPhoneNumber(Order order) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE preorders SET Phone = ? WHERE (OrderId = ?);");
			stmt.setString(1, order.getTelephoneNumber());
			stmt.setInt(2, order.getOrderId());
			int rs = stmt.executeUpdate();
			// if the query ran successfully, but returned as empty table.
			if (rs == 0) {
				return DatabaseResponse.Failed;
			}
			return DatabaseResponse.Order_PhoneNumber_Updated;

		} catch (SQLException ex) {
			return DatabaseResponse.Exception_Was_Thrown;
		}
	}
	
	/**
	 * Gets an order and updates its Email address in the DB
	 * @param order - must be already initialized with the updated Email
	 * @return on success returns Order_Email_Updated
	 *  	   on Failure: returns Failed
	 *         exception: returns Exception_Was_Thrown
	 */
	
	//NOTICE : NOT USED THAT QUERY!!
	public DatabaseResponse updateOrderEmail(Order order) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE preorders SET Email = ? WHERE (OrderId = ?);");
			stmt.setString(1, order.getEmail());
			stmt.setInt(2, order.getOrderId());
			int rs = stmt.executeUpdate();
			// if the query ran successfully, but returned as empty table.
			if (rs == 0) {
				return DatabaseResponse.Failed;
			}
			return DatabaseResponse.Order_Email_Updated;

		} catch (SQLException ex) {
			return DatabaseResponse.Exception_Was_Thrown;
		}
	}
	
	/**
	 * Gets an order and updates its number of visitors (amount) in the DB
	 * @param order - must be already initialized with the updated amount
	 * @return on success returns Order_Number_Of_Visitors_Updated
	 *  	   on Failure: returns Failed
	 *         exception: returns Exception_Was_Thrown
	 */
	//NOTICE : NOT USED THAT QUERY!!
	public DatabaseResponse updateOrderNumberOfVisitors(Order order) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE preorders SET Amount = ? WHERE (OrderId = ?);");
			stmt.setInt(1, order.getNumberOfVisitors());
			stmt.setInt(2, order.getOrderId());
			int rs = stmt.executeUpdate();
			// if the query ran successfully, but returned as empty table.
			if (rs == 0) {
				return DatabaseResponse.Failed;
			}
			return DatabaseResponse.Order_Number_Of_Visitors_Updated;

		} catch (SQLException ex) {
			return DatabaseResponse.Exception_Was_Thrown;
		}
	}
	
	/**
	 * Gets an order and updates its type in the DB according to the given requested type 
	 * @param order - the order to update
	 * @return on success returns Order_Type_Updated
	 *  	   on Failure: returns Failed
	 *         exception: returns Exception_Was_Thrown
	 */
	/**
	 * @param order
	 * @param requestedType
	 * @return
	 */
	
	//NOTICE : NOT USED THAT QUERY!!
	public DatabaseResponse updateOrderType(Order order, OrderTypeEnum requestedType) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE preorders SET OrderType = ? WHERE (OrderId = ?);");
			stmt.setString(1, order.getOrderType().toString());
			stmt.setInt(2, order.getOrderId());
			int rs = stmt.executeUpdate();
			// if the query ran successfully, but returned as empty table.
			if (rs == 0) {
				return DatabaseResponse.Failed;
			}
			return DatabaseResponse.Order_Type_Updated;

		} catch (SQLException ex) {
			return DatabaseResponse.Exception_Was_Thrown;
		}
	}
	
	
	/**
	 * Gets an order and updates its entry date time to the park in the DB according to the requested enter date time
	 * @param order - the order to update
	 * @return on success returns Order_EnterDate_Updated
	 *  	   on Failure: returns Failed
	 *         exception: returns Exception_Was_Thrown
	 */
	
	//NOTICE : NOT USED THAT QUERY!!
	public DatabaseResponse updateOrderEnterDate(Order order, LocalDateTime enterDate) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE preorders SET EnterDate = ? WHERE (OrderId = ?);");
			stmt.setString(1, enterDate.toString());
			stmt.setInt(2, order.getOrderId());
			int rs = stmt.executeUpdate();
			// if the query ran successfully, but returned as empty table.
			if (rs == 0) {
				return DatabaseResponse.Failed;
			}
			return DatabaseResponse.Order_EnterDate_Updated;

		} catch (SQLException ex) {
			return DatabaseResponse.Exception_Was_Thrown;
		}
	}
	
	
	/**
	 * Gets an order and updates its exit date time from the park in the DB according to the requested exit date time
	 * @param order - the order to update
	 * @return on success returns Order_ExitDate_Updated
	 *  	   on Failure: returns Failed
	 *         exception: returns Exception_Was_Thrown
	 */
	
	//NOTICE : NOT USED THAT QUERY!!
	public DatabaseResponse updateOrderExitDate(Order order, LocalDateTime exitDate) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE preorders SET ExitDate = ? WHERE (OrderId = ?);");
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
	
	
	/**
	 * @param status - requested order status
	 * @return the amount of preorder made in the system which currently have the requested status
	 */
	
	//NOTICE : NOT USED THAT QUERY!!
	public int returnTotalPreOrdersWithStatus(OrderStatusEnum status) {
		int ordersCount = 0;

		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) AS orderCount FROM preorders WHERE OrderStatus = ?");
			stmt.setString(1, status.toString());
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return ordersCount;
			}

			ordersCount = rs.getInt("orderCount");
			return ordersCount;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ordersCount;
	}
	
	
	public ArrayList<Order> searchForNotifiedOrdersOfSpecificClient(String customerId){
		ArrayList<Order> retList = new ArrayList<Order>();
		
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT OrderId,ParkId,EnterDate,PayStatus,Amount,OrderStatus FROM preorders WHERE (OrderStatus = 'Notified' OR OrderStatus = 'Notified Waiting List') AND OwnerId = ?");
			stmt.setString(1, customerId);
			
			ResultSet rs = stmt.executeQuery();
			
			if(!rs.next())
				return null;
			rs.previous();
			
			while(rs.next()) {
				Order orderToAdd = new Order();
				orderToAdd.setOrderId(rs.getInt(1));
				orderToAdd.setParkName(ParkNameEnum.fromParkId(rs.getInt(2)));
				orderToAdd.setEnterDate(rs.getTimestamp(3).toLocalDateTime());
				orderToAdd.setPaid((rs.getInt(4)==1?true:false));
				orderToAdd.setNumberOfVisitors(rs.getInt(5));
				orderToAdd.setStatus(OrderStatusEnum.fromString(rs.getString(6)));
				retList.add(orderToAdd);
			}
			
			return retList;
			
		}catch(SQLException ex) {
			return null;
		}
	}
	
	public ArrayList<Order> notifyTheNextOrdersInWaitingList(LocalDateTime enterDate,int parkId) {
		ArrayList<Order> ordersInWaitingList=new ArrayList<Order>();
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT p.OrderId, p.ParkId, w.enterListTime, p.EnterDate,p.Amount "
					+ "FROM preorders p "
					+ "JOIN waitinglist w ON p.OrderId = w.orderId "
					+ "WHERE p.ParkId = ? AND p.EnterDate = ? AND p.OrderStatus = 'In Waiting List'"
					+ "ORDER BY w.enterListTime");
			
			stmt.setInt(1, parkId);
			stmt.setString(2, enterDate.toString());
			ResultSet rs = stmt.executeQuery();
			
			if(!rs.next())
				return null;
			
			rs.previous();
			while(rs.next()) {
				Order orderToAdd = new Order();
				orderToAdd.setOrderId(rs.getInt(1));
				orderToAdd.setParkName(ParkNameEnum.fromParkId(rs.getInt(2)));
				orderToAdd.setEnterDate(rs.getTimestamp(4).toLocalDateTime());
				orderToAdd.setNumberOfVisitors(rs.getInt(5));
				ordersInWaitingList.add(orderToAdd);
			}
			
			return ordersInWaitingList;
			
		}catch(SQLException ex) {
			return null;
		}
	}
	
	public ArrayList<Order> importAllOrdersForToday(int parkId){
		ArrayList<Order> retList = new ArrayList<Order>();
		
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT OrderId AS orderId, 1 AS isPaid, Amount AS amountOfVisitors, Phone AS ownerPhone, EnterDate AS EnterTime, ExitDate AS ExitTime, OrderStatus, OrderType "
														+ "FROM occasionalvisits "
														+ "WHERE DATE(EnterDate) = CURDATE() AND (OrderStatus = 'Confirmed' OR OrderStatus = 'In Park') AND ParkId = ? "
														+ "UNION ALL "
														+ "SELECT OrderId, PayStatus AS isPaid, Amount, Phone, EnterDate, ExitDate, OrderStatus,OrderType "
														+ "FROM preorders "
														+ "WHERE DATE(EnterDate) = CURDATE() AND (OrderStatus = 'Confirmed' OR OrderStatus = 'In Park') AND ParkId = ?");
			stmt.setInt(1, parkId);
			stmt.setInt(2, parkId);
			
			ResultSet rs = stmt.executeQuery();
			if(!rs.next())
				return null;
			
			rs.previous();
			
			while(rs.next()) {
				Order orderToAdd = new Order();
				orderToAdd.setOrderId(rs.getInt(1));
				orderToAdd.setPaid(rs.getBoolean(2));
				orderToAdd.setNumberOfVisitors(rs.getInt(3));
				orderToAdd.setTelephoneNumber(rs.getString(4));
				orderToAdd.setEnterDate(rs.getTimestamp(5).toLocalDateTime());
				orderToAdd.setExitDate(rs.getTimestamp(6).toLocalDateTime());
				orderToAdd.setStatus(OrderStatusEnum.fromString(rs.getString(7)));
				orderToAdd.setOrderType(OrderTypeEnum.fromString(rs.getString(8)));
				retList.add(orderToAdd);
			}
			
			return retList;
			
		}catch(SQLException ex) {
			return null;
		}
	}
	
	
	public boolean deleteOrderFromTable(Order order) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM preorders WHERE OrderId = ?");
			
			stmt.setInt(1,order.getOrderId());
			
			int rs = stmt.executeUpdate();
			if(rs==0)
				return false;
			
			return true;
			
			
		}catch(SQLException ex) {
			return false;
		}
	}
	
	
}