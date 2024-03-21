package logic;

import java.time.LocalDateTime;

import utils.enums.RequestTypeEnum;
import utils.enums.RequestStatusEnum;

public class Request {

	private int requestId;
	private int parkId;
	private RequestTypeEnum requestType;
	private int oldValue;
	private int newValue;
	private RequestStatusEnum requestStatus;
	private LocalDateTime requestDate;

	public Request(int requestId, int parkId, RequestTypeEnum requestType, int oldValue, int newValue,
			RequestStatusEnum requestStatus, LocalDateTime requestDate) {
		this.requestId = requestId;
		this.parkId = parkId;
		this.requestType = requestType;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.requestStatus = requestStatus;
		this.requestDate = requestDate;
	}

	public Request() {
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public int getParkId() {
		return parkId;
	}

	public void setParkId(int parkId) {
		this.parkId = parkId;
	}

	public RequestTypeEnum getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestTypeEnum requestType) {
		this.requestType = requestType;
	}

	public int getOldValue() {
		return oldValue;
	}

	public void setOldValue(int oldValue) {
		this.oldValue = oldValue;
	}

	public int getNewValue() {
		return newValue;
	}

	public void setNewValue(int newValue) {
		this.newValue = newValue;
	}

	public RequestStatusEnum getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(RequestStatusEnum requestStatus) {
		this.requestStatus = requestStatus;
	}

	public LocalDateTime getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDateTime requestDate) {
		this.requestDate = requestDate;
	}

}