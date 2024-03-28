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

public class CustomerQueries {
	
	public CustomerQueries() {
	}

	public DatabaseResponse searchForApprovedGuide(Guide guide) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE Username = ? AND UserType = 'Guide'");
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
