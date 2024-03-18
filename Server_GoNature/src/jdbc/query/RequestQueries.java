package jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.DatabaseResponse;
import jdbc.MySqlConnection;
import logic.Request;
import utils.enums.RequestStatusEnum;
import utils.enums.RequestTypeEnum;

public class RequestQueries {
	
	public DatabaseResponse UpdateParkMaxCapacity(Request request) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE parks SET MaxCapacity = ? WHERE ParkId = ?");
			stmt.setInt(1, request.getNewValue());
			stmt.setInt(2,request.getParkId());
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return DatabaseResponse.Such_Park_Does_Not_Exists;
			}
			
			request.setRequestId(rs.getInt(1));
			request.setParkId(rs.getInt(2));
			request.setRequestType(RequestTypeEnum.fromString(rs.getString(3)));
			request.setOldValue(rs.getInt(4));
			request.setNewValue(rs.getInt(5));
			request.setRequestStatus(RequestStatusEnum.fromString(rs.getString(6)));
			request.setRequestDate(rs.getTimestamp(7).toLocalDateTime());

			return DatabaseResponse.Park_MaxCapacity_Was_Updated;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return DatabaseResponse.Failed;
		}
	}
	public DatabaseResponse UpdateParkEstimatedVisitTime(Request request)
	{
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE parks SET EstimatedVisitTime = ? WHERE ParkId = ?");
			stmt.setInt(1, request.getNewValue());
			stmt.setInt(2,request.getParkId());
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return DatabaseResponse.Such_Park_Does_Not_Exists;
			}
			
			request.setRequestId(rs.getInt(1));
			request.setParkId(rs.getInt(2));
			request.setRequestType(RequestTypeEnum.fromString(rs.getString(3)));
			request.setOldValue(rs.getInt(4));
			request.setNewValue(rs.getInt(5));
			request.setRequestStatus(RequestStatusEnum.fromString(rs.getString(6)));
			request.setRequestDate(rs.getTimestamp(7).toLocalDateTime());

			return DatabaseResponse.Park_EstimatedStayTime_Was_Updated;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return DatabaseResponse.Failed;
		}
	}
	
	public DatabaseResponse UpdateParkReservedSpots(Request request)
	{
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE parks SET ReservedSpots = ? WHERE ParkId = ?");
			stmt.setInt(1, request.getNewValue());
			stmt.setInt(2,request.getParkId());
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return DatabaseResponse.Such_Park_Does_Not_Exists;
			}
			
			request.setRequestId(rs.getInt(1));
			request.setParkId(rs.getInt(2));
			request.setRequestType(RequestTypeEnum.fromString(rs.getString(3)));
			request.setOldValue(rs.getInt(4));
			request.setNewValue(rs.getInt(5));
			request.setRequestStatus(RequestStatusEnum.fromString(rs.getString(6)));
			request.setRequestDate(rs.getTimestamp(7).toLocalDateTime());

			return DatabaseResponse.Park_ReservedSpots_Was_Updated;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return DatabaseResponse.Failed;
		}
	}
	
	public DatabaseResponse UpdateParkCurrentInPark(Request request)
	{
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE parks SET CurrentInPark = ? WHERE ParkId = ?");
			stmt.setInt(1, request.getNewValue());
			stmt.setInt(2,request.getParkId());
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return DatabaseResponse.Such_Park_Does_Not_Exists;
			}
			
			request.setRequestId(rs.getInt(1));
			request.setParkId(rs.getInt(2));
			request.setRequestType(RequestTypeEnum.fromString(rs.getString(3)));
			request.setOldValue(rs.getInt(4));
			request.setNewValue(rs.getInt(5));
			request.setRequestStatus(RequestStatusEnum.fromString(rs.getString(6)));
			request.setRequestDate(rs.getTimestamp(7).toLocalDateTime());

			return DatabaseResponse.Park_ReservedSpots_Was_Updated;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return DatabaseResponse.Failed;
		}
	}
	

	
}
