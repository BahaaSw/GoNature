package jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.DatabaseResponse;
import jdbc.MySqlConnection;
import logic.Employee;
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
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE Username = ?");
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
			employee.setEmployeeType(EmployeeTypeEnum.fromString(rs.getString(11)));
			
			return DatabaseResponse.Employee_Connected_Successfully;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return DatabaseResponse.Failed;
		}
	}
}
