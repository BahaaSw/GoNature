package jdbc.query;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;


import jdbc.MySqlConnection;
import logic.AmountDivisionReport;
import logic.CancellationsReport;
import logic.ParkAmountSummary;
import logic.ParkDailySummary;
import logic.ParkFullDaySummary;
import logic.UsageReport;
import logic.VisitsReport;
import utils.ReportGenerator;
import utils.enums.ParkNameEnum;

/**
 * The class provides methods to interact with the database
 * for generating and retrieving various types of reports related to park usage,
 * cancellations, and visitations.
 * 
 * @Author NadavReubens
 * @Author Gal Bitton
 * @Author Tamer Amer
 * @Author Rabea Lahham
 * @Author Bahaldeen Swied
 * @Author Ron Sisso
 * @version 1.0.0 
 */

public class ReportsQueries {
	
    /**
     * Retrieves a summary of daily activities for a specific park on a given day.
     * 
     * @param day The day of the month.
     * @param month The month.
     * @param parkId The identifier of the park.
     * @return {@link ParkDailySummary} containing the summary of daily activities.
     */

	public ParkDailySummary getParkDailySummaryByDay(int day,int month, int parkId) { 
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
	
    /**
     * Determines if the park was full at any point during each day of a given month and hour.
     * 
     * @param month The month to check.
     * @param hour The hour to check.
     * @param year The year to check.
     * @param park The name of the park to check.
     * @return {@link ParkFullDaySummary} containing summary information for the park's full status.
     */
	public ParkFullDaySummary getIfParkWasFullEachDay(int month,int hour, int year, ParkNameEnum park) //added by tamir
	{
		ParkFullDaySummary currentDaySummary = new ParkFullDaySummary();
		String parkColumnName = park.name();
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			
			PreparedStatement stmt = con.prepareStatement("SELECT " +parkColumnName +" FROM parkfulldatetime WHERE Month=? AND Hour(Hour)=? AND year=?;");
	        stmt.setInt(1, month);
	        stmt.setInt(2, hour);
	        stmt.setInt(3, year);
	        ResultSet rs = stmt.executeQuery();
	        
	        if(!rs.next())
	        {
	             currentDaySummary = new ParkFullDaySummary();
	             currentDaySummary.setHour(hour);
	             currentDaySummary.setTimesFullInSpecificHour(0);
	             currentDaySummary.setPark(park);
	             return currentDaySummary;
	        }
	        
            currentDaySummary = new ParkFullDaySummary();
            currentDaySummary.setHour(hour);
            currentDaySummary.setTimesFullInSpecificHour(rs.getInt(1));
            currentDaySummary.setPark(park);
         
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return currentDaySummary;
	}
	
    /**
     * Generates a usage report for a park, including details on when the park was full.
     * 
     * @param report The {@link UsageReport} object containing report parameters.
     * @return {@code true} if the report was successfully generated; {@code false} otherwise.
     */
	public boolean generateUsageReport(UsageReport report) {

		int year = report.getYear();
		int month = report.getMonth();

		HashMap<Integer, ParkFullDaySummary> parkSummaryByDays = new HashMap<Integer, ParkFullDaySummary>();

		for (int hour = 8; hour < 21; hour++) 
		{
			ParkFullDaySummary temp = getIfParkWasFullEachDay(month,hour,year,report.getRequestedPark());
			if (temp != null)
			{
				parkSummaryByDays.put(hour, temp);
			}
		}
		if (parkSummaryByDays.isEmpty())
			return false;

		report.setReportData(parkSummaryByDays);
		report.setBlobPdfContent(ReportGenerator.generateUsageReportAsPdfBlob(report));

		if (insertGeneratedUsageReportToDatabase(report))
			return true;

		return false;

	}
	
    /**
     * Inserts a generated usage report into the database.
     *
     * @param report The UsageReport object to be inserted.
     * @return true if the report was successfully inserted, false otherwise.
     */
	private boolean insertGeneratedUsageReportToDatabase(UsageReport report) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"INSERT INTO usagereport (parkId, year, month, pdfblob) \r\n" + "VALUES (?, ?, ?, ?)");

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
	
