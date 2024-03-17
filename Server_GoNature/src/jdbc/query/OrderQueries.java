package jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.DatabaseResponse;
import jdbc.MySqlConnection;
import logic.Order;
import utils.enums.EmployeeTypeEnum;
import utils.enums.OrderStatusEnum;
import utils.enums.OrderTypeEnum;
import utils.enums.ParkNameEnum;
import utils.enums.UserStatus;
import utils.enums.UserTypeEnum;

public class OrderQueries {

	public OrderQueries() {}
	
	public DatabaseResponse fetchOrderDetailsByID(Order order) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM preorders WHERE orderId = ?");
			stmt.setInt(1, order.getOrderId());
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				return DatabaseResponse.Such_Order_Does_Not_Exists;
			}
			
			order.setParkName(ParkNameEnum.fromParkId(rs.getInt(2)));
			order.setUserId(String.format("%d", rs.getInt(3)));
			order.setOwnerType(rs.getString(4));
			order.setEnterDate(rs.getTimestamp(5).toLocalDateTime());
			order.setExitDate(rs.getTimestamp(6).toLocalDateTime());
			order.setPaid(rs.getBoolean(7));
			order.setStatus(OrderStatusEnum.fromString(rs.getString(8)));
			order.setEmail(rs.getString(9));
			order.setTelephoneNumber(rs.getString(10));
			order.setFirstName(rs.getString(11));
			order.setLastName(rs.getString(12));
			order.setOrderType(OrderTypeEnum.fromString(rs.getString(13)));
			order.setNumberOfVisitors(rs.getInt(14));
			order.setPrice(rs.getDouble(15));
			
			return DatabaseResponse.Order_Found_Successfully;
			
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return DatabaseResponse.Failed;
		}
	}
}
