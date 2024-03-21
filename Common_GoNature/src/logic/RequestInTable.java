package logic;

import java.time.LocalDateTime;

import javafx.beans.property.SimpleStringProperty;
import utils.enums.ParkNameEnum;
import utils.enums.RequestStatusEnum;
import utils.enums.RequestTypeEnum;

public class RequestInTable {
	
	private Request request;
	
	private final SimpleStringProperty requestId;
	private final SimpleStringProperty parkId;
	private final SimpleStringProperty requestType;
	private final SimpleStringProperty oldValue;
	private final SimpleStringProperty newValue;
	private final SimpleStringProperty requestStatus;
	private final SimpleStringProperty requestDate; // This will hold the selected status

	public RequestInTable(int requestId, int parkId, RequestTypeEnum requestType, int oldValue, int newValue, RequestStatusEnum requestStatus,
			LocalDateTime requestDate) {
		this.requestId = new SimpleStringProperty(String.valueOf(requestId));
		this.parkId = new SimpleStringProperty(ParkNameEnum.fromParkId(parkId).name());
		this.requestType=new SimpleStringProperty(requestType.name());
		this.oldValue = new SimpleStringProperty(String.valueOf(oldValue));
		this.newValue = new SimpleStringProperty(String.valueOf(newValue));
		this.requestStatus = new SimpleStringProperty(requestStatus.name());
		this.requestDate = new SimpleStringProperty(requestDate.toString());
	}
	
	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public String getRequestId() {
		return requestId.get();
	}

	public String getParkId() {
		return parkId.get();
	}

	public String getRequestType() {
		return requestType.get();
	}

	public String getOldValue() {
		return oldValue.get();
	}

	public String getNewValue() {
		return newValue.get();
	}

	public String getRequestStatus() {
		return requestStatus.get();
	}

	public String getRequestDate() {
		return requestDate.get();
	}
	
	public void setStatus(String status) {
		this.requestStatus.set(status);
	}


	
}
