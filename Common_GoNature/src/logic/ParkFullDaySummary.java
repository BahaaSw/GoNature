package logic;

import java.io.Serializable;

import utils.enums.ParkNameEnum;

public class ParkFullDaySummary implements Serializable {

	private int hour;
	private int timesFullInSpecificHour;
	private ParkNameEnum park;
	
	public ParkFullDaySummary() {};


	public ParkFullDaySummary(int hour, int timesFullInSpecificHour, ParkNameEnum park) {
		super();
		this.hour = hour;
		this.timesFullInSpecificHour = timesFullInSpecificHour;
		this.park = park;
	}


	public ParkNameEnum getPark() {
		return park;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getTimesFullInSpecificHour() {
		return timesFullInSpecificHour;
	}

	public void setTimesFullInSpecificHour(int timesFullInSpecificHour) {
		this.timesFullInSpecificHour = timesFullInSpecificHour;
	}

	public void setPark(ParkNameEnum park) {
		this.park = park;
	}

}