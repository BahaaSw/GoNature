package logic;

import java.io.Serializable;
import java.util.HashMap;

import utils.enums.ParkNameEnum;
import utils.enums.ReportType;

public class UsageReport extends Report implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5930390679218481702L;
	private HashMap<Integer, ParkFullDaySummary> reportData;
	private ParkFullDaySummary currentDaySummary;

	public UsageReport(ReportType reportType) {
		super(reportType);
	}
	
	public UsageReport(int month,int year,ParkNameEnum park)
	{
		super(ReportType.UsageReport);
		this.month = month;
		this.year = year;
		this.requestedPark = park;
	}

	public HashMap<Integer, ParkFullDaySummary> getReportData() {
		return reportData;
	}

	public void setReportData(HashMap<Integer, ParkFullDaySummary> reportData) {
		this.reportData = reportData;
	}

	public ParkFullDaySummary getCurrentDaySummary() {
		return currentDaySummary;
	}

	public void setCurrentDaySummary(ParkFullDaySummary currentDaySummary) {
		this.currentDaySummary = currentDaySummary;
	}

}