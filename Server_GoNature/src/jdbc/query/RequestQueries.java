package jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jdbc.DatabaseResponse;
import jdbc.MySqlConnection;
import logic.Request;
import utils.enums.RequestStatusEnum;
import utils.enums.RequestTypeEnum;

public class RequestQueries {
	
//	public DatabaseResponse UpdateParkMaxCapacity(Request request) {
//		try {
//			Connection con = MySqlConnection.getInstance().getConnection();
//			PreparedStatement stmt = con.prepareStatement("UPDATE parks SET MaxCapacity = ? WHERE ParkId = ?");
//			stmt.setInt(1, request.getNewValue());
//			stmt.setInt(2,request.getParkId());
//			int rs = stmt.executeUpdate();
//
//			// if the query ran successfully, but returned as empty table.
//			if (rs==0) {
//				return DatabaseResponse.Such_Park_Does_Not_Exists;
//			}
//
//			return DatabaseResponse.Park_MaxCapacity_Was_Updated;
//			
//		} catch (SQLException ex) {
////			serverController.printToLogConsole("Query search for user failed");
//			return DatabaseResponse.Failed;
//		}
//	}
//	public DatabaseResponse UpdateParkEstimatedVisitTime(Request request)
//	{
//		try {
//			Connection con = MySqlConnection.getInstance().getConnection();
//			PreparedStatement stmt = con.prepareStatement("UPDATE parks SET EstimatedVisitTime = ? WHERE ParkId = ?");
//			stmt.setInt(1, request.getNewValue());
//			stmt.setInt(2,request.getParkId());
//			int rs = stmt.executeUpdate();
//
//			// if the query ran successfully, but returned as empty table.
//			if (rs==0) {
//				return DatabaseResponse.Such_Park_Does_Not_Exists;
//			}
//			
//			return DatabaseResponse.Park_EstimatedStayTime_Was_Updated;
//			
//		} catch (SQLException ex) {
////			serverController.printToLogConsole("Query search for user failed");
//			return DatabaseResponse.Failed;
//		}
//	}
//	
//	public DatabaseResponse UpdateParkReservedSpots(Request request)
//	{
//		try {
//			Connection con = MySqlConnection.getInstance().getConnection();
//			PreparedStatement stmt = con.prepareStatement("UPDATE parks SET ReservedSpots = ? WHERE ParkId = ?");
//			stmt.setInt(1, request.getNewValue());
//			stmt.setInt(2,request.getParkId());
//			int rs = stmt.executeUpdate();
//
//			// if the query ran successfully, but returned as empty table.
//			if (rs==0) {
//				return DatabaseResponse.Such_Park_Does_Not_Exists;
//			}
//
//
//			return DatabaseResponse.Park_ReservedSpots_Was_Updated;
//			
//		} catch (SQLException ex) {
////			serverController.printToLogConsole("Query search for user failed");
//			return DatabaseResponse.Failed;
//		}
//	}
	
	public DatabaseResponse ShowAllParkManagerRequests(ArrayList<Request> request) //Method to pull all the requests with pending status. (Tamir/Siso)
	{
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM requests WHERE RequestStatus = 'Pending'");

			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.first()) {
				return DatabaseResponse.No_Pending_Request_Exists;
			}
			
			while (rs.next()) {

	            Request newRequest = new Request();
	            
	            newRequest.setRequestId(rs.getInt(1)); 
	            newRequest.setParkId(rs.getInt(2));
	            newRequest.setRequestType(RequestTypeEnum.fromString(rs.getString(3)));
	            newRequest.setOldValue(rs.getInt(4));
	            newRequest.setNewValue(rs.getInt(5));
	            newRequest.setRequestStatus(RequestStatusEnum.fromString(rs.getString(6)));
	            newRequest.setRequestDate(rs.getTimestamp(7).toLocalDateTime());

	            request.add(newRequest);
	        }
			
			return DatabaseResponse.Pending_Request_Pulled;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return DatabaseResponse.Failed;
		}
	}
	
	public DatabaseResponse UpdateStatusRequest(Request request,String status)
	{
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE requests SET RequestStatus = WHERE RequestId = ?");
			stmt.setString(1, status);
			stmt.setInt(2,request.getRequestId());
			int rs = stmt.executeUpdate();

			// if the query ran successfully, but returned as empty table.
			if (rs==0) {
				return DatabaseResponse.No_Request_Exsists;
			}

			return DatabaseResponse.Request_Was_Updated;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return DatabaseResponse.Failed;
		}
	}
	
}
	