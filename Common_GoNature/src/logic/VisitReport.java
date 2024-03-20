package logic;

import java.io.Serializable;

import utils.enums.ParkNameEnum;
import utils.enums.ReportType;

public class VisitReport extends Report implements Serializable{
	
	protected ParkNameEnum park;
	
	public VisitReportDailySummary[] VisitReportData = new VisitReportDailySummary[8];
	private byte[] blobPdfContent;
	
	public VisitReport()
	{
		super(ReportType.VisitsReports);
	}
	

	public ParkNameEnum getPark() {
		return park;
	}

	public void setPark(ParkNameEnum park) {
		this.park = park;
	}


	public byte[] getBlobPdfContent() {
		return blobPdfContent;
	}

	public void setBlobPdfContent(byte[] blobPdfContent) {
		this.blobPdfContent = blobPdfContent;
	}
	
	public VisitReportDailySummary[] getVisitReportData() {
		return VisitReportData;
	}

	public void setVisitReportData(VisitReportDailySummary[] visitReportData) {
		VisitReportData = visitReportData;
	}
	

}
