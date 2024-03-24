package logic;

import java.io.Serializable;

import utils.enums.ParkNameEnum;

public class ParkFullDaySummary implements Serializable {

	private int day;
	private boolean isfull;
	private ParkNameEnum park;
	
	public ParkFullDaySummary() {};

	public ParkFullDaySummary(int day, boolean isfull, ParkNameEnum park) {
		super();
		this.day = day;
		this.isfull = isfull;
		this.park = park;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public boolean isIsfull() {
		return isfull;
	}

	public void setIsfull(boolean isfull) {
		this.isfull = isfull;
	}

	public ParkNameEnum getPark() {
		return park;
	}

	public void setPark(ParkNameEnum park) {
		this.park = park;
	}

}
