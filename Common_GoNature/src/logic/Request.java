package logic;

import java.time.LocalDateTime;

import utils.enums.RequestTypeEnum;
import utils.enums.RequestStatusEnum;

public class Request {
	
	
		private int RequestId;
		private int ParkId;
		private RequestTypeEnum RequestType;
		private int OldValue;
		private int NewValue;
		private RequestStatusEnum RequestStatus;
		private LocalDateTime RequestDate;
		
		public Request(int RequestId, int ParkId, RequestTypeEnum RequestType, int OldValue, int NewValue, RequestStatusEnum RequestStatus, LocalDateTime RequestDate)
		{
			this.RequestId=RequestId;
			this.ParkId=ParkId;
			this.RequestType=RequestType;
			this.OldValue=OldValue;
			this.NewValue=NewValue;
			this.RequestStatus=RequestStatus;
			this.RequestDate=RequestDate;
		}
		
		public int getRequestId() {
			return RequestId;
		}

		public void setRequestId(int requestId) {
			RequestId = requestId;
		}

		public int getParkId() {
			return ParkId;
		}

		public void setParkId(int parkId) {
			ParkId = parkId;
		}

		public RequestTypeEnum getRequestType() {
			return RequestType;
		}

		public void setRequestType(RequestTypeEnum requestType) {
			RequestType = requestType;
		}

		public int getOldValue() {
			return OldValue;
		}

		public void setOldValue(int oldValue) {
			OldValue = oldValue;
		}

		public int getNewValue() {
			return NewValue;
		}

		public void setNewValue(int newValue) {
			NewValue = newValue;
		}

		public RequestStatusEnum getRequestStatus() {
			return RequestStatus;
		}

		public void setRequestStatus(RequestStatusEnum requestStatus) {
			RequestStatus = requestStatus;
		}

		public LocalDateTime getRequestDate() {
			return RequestDate;
		}

		public void setRequestDate(LocalDateTime requestDate) {
			RequestDate = requestDate;
		}

		
		
}
