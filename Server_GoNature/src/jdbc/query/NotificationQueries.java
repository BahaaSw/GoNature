package jdbc.query;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jdbc.MySqlConnection;


public class NotificationQueries {
	
	public void CheckAllOrdersAndChangeToCancelledIfNeeded(LocalDateTime localDateTime)
	{
		localDateTime = localDateTime.plusHours(22);
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String dateTimeString = localDateTime.format(formatter);
		
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE preorders SET status = 'Cancelled' WHERE OrderStatus = 'Notified' AND ? > EnterDate");
			
			stmt.setString(1, dateTimeString);
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
	
	public void CheckAllOrdersAndChangeToNotifedfNeeded(LocalDateTime localDateTime)
	{
	    LocalDateTime edge1 = localDateTime.plusHours(24).minusMinutes(1);
	    LocalDateTime edge2 = localDateTime.plusHours(24).plusMinutes(1);
	    
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String dateTimeString1 = edge1.format(formatter);
	    String dateTimeString2 = edge2.format(formatter);
		
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE preorders SET status = 'Notified' WHERE OrderStatus = 'Wait-Notify' AND ? < EnterDate AND ? > EnterDate");
			
			stmt.setString(1, dateTimeString1);
			stmt.setString(1, dateTimeString2);
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
	
	public void CheckWaitingListAndRemoveAllIrrelcantOrders(LocalDateTime localDateTime)
	{
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String dateTimeString = localDateTime.format(formatter);
	    try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE waitinglist SET status = 'irrelevant' WHERE OrderStatus = 'Wait-Notify' AND ? > EnterDate");
			
			stmt.setString(1, dateTimeString);
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
	
	
}