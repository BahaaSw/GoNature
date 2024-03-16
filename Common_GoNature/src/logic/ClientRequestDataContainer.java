package logic;

import java.io.Serializable;

import utils.enums.ClientRequest;

public class ClientRequestDataContainer implements Serializable{

	private ClientRequest request;
	private Object data;
	
	public ClientRequestDataContainer(ClientRequest request,Object data) {
		this.request=request;
		this.data=data;
	}
	
	public ClientRequestDataContainer() {}
	
	public void setRequest(ClientRequest request) {
		this.request=request;
	}
	public ClientRequest getRequest() {
		return request;
	}
	
	public void setMessage(Object data) {
		this.data=data;
	}
	
	public Object getData() {
		return data;
	}
	
}
