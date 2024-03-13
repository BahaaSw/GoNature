package logic;

import java.io.Serializable;

import utils.enums.ClientRequest;

public class ClientRequestDataContainer implements Serializable{

	private ClientRequest request;
	private Object data;
	private String input;
	
	public ClientRequestDataContainer(ClientRequest request,Object data, String input) {
		this.request=request;
		this.data=data;
		this.input=input;
	}
	
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
	
	public String getInput() {
		return input;
	}
	
	public void setInput(String input) {
		this.input=input;
	}
	
	
	
	
}
