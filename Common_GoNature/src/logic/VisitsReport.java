package logic;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import utils.enums.ParkNameEnum;
import utils.enums.ReportType;

public class VisitsReport extends Report implements Serializable{

	private HashMap<Integer, ArrayList<Integer>> totalVisitsByEnterTime;
	private HashMap<Integer,ArrayList<Integer>> totalIdleTimeByGap;
	
	public VisitsReport(ReportType reportType) {
		super(reportType);
	}
	
	public VisitsReport(int month,int year,ParkNameEnum requestedPark) {
		super(ReportType.VisitsReports);
		this.requestedPark=requestedPark;
		this.month=month;
		this.year=year;
	}
	
	public HashMap<Integer, ArrayList<Integer>> getTotalVisitsByEnterTime() {
		return totalVisitsByEnterTime;
	}

	public void setTotalVisitsEnterByTime(HashMap<Integer, ArrayList<Integer>> totalVisitsByEnterTime) {
		this.totalVisitsByEnterTime = totalVisitsByEnterTime;
	}

	public HashMap<Integer, ArrayList<Integer>> getTotalIdleTimeByGap() {
		return totalIdleTimeByGap;
	}

	public void setTotalIdleTimeByGap(HashMap<Integer, ArrayList<Integer>> totalIdleTimeByGap) {
		this.totalIdleTimeByGap = totalIdleTimeByGap;
	}
	
	public ArrayList<Integer> getIdleTimeData(int index){
		return totalIdleTimeByGap.get(index);
	}
	
	public ArrayList<Integer> getEnterTimeData(int index){
		return totalVisitsByEnterTime.get(index);
	}



}
