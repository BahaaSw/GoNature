package jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jdbc.DatabaseResponse;
import jdbc.MySqlConnection;

import logic.Park;
import logic.Request;
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

	
}