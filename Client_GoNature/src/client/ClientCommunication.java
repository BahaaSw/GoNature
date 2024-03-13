package client;

import java.io.IOException;
import java.util.ArrayList;

import logic.ClientRequestDataContainer;
import logic.ClientRequestFromServer;
import logic.ServerResponseBackToClient;
import ocsf.AbstractClient;
import ocsf.ChatIF;
import utils.enums.ClientRequest;
import utils.enums.ServerResponseEnum;

public class ClientCommunication extends AbstractClient {
	// Instance variables **********************************************

		/**
		 * The interface type variable. It allows the implementation of the display
		 * method in the client.
		 */

	
	ChatIF clientUI;
//	private Object ServerResponseHandler;
	public static boolean awaitResponse = false;
	public static ServerResponseBackToClient responseFromServer;
	
	public ClientCommunication(String host, int port, ChatIF clientUI) throws IOException{
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		openConnection();
	}
	
	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		awaitResponse = false;
		ServerResponseBackToClient data = (ServerResponseBackToClient)msg;
		ServerResponseEnum response=data.getRensponse();
		
		switch(response) {
		case Login:
			return;
		case Password_Incorrect:
			return;
		case User_Already_Connected:
			return;
		case User_Does_Not_Exists:
			return;
		case Server_Closed:
			return;
		case Server_Disconnected:
			try {
				closeConnection();
				ClientApplication.client=null;
				System.exit(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return;
		}
		
	}
	
	public void handleMessageFromClientUI(ClientRequestDataContainer message) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;

			sendToServer(message);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server: Terminating client." + e);
			quit();
		}
	}
	
	public void quit() {
		try {
			this.closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
