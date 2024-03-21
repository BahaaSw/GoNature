package jdbc.query;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import jdbc.DatabaseResponse;
import jdbc.MySqlConnection;
import logic.CancellationsReport;
import logic.ParkDailySummary;
import logic.VisitsReport;
import utils.ReportGenerator;
import utils.enums.OrderStatusEnum;
import utils.enums.OrderTypeEnum;
import utils.enums.ParkNameEnum;

public class ReportsQueries {

	public ParkDailySummary getParkDailySummaryByDay(int month, int day, int parkId) {
		ParkDailySummary currentDaySummary = new ParkDailySummary();
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con
					.prepareStatement("SELECT COUNT(CASE WHEN OrderStatus = 'Canceled' THEN 1 END) AS CanceledOrders, "
							+ "COUNT(CASE WHEN OrderStatus = 'Time Passed' THEN 1 END) AS TimePassedOrders, "
							+ "COUNT(*) AS TotalOrders " + "FROM preorders " + "WHERE ParkId = ? "
							+ "AND MONTH(EnterDate) = ? AND DAY(EnterDate) = ?");

			stmt.setInt(1, parkId);
			stmt.setInt(2, month);
			stmt.setInt(3, day);
			ResultSet rs = stmt.executeQuery();

			// if the query ran successfully, but returned as empty table.
			if (!rs.next()) {
				currentDaySummary.setCancelsOrders(0);
				currentDaySummary.setDay(day);
				currentDaySummary.setTimePassedOrders(0);
				currentDaySummary.setTotalOrders(0);
				currentDaySummary.setPark(ParkNameEnum.fromParkId(parkId));
				return currentDaySummary;
			}
			currentDaySummary.setCancelsOrders(rs.getInt(1));
			currentDaySummary.setDay(day);
			currentDaySummary.setTimePassedOrders(rs.getInt(2));
			currentDaySummary.setTotalOrders(rs.getInt(3));
			currentDaySummary.setPark(ParkNameEnum.fromParkId(parkId));
			return currentDaySummary;

		} catch (SQLException ex) {
//		serverController.printToLogConsole("Query search for user failed");
			return null;
		}

	}

	public boolean generateCancellationsReport(CancellationsReport report) {
		int year = report.getYear();
		int month = report.getMonth();
		int parkId = report.getRequestedPark().getParkId();
		YearMonth yearMonth = YearMonth.of(year, month);
		int daysInMonth = yearMonth.lengthOfMonth();
		HashMap<Integer, ParkDailySummary> parkSummaryByDays = new HashMap<Integer, ParkDailySummary>();

		for (int day = 1; day <= daysInMonth; day++) {
			ParkDailySummary temp = getParkDailySummaryByDay(month, day, parkId);
			if (temp != null) {
				parkSummaryByDays.put(day, temp);
			}
		}
		if (parkSummaryByDays.isEmpty())
			return false;

		report.setReportData(parkSummaryByDays);
		report.setBlobPdfContent(ReportGenerator.generateCancellationsReportAsPdfBlob(report));

		if (insertGeneratedCancellationsReportToDatabase(report))
			return true;

		return false;

	}

	public boolean generateVisitsReport(VisitsReport report) {
		int year = report.getYear();
		int month = report.getMonth();
		int parkId = report.getRequestedPark().getParkId();
		boolean isQuerySucceed = getParkVisitsSummaryByEnterTime(report);
		if (!isQuerySucceed)
			return false;

		isQuerySucceed = getParkIdleVisitTimeSummary(report);
		if (!isQuerySucceed)
			return false;

		report.setBlobPdfContent(ReportGenerator.generateTotalVisitorsAmountReportAsPdf(report));

		if (insertVisitsAmountReportToDatabase(report))
			return true;
		return false;
	}

	private boolean getParkVisitsSummaryByEnterTime(VisitsReport report) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
				    "SELECT " +
				    "OrderType, " +
				    "SUM(CASE WHEN TIME(EnterDate) >= '08:00:00' AND TIME(EnterDate) < '09:00:00' THEN Amount ELSE 0 END) AS Visitors_08_09, " +
				    "SUM(CASE WHEN TIME(EnterDate) >= '09:00:00' AND TIME(EnterDate) < '10:00:00' THEN Amount ELSE 0 END) AS Visitors_09_10, " +
				    "SUM(CASE WHEN TIME(EnterDate) >= '10:00:00' AND TIME(EnterDate) < '11:00:00' THEN Amount ELSE 0 END) AS Visitors_10_11, " +
				    "SUM(CASE WHEN TIME(EnterDate) >= '11:00:00' AND TIME(EnterDate) < '12:00:00' THEN Amount ELSE 0 END) AS Visitors_11_12, " +
				    "SUM(CASE WHEN TIME(EnterDate) >= '12:00:00' AND TIME(EnterDate) < '13:00:00' THEN Amount ELSE 0 END) AS Visitors_12_13, " +
				    "SUM(CASE WHEN TIME(EnterDate) >= '13:00:00' AND TIME(EnterDate) < '14:00:00' THEN Amount ELSE 0 END) AS Visitors_13_14, " +
				    "SUM(CASE WHEN TIME(EnterDate) >= '14:00:00' AND TIME(EnterDate) < '15:00:00' THEN Amount ELSE 0 END) AS Visitors_14_15, " +
				    "SUM(CASE WHEN TIME(EnterDate) >= '15:00:00' AND TIME(EnterDate) <= '16:00:00' THEN Amount ELSE 0 END) AS Visitors_15_16 " +
				    "FROM ( " +
				    "SELECT EnterDate, ExitDate, Amount, OrderType " +
				    "FROM occasionalvisits " +
				    "WHERE parkId = ? AND MONTH(EnterDate) = ? AND YEAR(EnterDate) = ? " +
				    "UNION ALL " +
				    "SELECT EnterDate, ExitDate, Amount, OrderType " +
				    "FROM preorders " +
				    "WHERE parkId = ? AND MONTH(EnterDate) = ? AND YEAR(EnterDate) = ?" +
				    ") AS combined " +
				    "GROUP BY OrderType;"
				);

			stmt.setInt(1, report.getRequestedPark().getParkId());
			stmt.setInt(2, report.getMonth());
			stmt.setInt(3, report.getYear());
			stmt.setInt(4, report.getRequestedPark().getParkId());
			stmt.setInt(5, report.getMonth());
			stmt.setInt(6, report.getYear());

			ResultSet rs = stmt.executeQuery();
			if (!rs.next())
				return false;
			HashMap<Integer, ArrayList<Integer>> totalVisitsByEnterTime = new HashMap<Integer, ArrayList<Integer>>();
			rs.previous();
			while (rs.next()) {
				ArrayList<Integer> data = new ArrayList<Integer>();
				String orderType = rs.getString(1);
				int indexToAdd = (orderType.equals("Solo") ? 0
						: orderType.equals("Solo Preorder") ? 1
								: orderType.equals("Family") ? 2
										: orderType.equals("Family Preorder") ? 3
												: orderType.equals("Group") ? 4
														: orderType.equals("Group Preorder") ? 5 : 6);

				data.add(rs.getInt(2));
				data.add(rs.getInt(3));
				data.add(rs.getInt(4));
				data.add(rs.getInt(5));
				data.add(rs.getInt(6));
				data.add(rs.getInt(7));
				data.add(rs.getInt(8));
				data.add(rs.getInt(9));

				totalVisitsByEnterTime.put(indexToAdd, data);
			}

			report.setTotalVisitsEnterByTime(totalVisitsByEnterTime);

			return true;
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
			return false;
		}
	}

	private boolean getParkIdleVisitTimeSummary(VisitsReport report) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT "
				    + "SUM(CASE WHEN (OrderType='Solo' OR OrderType='Solo Preorder') AND Duration='0-1' THEN Amount ELSE 0 END) AS TotalSolo0_1, "
				    + "SUM(CASE WHEN (OrderType='Family' OR OrderType='Family Preorder') AND Duration='0-1' THEN Amount ELSE 0 END) AS TotalFamily0_1, "
				    + "SUM(CASE WHEN (OrderType='Group' OR OrderType='Group Preorder') AND Duration='0-1' THEN Amount ELSE 0 END) AS TotalGroup0_1, "
				    + "SUM(CASE WHEN (OrderType='Solo' OR OrderType='Solo Preorder') AND Duration='1-2' THEN Amount ELSE 0 END) AS TotalSolo1_2, "
				    + "SUM(CASE WHEN (OrderType='Family' OR OrderType='Family Preorder') AND Duration='1-2' THEN Amount ELSE 0 END) AS TotalFamily1_2, "
				    + "SUM(CASE WHEN (OrderType='Group' OR OrderType='Group Preorder') AND Duration='1-2' THEN Amount ELSE 0 END) AS TotalGroup1_2, "
				    + "SUM(CASE WHEN (OrderType='Solo' OR OrderType='Solo Preorder') AND Duration='2-3' THEN Amount ELSE 0 END) AS TotalSolo2_3, "
				    + "SUM(CASE WHEN (OrderType='Family' OR OrderType='Family Preorder') AND Duration='2-3' THEN Amount ELSE 0 END) AS TotalFamily2_3, "
				    + "SUM(CASE WHEN (OrderType='Group' OR OrderType='Group Preorder') AND Duration='2-3' THEN Amount ELSE 0 END) AS TotalGroup2_3, "
				    + "SUM(CASE WHEN (OrderType='Solo' OR OrderType='Solo Preorder') AND Duration='3-4' THEN Amount ELSE 0 END) AS TotalSolo3_4, "
				    + "SUM(CASE WHEN (OrderType='Family' OR OrderType='Family Preorder') AND Duration='3-4' THEN Amount ELSE 0 END) AS TotalFamily3_4, "
				    + "SUM(CASE WHEN (OrderType='Group' OR OrderType='Group Preorder') AND Duration='3-4' THEN Amount ELSE 0 END) AS TotalGroup3_4, "
				    + "SUM(CASE WHEN (OrderType='Solo' OR OrderType='Solo Preorder') AND Duration='4+' THEN Amount ELSE 0 END) AS TotalSolo4, "
				    + "SUM(CASE WHEN (OrderType='Family' OR OrderType='Family Preorder') AND Duration='4+' THEN Amount ELSE 0 END) AS TotalFamily4, "
				    + "SUM(CASE WHEN (OrderType='Group' OR OrderType='Group Preorder') AND Duration='4+' THEN Amount ELSE 0 END) AS TotalGroup4 "
				    + "FROM ( "
				    + "SELECT OrderId, EnterDate, ExitDate, Amount, OrderType, "
				    + "CASE "
				    + "WHEN TIMEDIFF(ExitDate, EnterDate) BETWEEN '00:00:00' AND '01:00:00' THEN '0-1' "
				    + "WHEN TIMEDIFF(ExitDate, EnterDate) BETWEEN '01:00:01' AND '02:00:00' THEN '1-2' "
				    + "WHEN TIMEDIFF(ExitDate, EnterDate) BETWEEN '02:00:01' AND '03:00:00' THEN '2-3' "
				    + "WHEN TIMEDIFF(ExitDate, EnterDate) BETWEEN '03:00:01' AND '04:00:00' THEN '3-4' "
				    + "ELSE '4+' "
				    + "END AS Duration "
				    + "FROM preorders "
				    + "WHERE parkId = ? AND MONTH(EnterDate) = ? AND YEAR(EnterDate) = ? "
				    + "UNION ALL "
				    + "SELECT OrderId, EnterDate, ExitDate, Amount, OrderType, "
				    + "CASE "
				    + "WHEN TIMEDIFF(ExitDate, EnterDate) BETWEEN '00:00:00' AND '01:00:00' THEN '0-1' "
				    + "WHEN TIMEDIFF(ExitDate, EnterDate) BETWEEN '01:00:01' AND '02:00:00' THEN '1-2' "
				    + "WHEN TIMEDIFF(ExitDate, EnterDate) BETWEEN '02:00:01' AND '03:00:00' THEN '2-3' "
				    + "WHEN TIMEDIFF(ExitDate, EnterDate) BETWEEN '03:00:01' AND '04:00:00' THEN '3-4' "
				    + "ELSE '4+' "
				    + "END AS Duration "
				    + "FROM occasionalvisits "
				    + "WHERE parkId = ? AND MONTH(EnterDate) = ? AND YEAR(EnterDate) = ? "
				    +") AS subquery;");
			
			
			stmt.setInt(1, report.getRequestedPark().getParkId());
			stmt.setInt(2, report.getMonth());
			stmt.setInt(3, report.getYear());
			stmt.setInt(4, report.getRequestedPark().getParkId());
			stmt.setInt(5, report.getMonth());
			stmt.setInt(6, report.getYear());
			
			ResultSet rs = stmt.executeQuery();
			if(!rs.next())
				return false;
			HashMap<Integer,ArrayList<Integer>> totalIdleTimeByGap= new HashMap<Integer, ArrayList<Integer>>();
			
			for(int i=0,j=0;i<15;i+=3,j+=1) {
				ArrayList<Integer> data= new ArrayList<Integer>();
				data.add(rs.getInt(i+1));
				data.add(rs.getInt(i+2));
				data.add(rs.getInt(i+3));
				totalIdleTimeByGap.put(j, data);
			}
			
			report.setTotalIdleTimeByGap(totalIdleTimeByGap);

			return true;
		} catch (SQLException ex) {
//			serverController.printToLogConsole("Query search for user failed");
				return false;
			}
	}

	private boolean insertGeneratedCancellationsReportToDatabase(CancellationsReport report) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"INSERT INTO cancellationsreports (parkId, year, month, pdfblob) \r\n" + "VALUES (?, ?, ?, ?)");

			stmt.setInt(1, report.getRequestedPark().getParkId());
			stmt.setInt(2, report.getYear());
			stmt.setInt(3, report.getMonth());
			stmt.setBytes(4, report.getBlobPdfContent());

			int rs = stmt.executeUpdate();

			// if the query ran successfully, but returned as empty table.
			if (rs == 0) {
				return false;
			}

			return true;
		} catch (SQLException ex) {
//		serverController.printToLogConsole("Query search for user failed");
			return false;
		}
	}

	private boolean insertVisitsAmountReportToDatabase(VisitsReport report) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"INSERT INTO visitsreport (parkId, year, month, pdfblob) \r\n" + "VALUES (?, ?, ?, ?)");
 
			stmt.setInt(1, report.getRequestedPark().getParkId());
			stmt.setInt(2, report.getYear());
			stmt.setInt(3, report.getMonth());
			stmt.setBytes(4, report.getBlobPdfContent());

			int rs = stmt.executeUpdate();

			// if the query ran successfully, but returned as empty table.
			if (rs == 0) {
				return false;
			}

			return true;
		} catch (SQLException ex) {
//		serverController.printToLogConsole("Query search for user failed");
			return false;
		}
	}

	public byte[] getRequestedCancellationsReport(CancellationsReport report) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"SELECT PdfBlob FROM cancellationsreports WHERE Year = ? AND Month = ? AND ParkId = ?");

			stmt.setInt(1, report.getYear());
			stmt.setInt(2, report.getMonth());
			stmt.setInt(3, report.getRequestedPark().getParkId());

			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) {
				return null;
			}
			// retrieve the Blob from the database
			Blob pdfBlob = rs.getBlob(1);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[50000];
			try (InputStream in = pdfBlob.getBinaryStream()) {
				int n;
				while ((n = in.read(buf)) > 0) {
					baos.write(buf, 0, n);
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

			byte[] pdfBytes = baos.toByteArray();
			return pdfBytes;
		} catch (SQLException ex) {
			return null;
		}

	}
	
	public byte[] getRequestedVisitsReport(VisitsReport report) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"SELECT PdfBlob FROM visitsreport WHERE Year = ? AND Month = ? AND ParkId = ?");

			stmt.setInt(1, report.getYear());
			stmt.setInt(2, report.getMonth());
			stmt.setInt(3, report.getRequestedPark().getParkId());

			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) {
				return null;
			}
			// retrieve the Blob from the database
			Blob pdfBlob = rs.getBlob(1);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[50000];
			try (InputStream in = pdfBlob.getBinaryStream()) {
				int n;
				while ((n = in.read(buf)) > 0) {
					baos.write(buf, 0, n);
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

			byte[] pdfBytes = baos.toByteArray();
			return pdfBytes;
		} catch (SQLException ex) {
			return null;
		}
	}

}
