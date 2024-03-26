package logic;

import java.io.Serializable;

import utils.enums.ParkNameEnum;
import utils.enums.ReportType;

public class Report implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6207770687406496946L;
	protected ReportType reportType;
	protected ParkNameEnum requestedPark;
	protected int month;
	protected int year;
	protected byte[] blobPdfContent;
	
	public Report(ReportType reportType) {
		this.reportType=reportType;
	}
	
	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public ParkNameEnum getRequestedPark() {
		return requestedPark;
	}

	public void setRequestedPark(ParkNameEnum requestedPark) {
		this.requestedPark = requestedPark;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public byte[] getBlobPdfContent() {
		return blobPdfContent;
	}
	
	public void setBlobPdfContent(byte[] content) {
		blobPdfContent=content;
	}
	
}
