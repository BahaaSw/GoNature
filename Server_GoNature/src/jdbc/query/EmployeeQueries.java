package jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jdbc.DatabaseResponse;
import jdbc.MySqlConnection;
import logic.Employee;
import logic.Guide;
import utils.enums.EmployeeTypeEnum;
import utils.enums.ParkNameEnum;
import utils.enums.UserStatus;
import utils.enums.UserTypeEnum;

public class EmployeeQueries {

	public EmployeeQueries() {
	}

	public DatabaseResponse searchForApprovedEmployee(Employee employee) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE Username = ? AND UserType = 'Employee' ");
			stmt.setString(1, employee.getUsername());
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return DatabaseResponse.Such_Employee_Not_Found;
			}
			
			if(!employee.getPassword().equals(rs.getString(3))) {
				return DatabaseResponse.Password_Incorrect;
			}
			
			employee.setUserId(rs.getString(1));
			employee.setFirstName(rs.getString(4));
			employee.setLastName(rs.getString(5));
			employee.setPhoneNumber(rs.getString(6));
			employee.setEmailAddress(rs.getString(7));
			employee.setUserStatus(UserStatus.Approved);
			employee.setUserType(UserTypeEnum.Employee);
			employee.setRelatedPark(ParkNameEnum.fromParkId(rs.getInt(10)));
			employee.setEmployeeType(EmployeeTypeEnum.fromString(rs.getString(11).trim()));
			
			return DatabaseResponse.Employee_Connected_Successfully;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return DatabaseResponse.Failed;
		}
	}
	
	//NOTICE : NOT USED THAT QUERY!!
	public DatabaseResponse checkIfVisitorPaidAndConfirmed(int orderId) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT PayStatus, OrderStatus FROM preorders WHERE orderId = ?");
			stmt.setInt(1, orderId);
			ResultSet rs = stmt.executeQuery();

			if(!rs.next()) {
				return DatabaseResponse.Such_Order_Does_Not_Exists;
			}

			int isPaid = rs.getInt("PayStatus");
			String orderStatus = rs.getString("OrderStatus");

			if(orderStatus.equals("Confirmed")) {
				if(isPaid == 1) {
					return DatabaseResponse.Order_Paid_And_Confirmed;
				}
				return DatabaseResponse.Order_Not_Paid;
			}
			return DatabaseResponse.Order_Not_Confirmed;


		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	

	public DatabaseResponse UpdateGuideStatusToApprove(Guide guide) //Update guide permission from Pending to Approve (Tamir/Siso)
	{
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE users SET Status = 'Approved' WHERE UserId = ?");
			stmt.setString(1, guide.getUserId());
			int rs = stmt.executeUpdate();

			// if the query ran successfully, but returned as empty table.
			if (rs==0) {
				return DatabaseResponse.Such_Guide_Not_Found;
			}

			return DatabaseResponse.Guide_Was_Approved;

		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return DatabaseResponse.Failed;
		}
	}

	public DatabaseResponse ShowAllGuidesWithPendingStatus(ArrayList<Guide> guideList) //Method to pull all the requests with pending status. (Tamir/Siso)
	{
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE Status = 'Pending'");

			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.first()) {
				return DatabaseResponse.No_Pending_Request_Exists;
			}

			rs.previous();
			while (rs.next()) {

	            Guide guide = new Guide();

	            guide.setUserId(rs.getString(1));
	            guide.setUsername(rs.getString(2));
	            guide.setPassword(rs.getString(3));
	            guide.setFirstName(rs.getString(4));
	            guide.setLastName(rs.getString(5));
	            guide.setPhoneNumber(rs.getString(6));
	            guide.setEmailAddress(rs.getString(7));

	            String statusStr = rs.getString(8); // Use column name or index as appropriate
	            UserStatus status = UserStatus.fromString(statusStr); // This returns a UserStatus enum
	            guide.setUserStatus(status);

	            String userType = rs.getString(9);
	            UserTypeEnum type = UserTypeEnum.fromString(userType);
	            guide.setUserType(type);

	            guideList.add(guide);
	        }

			return DatabaseResponse.Pending_Request_Pulled;

		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return DatabaseResponse.Failed;
		}
	}

}
