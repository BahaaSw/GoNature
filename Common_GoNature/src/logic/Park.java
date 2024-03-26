package logic;

import java.io.Serializable;

import utils.enums.ParkNameEnum;

public class Park implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6759762004224132621L;
	private int parkId;
	private ParkNameEnum parkName;
	private int currentMaxCapacity;
	private int currentInPark;
	private int currentEstimatedStayTime;
	private int currentEstimatedReservedSpots; // the gap between max capacity, to amount of orders we let order
	private double parkEntranceFee;
	private String district;
	private int price; 

	
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Park(int parkId) {
		this.parkId=parkId;
	}
	
	public Park(int parkId, ParkNameEnum parkName,int currentMaxCapacity,int currentInPark,int currentEstimatedStayTime,int currentEstimatedReservedSpots) {
		this.parkId=parkId;
		this.parkName=parkName;
		this.currentMaxCapacity=currentMaxCapacity;
		this.currentInPark=currentInPark;
		this.currentEstimatedStayTime=currentEstimatedStayTime;
		this.currentEstimatedReservedSpots=currentEstimatedReservedSpots;
	}
	
	public Park(int parkId,ParkNameEnum parkName) {
		this.parkId=parkId;
		this.parkName=parkName;
	}

	public int getCurrentInPark() {
		return currentInPark;
	}

	public void setCurrentInPark(int currentInPark) {
		this.currentInPark = currentInPark;
	}
	public int getParkId() {
		return parkId;
	}

	public void setParkId(int parkId) {
		this.parkId = parkId;
	}

	public ParkNameEnum getParkName() {
		return parkName;
	}

	public void setParkName(ParkNameEnum parkName) {
		this.parkName = parkName;
	}

	public int getCurrentMaxCapacity() {
		return currentMaxCapacity;
	}

	public void setCurrentMaxCapacity(int currentMaxCapacity) {
		this.currentMaxCapacity = currentMaxCapacity;
	}

	public int getCurrentEstimatedStayTime() {
		return currentEstimatedStayTime;
	}

	public void setCurrentEstimatedStayTime(int currentEstimatedStayTime) {
		this.currentEstimatedStayTime = currentEstimatedStayTime;
	}

	public int getCurrentEstimatedReservedSpots() {
		return currentEstimatedReservedSpots;
	}

	public void setCurrentEstimatedReservedSpots(int currentEstimatedReservedSpots) {
		this.currentEstimatedReservedSpots = currentEstimatedReservedSpots;
	}

	public double getParkEntranceFee() {
		return parkEntranceFee;
	}

	public void setParkEntranceFee(double parkEntranceFee) {
		this.parkEntranceFee = parkEntranceFee;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}
}