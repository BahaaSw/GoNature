package client;

import java.io.IOException;
import java.util.ArrayList;

import logic.ClientRequestFromServer;
import logic.ServerResponseBackToClient;
import ocsf.AbstractClient;
import ocsf.ChatIF;
import utils.enums.ClientRequest;

public class ClientCommunication extends AbstractClient {
	// Instance variables **********************************************

		/**
		 * The interface type variable. It allows the implementation of the display
		 * method in the client.
		 */

	
	ChatIF clientUI;
	public static boolean awaitResponse = false;
	public static ServerResponseBackToClient responseFromServer;
	public static ArrayList<ClientRequest> requestsWaitingForApproval=new ArrayList<>();
	
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
		if (msg instanceof ServerResponseBackToClient) {
			ServerResponseBackToClient response = (ServerResponseBackToClient) msg;
			responseFromServer = response;	
		}
		
		else if (msg instanceof String) {
			String serverMsg = (String) msg;
			if (serverMsg.equals("Finished")) {
				//System.out.println("Finished handle client request");
			}
			if (serverMsg.equals("Server closing")) {
			}
				
		}
	}
	
	public void handleMessageFromClientUI(String message) {
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
	
	public void handleMessageFromClientUI(ClientRequestFromServer<?> message) {
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
