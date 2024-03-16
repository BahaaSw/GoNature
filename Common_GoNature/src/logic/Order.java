package logic;

import java.io.Serializable;
import java.time.LocalDateTime;

import utils.CurrentDateAndTime;
import utils.enums.OrderStatusEnum;
import utils.enums.OrderTypeEnum;
import utils.enums.ParkNameEnum;
import utils.enums.UserTypeEnum;

public class Order implements Serializable {

	private int orderId;
	private OrderTypeEnum orderType;
	private ParkNameEnum parkName;
	private String userId;
	private UserTypeEnum userType;
	private String telephoneNumber;
	private String email;
	private String timeOfVisit;
	private int numberOfVisitors;
	private String creationDate;
	private OrderStatusEnum status;
	private String lastStatusUpdatedTime;
	private String confirmationTime;
	private int price;

	public Order() {
	}

	public Order(int orderId) {
		this.orderId=orderId;
	}
	
	public Order(int orderId,OrderTypeEnum orderType,ParkNameEnum parkName,String userId,UserTypeEnum userType,
			String telephoneNumber,String email,String timeOfVisit,int numberOfVisitors,String creationDate) {
		this.orderId=orderId;
		this.orderType=orderType;
		this.parkName=parkName;
		this.userId=userId;
		this.userType=userType;
		this.telephoneNumber=telephoneNumber;
		this.email=email;
		this.timeOfVisit=timeOfVisit;
		this.numberOfVisitors=numberOfVisitors;
		this.creationDate=creationDate;
		status=OrderStatusEnum.Wait_Notification;
		lastStatusUpdatedTime=CurrentDateAndTime.getCurrentDateAndTime("yyyy-MM-dd hh:mm");
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public ParkNameEnum getParkName() {
		return parkName;
	}

	public void setParkName(ParkNameEnum parkName) {
		this.parkName = parkName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String UserId) {
		this.userId = UserId;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTimeOfVisit() {
		return timeOfVisit;
	}

	public void setTimeOfVisit(String timeOfVisit) {
		this.timeOfVisit = timeOfVisit;
	}

	public int getNumberOfVisitors() {
		return numberOfVisitors;
	}

	public void setNumberOfVisitors(int numberOfVisitors) {
		this.numberOfVisitors = numberOfVisitors;
	}

	public String getOrderDate() {
		return creationDate;
	}

	public void setOrderDate(String orderDate) {
		this.creationDate = orderDate;
	}

	public String getOrderConfirmationTime() {
		return confirmationTime;
	}

	public void setOrderConfirmationTime(String confirmationTime) {
		this.confirmationTime = confirmationTime;
	}

	public OrderTypeEnum getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderTypeEnum orderType) {
		this.orderType = orderType;
	}

	public OrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(OrderStatusEnum status) {
		this.status = status;
	}

	public UserTypeEnum getUserType() {
		return userType;
	}

	public void setUserType(UserTypeEnum userType) {
		this.userType = userType;
	}
	
	public String getLastStatusUpdatedTime() {
		return lastStatusUpdatedTime;
	}
	
	public void setLastStatusUpdatedTime(String lastStatusUpdatedTime) {
		this.lastStatusUpdatedTime=lastStatusUpdatedTime;
	}

	@Override
	public boolean equals(Object obj) {
		if (this.orderId == ((Order) obj).getOrderId())
			return false;
		return true;
	}
}