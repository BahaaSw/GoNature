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


public class NotificationQueries {
	
	public ArrayList<Order> CheckAllOrdersAndChangeToCancelledIfNeeded(LocalDateTime localDateTime)
	{
		ArrayList<Order> cancelledOrders = new ArrayList<Order>();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00");
	    String dateTimeString = localDateTime.format(formatter);
		
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT OrderId,ParkId,OwnerId,OwnerType,Email,Phone,FirstName,LastName,Amount,EnterDate FROM preorders WHERE OrderStatus = 'Notified' AND EnterDate <= ?");
			
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
				orderToAdd.setEnterDate(rs.getTimestamp(10).toLocalDateTime());
				cancelledOrders.add(orderToAdd);
			}
			
			return cancelledOrders;
	
		} catch (SQLException ex) 
		{
			ex.printStackTrace();
			return null;
		}
	}
	
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
	
	public ArrayList<Order> CheckAllOrdersAndChangeToNotifedfNeeded(LocalDateTime localDateTime)
	{
		Connection con = MySqlConnection.getInstance().getConnection();

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
	
	public ArrayList<Order> CheckAllWaitingListOrdersAndCancelAutomaticallyIfNotConfirmed()
	{
		ArrayList<Order> cancelledOrders = new ArrayList<Order>();
		
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT p.OrderId, p.ParkId, p.OwnerId, p.OwnerType, p.Email, p.Phone, p.FirstName, p.LastName, p.Amount, p.EnterDate"
					+ " FROM preorders AS p"
					+ " JOIN waitinglist AS w ON p.OrderId = w.orderId"
					+ " WHERE p.OrderStatus = 'Notified Waiting List'"
					+ " AND w.notificationSentTime <= CURRENT_TIMESTAMP - INTERVAL '2' HOUR"
					+ " AND w.notificationSentTime > CURRENT_TIMESTAMP - INTERVAL '2' HOUR - INTERVAL '1' MINUTE");
			
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
				orderToAdd.setEnterDate(rs.getTimestamp(10).toLocalDateTime());
				cancelledOrders.add(orderToAdd);
			}
			
			return cancelledOrders;
	
		} catch (SQLException ex) 
		{
			ex.printStackTrace();
			return null;
		}
		
	}
	
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