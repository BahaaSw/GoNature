package jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import jdbc.DatabaseResponse;
import jdbc.MySqlConnection;
import logic.Request;
import utils.enums.RequestStatusEnum;
import utils.enums.RequestTypeEnum;

/**
 * 
 * @Author NadavReubens
 * @Author Gal Bitton
 * @Author Tamer Amer
 * @Author Rabea Lahham
 * @Author Bahaldeen Swied
 * @Author Ron Sisso
 * @version 1.0.0 
 */

/**
 * This class is responsible for executing database operations related to requests,
 * including showing all park manager requests with a pending status, updating the status of a request,
 * and inserting new requests into the database.
 */

public class RequestQueries {
	private ParkQueries parkQueries= new ParkQueries();
	
    /**
     * Retrieves all requests with a status of "Pending" from the database.
     * @param request An ArrayList of Request objects to be populated with the retrieved requests.
     * @return A DatabaseResponse indicating the outcome of the operation.
     */
	
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
			
			return DatabaseResponse.Pending_Request_Pulled;
			
		} catch (SQLException ex) {
			// Log failure (actual logging commented out)
			return DatabaseResponse.Failed;
		}
	}
	
    /**
     * Updates the status of a specific request in the database.
     * @param request The Request object containing the ID of the request to update.
     * @param status The new status to set for the request.
     * @return A DatabaseResponse indicating the outcome of the operation.
     */
	
	public DatabaseResponse UpdateStatusRequest(Request request,String status)
	{
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE requests SET RequestStatus = ? WHERE RequestId = ?");
			stmt.setString(1, status);
			stmt.setInt(2,request.getRequestId());
			int rs = stmt.executeUpdate();

			// if the query ran successfully, but returned as empty table.
			if (rs==0) {
				return DatabaseResponse.No_Request_Exists;
			}
			
			if(status.equals("Approved")) {
				parkQueries.InsertNewValueInRequestedPark(request);
			}

			return DatabaseResponse.Request_Was_Updated;
			
		} catch (SQLException ex) {
			// Log failure (actual logging commented out)
			return DatabaseResponse.Failed;
		}
	}
	
    /**
     * Inserts a new request into the database, ensuring that no duplicate pending requests of the same type
     * for the same park exist.
     * @param request The Request object to insert into the database.
     * @return True if the request was successfully inserted, otherwise false.
     */
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
	