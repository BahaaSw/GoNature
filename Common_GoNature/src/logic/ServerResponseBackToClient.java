package logic;

import java.io.Serializable;

import utils.enums.ServerResponseEnum;

public class ServerResponseBackToClient implements Serializable{
	private ServerResponseEnum response;
	private Object message;
	private String msg;
	
	public ServerResponseBackToClient(ServerResponseEnum response,Object message,String msg) {
		this.response=response;
		this.message=message;
		this.msg=msg;
	}

	public ServerResponseEnum getRensponse() {
		return response;
	}

	public void setRensponse(ServerResponseEnum response) {
		this.response = response;
	}


	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
