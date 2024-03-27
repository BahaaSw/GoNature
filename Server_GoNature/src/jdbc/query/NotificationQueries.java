package jdbc.query;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import jdbc.MySqlConnection;
import logic.Order;
import utils.enums.ParkNameEnum;
import utils.enums.UserTypeEnum;

/**
 * Handles database operations related to the notification status of orders,
 * including checking for orders that need to be updated due to their scheduled
 * entry time approaching or having passed.
 * Authors: Nadav Reubens, Gal Bitton, Tamer Amer, Rabea Lahham, Bahaldeen Swied, Ron Sisso
 */
public class NotificationQueries {
	
    /**
     * Checks all notified orders and updates their status to 'Cancelled' if the current
     * time has surpassed their entry time.
     * 
     * @param localDateTime The current LocalDateTime to compare against order entry times.
     * @return An ArrayList of Orders that were cancelled as a result of this operation.
     */
	public ArrayList<Order> CheckAllOrdersAndChangeToCancelledIfNeeded(LocalDateTime localDateTime)
	{
		ArrayList<Order> cancelledOrders = new ArrayList<Order>();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00");
	    String dateTimeString = localDateTime.format(formatter);
		
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT OrderId,ParkId,OwnerId,OwnerType,Email,Phone,FirstName,LastName,Amount FROM preorders WHERE OrderStatus = 'Notified' AND EnterDate <= ?");
			
			stmt.setString(1, dateTimeString);
			ResultSet rs= stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return null;
			}
			
			rs.previous();
			
			while(rs.next()) {
				Order orderToAdd = new Order();
				orderToAdd.setOrderId(rs.getInt(1));
				orderToAdd.setParkName(ParkNameEnum.fromParkId(rs.getInt(2)));
				orderToAdd.setUserId(String.valueOf(rs.getInt(3)));
				orderToAdd.setOwnerType(UserTypeEnum.fromString(rs.getString(4)));
				orderToAdd.setEmail(rs.getString(5));
				orderToAdd.setTelephoneNumber(rs.getString(6));
				orderToAdd.setFirstName(rs.getString(7));
				orderToAdd.setLastName(rs.getString(8));
				orderToAdd.setNumberOfVisitors(rs.getInt(9));
				cancelledOrders.add(orderToAdd);
			}
			
