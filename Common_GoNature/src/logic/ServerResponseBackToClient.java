package logic;

import java.io.Serializable;

import utils.enums.ServerResponse;

public class ServerResponseBackToClient implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4087076871424183043L;
	private ServerResponse response;
	private Object message;
	
	public ServerResponseBackToClient(ServerResponse response,Object message) {
		this.response=response;
		this.message=message;
	}

	public ServerResponse getRensponse() {
		return response;
	}

	public void setRensponse(ServerResponse response) {
		this.response = response;
	}


	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

}
