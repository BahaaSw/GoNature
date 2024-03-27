package jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.DatabaseResponse;
import jdbc.MySqlConnection;
import logic.Guide;
import logic.Visitor;
import utils.enums.UserStatus;
import utils.enums.UserTypeEnum;

/**
 * Handles database queries related to customers, including both guides and visitors,
 * providing functionality to search for approved guides and check access for visitors.
 * Authors: Nadav Reubens, Gal Bitton, Tamer Amer, Rabea Lahham, Bahaldeen Swied, Ron Sisso
 */
public class CustomerQueries {
	
	public CustomerQueries() {
	}
	
    /**
     * Searches for an approved guide in the database using the guide's username.
     * Verifies the password and checks the approval status of the guide.
     * 
     * @param guide A Guide object containing the username and password to verify.
     * @return A DatabaseResponse indicating the result of the search and verification process.
     *         Returns Guide_Connected_Successfully if the guide is found and approved,
     *         Such_Guide_Not_Found if no guide matches the username,
     *         Password_Incorrect if the password does not match,
     *         Guide_Not_Approve_Yet if the guide's status is still pending, or
     *         Failed if an SQL exception occurs.
     */
	public DatabaseResponse searchForApprovedGuide(Guide guide) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE Username = ?");
			stmt.setString(1, guide.getUsername());
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return DatabaseResponse.Such_Guide_Not_Found;
			}
			
			if(!guide.getPassword().equals(rs.getString(3))) {
				return DatabaseResponse.Password_Incorrect;
			}
			
			if(rs.getString(8).equals("Pending")) {
				return DatabaseResponse.Guide_Not_Approve_Yet;
			}
			
			guide.setUserId(rs.getString(1));
			guide.setFirstName(rs.getString(4));
			guide.setLastName(rs.getString(5));
			guide.setPhoneNumber(rs.getString(6));
			guide.setEmailAddress(rs.getString(7));
			guide.setUserStatus(UserStatus.Approved);
			guide.setUserType(UserTypeEnum.Guide);
			
			return DatabaseResponse.Guide_Connected_Successfully;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return DatabaseResponse.Failed;
		}
	}
	
    /**
     * Searches for an active preorder associated with a visitor's ID, excluding orders
     * that are cancelled or have passed their visit time.
     * 
     * @param visitor A Visitor object containing the customer ID to search for.
     * @return A DatabaseResponse indicating whether the visitor has an active preorder.
     *         Returns Visitor_Connected_Successfully if an active order is found,
     *         Doesnt_Have_Active_Order if no active order exists for the visitor, or
     *         Failed if an SQL exception occurs.
     */
	public DatabaseResponse searchAccessForVisitor(Visitor visitor) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM preorders WHERE OwnerId = ? AND OrderStatus != 'Cancelled' AND OrderStatus != 'Time Passed'");
			stmt.setString(1, visitor.getCustomerId());
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return DatabaseResponse.Doesnt_Have_Active_Order;
			}
			
			visitor.setFirstName(rs.getString(11));
			visitor.setLastName(rs.getString(12));
			visitor.setPhoneNumber(rs.getString(10));
			visitor.setEmailAddress(rs.getString(9));
			visitor.setUserType(UserTypeEnum.Visitor);
			visitor.setVisitorId(rs.getString(3));
			
			return DatabaseResponse.Visitor_Connected_Successfully;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return DatabaseResponse.Failed;
		}
	}
}