			return cancelledOrders;
	
		} catch (SQLException ex) 
		{
			ex.printStackTrace();
			return null;
		}
	}
	
    /**
     * Automatically updates the status of a specified order to 'Cancelled'.
     * 
     * @param order The order to cancel.
     */
	public void automaticallyCancelAllNotifiedOrders(Order order) {
		
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE preorders SET OrderStatus = 'Cancelled' WHERE OrderId = ?");
			
			stmt.setInt(1, order.getOrderId());
			int rs = stmt.executeUpdate();

			// if the query ran successfully, but returned as empty table.
			if (rs==0) {
				return;
			}
	
		} catch (SQLException ex) 
		{
			ex.printStackTrace();
			return;
		}
	}
	
    /**
     * Automatically marks a specified order as 'Irrelevant', typically used when
     * an order is no longer actionable or relevant to the current context.
     * 
     * @param order The order to mark as irrelevant.
     */
	public void automaticallyMarkOrdersAsIrrelevant(Order order) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE preorders SET OrderStatus = 'Irrelevant' WHERE OrderId = ?");
			
			stmt.setInt(1, order.getOrderId());
			int rs = stmt.executeUpdate();

			// if the query ran successfully, but returned as empty table.
			if (rs==0) {
				return;
			}
	
		} catch (SQLException ex) 
		{
			ex.printStackTrace();
			return;
		}
	}
	
    /**
     * Checks all orders with a status of 'Wait Notify' and updates their status to
     * 'Notified' if their entry time matches the specified LocalDateTime.
     * 
     * @param localDateTime The LocalDateTime to check orders against.
     * @return An ArrayList of Orders that were updated to 'Notified' status.
     */
	public ArrayList<Order> CheckAllOrdersAndChangeToNotifedfNeeded(LocalDateTime localDateTime)
	{
		Connection con = MySqlConnection.getInstance().getConnection();
//		LocalDateTime edge1 = localDateTime.plusHours(24).minusMinutes(1);
//		LocalDateTime edge2 = localDateTime.plusHours(24).plusMinutes(1);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00");
		String dateTimeString = localDateTime.format(formatter);
		try {
			ArrayList<Order> notifiedOrders = new ArrayList<Order>();
			PreparedStatement stmt = con.prepareStatement("SELECT OrderId,ParkId,OwnerId,OwnerType,Email,Phone,FirstName,LastName,Amount FROM preorders WHERE OrderStatus = 'Wait Notify' AND EnterDate = ?");
			stmt.setString(1, dateTimeString);
			ResultSet rs = stmt.executeQuery();
			
			if(!rs.next())
				return null;
			
			rs.previous();
			while(rs.next()) {
				Order orderToAdd = new Order();
				orderToAdd.setOrderId(rs.getInt(1));
				orderToAdd.setParkName(ParkNameEnum.fromParkId(rs.getInt(2)));
				orderToAdd.setUserId(String.valueOf(rs.getInt(3)));
				orderToAdd.setOwnerType(UserTypeEnum.fromString(rs.getString(4)));
				orderToAdd.setEmail(rs.getString(5));
				orderToAdd.setTelephoneNumber(rs.getString(6));
				orderToAdd.setFirstName(rs.getString(7));
				orderToAdd.setLastName(rs.getString(8));
				orderToAdd.setNumberOfVisitors(rs.getInt(9));
				notifiedOrders.add(orderToAdd);
			}
			
			return notifiedOrders;
			
		} catch (SQLException ex) 
		{
			ex.printStackTrace();
			return null;
		}
		
	}
	
    /**
     * Updates the status of all orders from 'Wait Notify' to 'Notified' for a specific
     * order. This is typically used as part of processing the queue of orders awaiting
     * notification.
     * 
     * @param orderToUpdate The order to update.
     */
	public void UpdateAllWaitNotifyOrdersToNotify(Order orderToUpdate) {

		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE preorders SET OrderStatus = 'Notified' WHERE OrderId = ?");
			
			stmt.setInt(1, orderToUpdate.getOrderId());
			int rs = stmt.executeUpdate();

			// if the query ran successfully, but returned as empty table.
			if (rs==0) {
				return;
			}
	
		} catch (SQLException ex) 
		{
			ex.printStackTrace();
			return;
		}
		
}
	
	//TODO: USE THIS QUERY
    /**
     * Checks the waiting list for all orders that are no longer relevant based on
     * their entry time and updates them accordingly.
     * 
     * @param localDateTime The current LocalDateTime used to assess relevance.
     * @return An ArrayList of Orders that were deemed irrelevant and updated.
     */
	public ArrayList<Order> CheckWaitingListAndRemoveAllIrrelcantOrders(LocalDateTime localDateTime)
	{   
		ArrayList<Order> irrelevantOrders = new ArrayList<Order>();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00");
	    String dateTimeString = localDateTime.format(formatter);
		
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT OrderId,ParkId,OwnerId,OwnerType,Email,Phone,FirstName,LastName,Amount FROM preorders WHERE (OrderStatus = 'In Waiting List' OR OrderStatus = 'Notified Waiting List') AND EnterDate < ?");
			
			stmt.setString(1, dateTimeString);
			ResultSet rs= stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return null;
			}
			
			rs.previous();
			
			while(rs.next()) {
				Order orderToAdd = new Order();
				orderToAdd.setOrderId(rs.getInt(1));
				orderToAdd.setParkName(ParkNameEnum.fromParkId(rs.getInt(2)));
				orderToAdd.setUserId(String.valueOf(rs.getInt(3)));
				orderToAdd.setOwnerType(UserTypeEnum.fromString(rs.getString(4)));
				orderToAdd.setEmail(rs.getString(5));
				orderToAdd.setTelephoneNumber(rs.getString(6));
				orderToAdd.setFirstName(rs.getString(7));
				orderToAdd.setLastName(rs.getString(8));
				orderToAdd.setNumberOfVisitors(rs.getInt(9));
				irrelevantOrders.add(orderToAdd);
			}
			
			return irrelevantOrders;
	
		} catch (SQLException ex) 
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	//NOTICE : NOT USED THAT QUERY!!
    /**
     * Checks if an order that has been notified for 24 hours has been updated
     * from the server-side. This method is currently not used and may be part
     * of a future implementation or legacy system.
     * 
     * @param OrderId The ID of the order to check.
     * @return true if the order has been updated within 24 hours, false otherwise.
     */
	public boolean CheckNotifiedFromServer24Hours(int OrderId)
    {

        try {
            Connection con = MySqlConnection.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT OrderStatus WHERE OrderId=?");

            stmt.setInt(1, OrderId);
            ResultSet rs = stmt.executeQuery();

            // if the query ran successfully, but returned as empty table.
            if (!rs.next()) {
                return false;
            }else if(rs.getString(1).equals("Notified"))
                return true;
            return false;
        } catch (SQLException ex) 
        {
            ex.printStackTrace();
            return false;
        }

    }
	
	
}