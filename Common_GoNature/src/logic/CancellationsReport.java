package logic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import utils.enums.ParkNameEnum;
import utils.enums.ReportType;

public class CancellationsReport extends Report implements Serializable {
	private HashMap<Integer, ParkDailySummary> reportData;
	private double averageCancels = 0;
	private double medianCancels = 0;

	public CancellationsReport(ReportType reportType) {
		super(reportType);
		// TODO Auto-generated constructor stub
	}

	public CancellationsReport(int month, int year, ParkNameEnum park) {
		super(ReportType.CancellationsReport);
		this.month = month;
		this.year = year;
		this.requestedPark = park;
	}

	public void showReport() {
	}

	public HashMap<Integer, ParkDailySummary> getReportData() {
		return reportData;
	}

	public void setReportData(HashMap<Integer, ParkDailySummary> reportData) {
		this.reportData = reportData;

		double sum = 0;
		for (ParkDailySummary summary : reportData.values()) {
			sum += summary.getCancelsOrders();
		}
		setAverageCancels(sum / reportData.size());

		List<Integer> cancelsList = reportData.values().stream().map(ParkDailySummary::getCancelsOrders).sorted()
				.collect(Collectors.toList());

		int size = cancelsList.size();
		if (size % 2 == 0) {
			setMedianCancels((cancelsList.get(size / 2 - 1) + cancelsList.get(size / 2)) / 2.0);
		} else {
			setMedianCancels(cancelsList.get(size / 2));
		}

	}

	public double getAverageCancels() {
		return averageCancels;
	}

	public void setAverageCancels(double averageCancels) {
		this.averageCancels = averageCancels;
	}

	public double getMedianCancels() {
		return medianCancels;
	}

	public void setMedianCancels(double medianCancels) {
		this.medianCancels = medianCancels;
	}


}

