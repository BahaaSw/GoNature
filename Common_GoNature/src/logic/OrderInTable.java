package logic;

import javafx.beans.property.SimpleStringProperty;

public class OrderInTable {
	private SimpleStringProperty orderId;
	private SimpleStringProperty amountOfVisitors;
	private SimpleStringProperty ownerPhone;
	private SimpleStringProperty estimatedEnterTime;
	private SimpleStringProperty estimatedExitTime;
	private SimpleStringProperty actualEnterTime;
	private SimpleStringProperty timeLeftInPark;

	public OrderInTable(String orderId, String amountOfVisitors, String ownerPhone, String estimatedEnterTime,
			String estimatedExitTime, String actualEnterTime, String timeLeftInPark) {
		this.orderId = new SimpleStringProperty(orderId);
		this.amountOfVisitors =  new SimpleStringProperty(amountOfVisitors);
		this.ownerPhone =  new SimpleStringProperty(ownerPhone);
		this.estimatedEnterTime =  new SimpleStringProperty(estimatedEnterTime);
		this.estimatedExitTime =  new SimpleStringProperty(estimatedExitTime);
		this.actualEnterTime =  new SimpleStringProperty(actualEnterTime);
		this.timeLeftInPark =  new SimpleStringProperty(timeLeftInPark);
	}

	public String getTimeLeftInPark() {
		return timeLeftInPark.get();
	}

	public void setTimeLeftInPark(String timeLeftInPark) {
		this.timeLeftInPark.set(timeLeftInPark);
	}

	public String getOrderId() {
		return orderId.get();
	}

	public void setOrderId(String orderId) {
		this.orderId.set(orderId);
	}

	public String getAmountOfVisitors() {
		return amountOfVisitors.get();
	}

	public void setAmountOfVisitors(String amountOfVisitors) {
		this.amountOfVisitors.set(amountOfVisitors);
	}

	public String getOwnerPhone() {
		return ownerPhone.get();
	}

	public void setOwnerPhone(String ownerPhone) {
		this.ownerPhone.set(ownerPhone);
	}

	public String getEstimatedEnterTime() {
		return estimatedEnterTime.get();
	}

	public void setEstimatedEnterTime(String estimatedEnterTime) {
		this.estimatedEnterTime.set(estimatedEnterTime);
	}

	public String getEstimatedExitTime() {
		return estimatedExitTime.get();
	}

	public void setEstimatedExitTime(String estimatedExitTime) {
		this.estimatedExitTime.set(estimatedExitTime);
	}

	public String getActualEnterTime() {
		return actualEnterTime.get();
	}

	public void setActualEnterTime(String actualEnterTime) {
		this.actualEnterTime.set(actualEnterTime);
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.getOrderId().equals(((OrderInTable)obj).getOrderId());
	}

}
