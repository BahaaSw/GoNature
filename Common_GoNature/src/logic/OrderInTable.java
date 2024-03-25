package logic;

import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;

public class OrderInTable implements Serializable{
	private SimpleStringProperty orderId;
	private SimpleStringProperty isPaid;
	private SimpleStringProperty amountOfVisitors;
	private SimpleStringProperty ownerPhone;
	private SimpleStringProperty estimatedEnterTime;
	private SimpleStringProperty estimatedExitTime;
	private SimpleStringProperty status;
	private SimpleStringProperty orderTable;

	public OrderInTable(String orderId, String amountOfVisitors,boolean isPaid, String ownerPhone, String estimatedEnterTime,
			String estimatedExitTime, String status) {
		this.orderId = new SimpleStringProperty(orderId);
		this.amountOfVisitors =  new SimpleStringProperty(amountOfVisitors);
		this.isPaid=((isPaid) ? new SimpleStringProperty("Yes") : new SimpleStringProperty("No"));
		this.ownerPhone =  new SimpleStringProperty(ownerPhone);
		this.estimatedEnterTime =  new SimpleStringProperty(estimatedEnterTime);
		this.estimatedExitTime =  new SimpleStringProperty(estimatedExitTime);
		this.status = new SimpleStringProperty(status);
	}
	
	public OrderInTable(Order order) {
		this.orderId = new SimpleStringProperty(String.valueOf(order.getOrderId()));
		this.amountOfVisitors =  new SimpleStringProperty(String.valueOf(order.getNumberOfVisitors()));
		this.isPaid=((order.isPaid()) ? new SimpleStringProperty("Yes") : new SimpleStringProperty("No"));
		this.ownerPhone =  new SimpleStringProperty(order.getTelephoneNumber());
		this.estimatedEnterTime =  new SimpleStringProperty(order.getEnterDate().toString());
		this.estimatedExitTime =  new SimpleStringProperty(order.getExitDate().toString());
		this.status = new SimpleStringProperty(order.getStatus().toString());
		this.orderTable= new SimpleStringProperty((order.getOrderType().toString().contains("Occasional")?"Occasional" : "Preorder"));
	}
	
	public OrderInTable() {}

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

	
	@Override
	public boolean equals(Object obj) {
		return this.getOrderId().equals(((OrderInTable)obj).getOrderId());
	}

	public String getIsPaid() {
		return isPaid.get();
	}

	public void setIsPaid(String isPaid) {
		this.isPaid.set(isPaid);
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(String status) {
		this.status.set(status);
	}

	public String getOrderTable() {
		return orderTable.get();
	}

	public void setOrderTable(String orderTable) {
		this.orderTable.set(orderTable);
	}

}
