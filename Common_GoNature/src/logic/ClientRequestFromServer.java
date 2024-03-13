package logic;

import java.io.Serializable;
import java.util.ArrayList;

import utils.enums.ClientRequest;


public class ClientRequestFromServer <T> implements Serializable{

	
	private ClientRequest requestType;
	private ArrayList<T> parameters = new ArrayList<>();
	private T obj;
	private String input;

	public ClientRequestFromServer(ClientRequest requestType) {
		this.requestType = requestType;
	}

	public ClientRequestFromServer(ClientRequest requestType, ArrayList<T> parameters) {
		this.requestType = requestType;
		this.parameters = parameters;
	}

	public ArrayList<?> getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList<T> parameters) {
		this.parameters = parameters;
	}

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}

	public ClientRequest getRequestType() {
		return requestType;
	}

	public void setRequestType(ClientRequest requestType) {
		this.requestType = requestType;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
}
