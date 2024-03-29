package jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import jdbc.MySqlConnection;
import logic.Request;
import utils.enums.RequestStatusEnum;
import utils.enums.RequestTypeEnum;
import utils.enums.ServerResponse;

public class RequestQueries {
	private ParkQueries parkQueries= new ParkQueries();
	
	public ServerResponse ShowAllParkManagerRequests(ArrayList<Request> request) //Method to pull all the requests with pending status. (Tamir/Siso)
	{
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM requests WHERE RequestStatus = 'Pending'");

			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.first()) {
				return ServerResponse.There_Are_Not_Pending_Requests;
			}
			
			rs.previous();
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
			
			return ServerResponse.Pending_Requests_Found_Successfully;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return ServerResponse.Query_Failed;
		}
	}
	
	public ServerResponse UpdateStatusRequest(Request request,String status)
	{
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE requests SET RequestStatus = ? WHERE RequestId = ?");
			stmt.setString(1, status);
			stmt.setInt(2,request.getRequestId());
			int rs = stmt.executeUpdate();

			// if the query ran successfully, but returned as empty table.
			if (rs==0) {
				return ServerResponse.Updated_Requests_Failed;
			}
			
			if(status.equals("Approved")) {
				parkQueries.InsertNewValueInRequestedPark(request);
			}

			return ServerResponse.Updated_Requests_Successfully;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return ServerResponse.Query_Failed;
		}
	}
	
	public boolean InsertNewRequest(Request request) {
	    try {
	        Connection con = MySqlConnection.getInstance().getConnection();

	        // Check if a pending request of the same type for the same park exists
	        String checkSql = "SELECT 1 FROM requests WHERE ParkId = ? AND RequestType = ? AND RequestStatus = 'Pending'";
	        PreparedStatement checkStmt = con.prepareStatement(checkSql);
	        checkStmt.setInt(1, request.getParkId());
	        checkStmt.setString(2, request.getRequestType().name());
	        ResultSet checkRs = checkStmt.executeQuery();
	        if (checkRs.next()) {
	            // A matching pending request exists, so do not insert a new one
	            return false;
	        }

	        // No matching pending request exists, proceed with insert
	        String insertSql = "INSERT INTO requests (ParkId, RequestType, OldValue, NewValue, RequestStatus, RequestDate) VALUES (?, ?, ?, ?, ?, ?)";
	        PreparedStatement stmt = con.prepareStatement(insertSql);
	        stmt.setInt(1, request.getParkId());
	        stmt.setString(2, request.getRequestType().name());
	        stmt.setInt(3, request.getOldValue());
	        stmt.setInt(4, request.getNewValue());
	        stmt.setString(5, request.getRequestStatus().name());
	        
	        // Convert LocalDateTime to Timestamp
	        Timestamp requestDateTimestamp = Timestamp.valueOf(request.getRequestDate());
	        stmt.setTimestamp(6, requestDateTimestamp);

	        int rs = stmt.executeUpdate();

	        // if the query ran successfully, but returned as empty table.
	        if (rs == 0) {
	            return false;
	        }

	        return true;
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        return false;
	    }
	}
	
}
	