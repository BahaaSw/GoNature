package logic;

import java.io.Serializable;

import utils.enums.ParkNameEnum;

public class ParkDailySummary implements Serializable{


	private int day;
	private int cancelsOrders;
	private int timePassedOrders;
	private int totalOrders;
	private ParkNameEnum park;
	
	public ParkDailySummary(int day, int cancelsOrders, int timePassedOrders, int totalOrders, ParkNameEnum park) {
		super();
		this.day = day;
		this.cancelsOrders = cancelsOrders;
		this.timePassedOrders = timePassedOrders;
		this.totalOrders = totalOrders;
		this.park = park;
	}
	
	
	public ParkDailySummary() {}
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getCancelsOrders() {
		return cancelsOrders;
	}
	public void setCancelsOrders(int cancelsOrders) {
		this.cancelsOrders = cancelsOrders;
	}
	public int getTimePassedOrders() {
		return timePassedOrders;
	}
	public void setTimePassedOrders(int timePassedOrders) {
		this.timePassedOrders = timePassedOrders;
	}
	public int getTotalOrders() {
		return totalOrders;
	}
	public void setTotalOrders(int totalOrders) {
		this.totalOrders = totalOrders;
	}
	public ParkNameEnum getPark() {
		return park;
	}
	public void setPark(ParkNameEnum park) {
		this.park = park;
	}
	
	public void BuildReportAsBlob() {}
	
}
