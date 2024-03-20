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
import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import jdbc.DatabaseResponse;
import jdbc.MySqlConnection;
import logic.CancellationsReport;
import logic.ParkDailySummary;
import logic.VisitReport;
import logic.VisitReportDailySummary;
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
		report.generateReportAsPdfBlob();

		if (insertGeneratedCancellationsReportToDatabase(report))
			return true;

		return false;

	}
	
	public VisitReportDailySummary getVisitReportDailySummary(int month, int day, int parkId) {return null;}
	


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

}
