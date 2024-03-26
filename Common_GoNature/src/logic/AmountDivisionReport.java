package logic;

import java.io.Serializable;

import utils.enums.ParkNameEnum;
import utils.enums.ReportType;

public class AmountDivisionReport extends Report implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5951223164371041856L;
	private ParkAmountSummary reportData;
	private byte[] blobPdfContent;

	public AmountDivisionReport(ReportType reportType) {
		super(reportType);
		// TODO Auto-generated constructor stub
	}

	public AmountDivisionReport(int month, int year, ParkNameEnum park) {
		super(ReportType.TotalVisitorsReport);
		this.month = month;
		this.year = year;
		this.requestedPark = park;
	}

	public ParkAmountSummary getReportData() {
		return reportData;
	}

	public void setReportData(ParkAmountSummary reportData) {
		this.reportData = reportData;
	}

	public byte[] getBlobPdfContent() {
		return blobPdfContent;
	}
	
	public void setBlobPdfContent(byte[] blobPdfContent) {
		this.blobPdfContent=blobPdfContent;
	}
}