    /**
     * Retrieves the binary content of a requested usage report.
     *
     * @param report The UsageReport object containing the identifiers for the requested report.
     * @return A byte array containing the report's PDF content, or null if not found.
     */
	public byte[] getRequestedUsageReport(UsageReport report) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"SELECT pdfblob FROM usagereport WHERE Year = ? AND Month = ? AND ParkId = ?");

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
	
    /**
     * Generates a report detailing the amounts divided by order type for a specific park in a chosen month.
     *
     * @param month The month of interest.
     * @param parkId The ID of the park.
     * @param year The year of interest.
     * @return A ParkAmountSummary object containing the sums of different order types.
     */
	public ParkAmountSummary getAmountDivisionByOrderTypeInChoosenMonth(int month, int parkId,int year) {
				ParkAmountSummary parkAmountSum = new ParkAmountSummary();
				try {
					Connection con = MySqlConnection.getInstance().getConnection();
					PreparedStatement stmt = con
							.prepareStatement("SELECT\n"
									+ "    SUM(ParkSolo) AS TotalSolo,\n"
									+ "    SUM(ParkFamily) AS TotalFamily,\n"
									+ "    SUM(ParkGroup) AS TotalGroup\n"
									+ "FROM (\n"
									+ "    SELECT\n"
									+ "        SUM(CASE WHEN parkId = ? AND OrderType='Solo' THEN Amount ELSE 0 END) AS ParkSolo,\n"
									+ "        SUM(CASE WHEN parkId = ? AND OrderType='Family' THEN Amount ELSE 0 END) AS ParkFamily,\n"
									+ "        SUM(CASE WHEN parkId = ? AND OrderType='Group' THEN Amount ELSE 0 END) AS ParkGroup\n"
									+ "    FROM\n"
									+ "        occasionalvisits\n"
									+ "    WHERE\n"
									+ "        OrderStatus = 'Completed' \n"
									+ "        AND MONTH(EnterDate) = ?\n"
									+ "        AND YEAR(EnterDate) = ?\n"
									+ "    UNION ALL\n"
									+ "    SELECT\n"
									+ "        SUM(CASE WHEN parkId = ? AND OrderType='Solo Preorder' THEN Amount ELSE 0 END) AS ParkSolo,\n"
									+ "        SUM(CASE WHEN parkId = ? AND OrderType='Family Preorder' THEN Amount ELSE 0 END) AS ParkFamily,\n"
									+ "        SUM(CASE WHEN parkId = ? AND OrderType='Group Preorder' THEN Amount ELSE 0 END) AS ParkGroup\n"
									+ "\n"
									+ "    FROM\n"
									+ "        preorders\n"
									+ "    WHERE\n"
									+ "        OrderStatus = 'Completed' \n"
									+ "        AND MONTH(EnterDate) = ?\n"
									+ "        AND YEAR(EnterDate) = ?\n"
									+ ") AS subquery;");

					stmt.setInt(1, parkId);
					stmt.setInt(2, parkId);
					stmt.setInt(3, parkId);
					stmt.setInt(4, month);
					stmt.setInt(5, year);
					stmt.setInt(6, parkId);
					stmt.setInt(7, parkId);
					stmt.setInt(8, parkId);
					stmt.setInt(9, month);
					stmt.setInt(10, year);
					
					ResultSet rs = stmt.executeQuery();

					// if the query ran successfully, but returned as empty table.
					if (!rs.next()) {
						parkAmountSum.setAmountGroup(0);
						parkAmountSum.setAmountFamily(0);
						parkAmountSum.setAmountSolo(0);
						parkAmountSum.setMonth(month);
						parkAmountSum.setYear(year);
						parkAmountSum.setPark(ParkNameEnum.fromParkId(parkId));
						return parkAmountSum;
					}
					parkAmountSum.setAmountSolo(rs.getInt(1));
					parkAmountSum.setAmountFamily(rs.getInt(2));
					parkAmountSum.setAmountGroup(rs.getInt(3));
					parkAmountSum.setMonth(month);
					parkAmountSum.setYear(year);
					parkAmountSum.setPark(ParkNameEnum.fromParkId(parkId));
					return parkAmountSum;

				} catch (SQLException ex) {
//				serverController.printToLogConsole("Query search for user failed");
					return null;
				}

			}
	
    /**
     * Generates and stores a cancellations report for a park.
     *
     * @param report The CancellationsReport object containing details necessary for the report.
     * @return true if the report was successfully generated and saved, false otherwise.
     */
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
	
    /**
     * Generates and stores a visits report for a park.
     *
     * @param report The VisitsReport object containing details necessary for the report.
     * @return true if the report was successfully generated and saved, false otherwise.
     */
	public boolean generateVisitsReport(VisitsReport report) {
		boolean isQuerySucceed = getParkVisitsSummaryByEnterTime(report);
		if (!isQuerySucceed)
			return false;

		isQuerySucceed = getParkIdleVisitTimeSummary(report);
		if (!isQuerySucceed)
			return false;

		report.setBlobPdfContent(ReportGenerator.generateVisitsReportAsPdf(report));

		if (insertVisitsAmountReportToDatabase(report))
			return true;
		return false;
	}
	
    /**
     * Generates a total amount division report for a specific park and month, detailing the division
     * of visitor amounts by order type. The report is generated as a PDF and stored in the database.
     *
     * @param report The AmountDivisionReport object containing the necessary parameters for generating the report,
     *               such as the year, month, and parkId. It is expected to be populated with report data and a PDF blob.
     * @return true if the report was successfully generated and saved; false otherwise. This involves generating
     *         the park amount summary, converting it into a PDF, and inserting the report into the database.
     */
	public boolean generateTotalAmountDivisionReport(AmountDivisionReport report) {
		int year = report.getYear();
		int month = report.getMonth();
		int parkId = report.getRequestedPark().getParkId();
		ParkAmountSummary parkAmountSum=new ParkAmountSummary();

		parkAmountSum = getAmountDivisionByOrderTypeInChoosenMonth(month, parkId,year);
			
		if (parkAmountSum==null)
			return false;

		report.setReportData(parkAmountSum);
		report.setBlobPdfContent(ReportGenerator.generateTotalVisitorsAmountReportAsPdf(report));

		if (insertTotalAmountReportToDatabase(report))
			return true;

		return false;

	}
	
    /**
     * Gathers data on park visits by order type and enter time, categorizing the totals into hourly segments.
     * 
     * @param report The VisitsReport to populate with data.
     * @return true if data was successfully retrieved and false otherwise.
     */
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
	
    /**
     * Gathers data on the idle visit time in the park, organizing it by order type and visit duration.
     * 
     * @param report The VisitsReport to populate with data.
     * @return true if data was successfully retrieved and false otherwise.
     */
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
	
    /**
     * Inserts a generated cancellations report into the database.
     * 
     * @param report The CancellationsReport containing the generated report.
     * @return true if the insertion was successful, false otherwise.
     */
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
	
    /**
     * Inserts a generated visits amount report into the database.
     * 
     * @param report The VisitsReport containing the generated report.
     * @return true if the insertion was successful, false otherwise.
     */
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
	
    /**
     * Inserts a generated total amount report into the database.
     * 
     * @param report The AmountDivisionReport containing the generated report.
     * @return true if the insertion was successful, false otherwise.
     */
	private boolean insertTotalAmountReportToDatabase(AmountDivisionReport report) {
		try {
			Connection con = MySqlConnection.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"INSERT INTO totalvisitorsreport (parkId, year, month, pdfblob) \r\n" + "VALUES (?, ?, ?, ?)");
 
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
	
    /**
     * Retrieves the binary content of a requested cancellations report.
     * 
     * @param report The CancellationsReport object containing identifiers for the requested report.
     * @return A byte array containing the report's PDF content, or null if not found.
     */
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
	
    /**
     * Retrieves the binary content of a requested visits report.
     * 
     * @param report The VisitsReport object containing identifiers for the requested report.
     * @return A byte array containing the report's PDF content, or null if not found.
     */
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
	
    /**
     * Retrieves the binary content of a requested total amount report.
     * 
     * @param report The AmountDivisionReport object containing identifiers for the requested report.
     * @return A byte array containing the report's PDF content, or null if not found.
     */
	public byte[] getRequestedTotalAmountReport(AmountDivisionReport report) {
			try {
				Connection con = MySqlConnection.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(
						"SELECT PdfBlob FROM totalvisitorsreport WHERE Year = ? AND Month = ? AND ParkId = ?");

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
