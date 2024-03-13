package client;

import java.io.IOException;

import logic.ClientRequestDataContainer;
import logic.ClientRequestFromServer;
import ocsf.ChatIF;

public class ClientMainControl implements ChatIF {

	// Class variables *************************************************

		/**
		 * The default port to connect on.
		 */
		private ClientCommunication client;

		// Instance variables **********************************************

		/**
		 * The instance of the client that created this ConsoleChat.
		 */
		
		// Constructors ****************************************************

		/**
		 * Constructs an instance of the ClientConsole UI.
		 *
		 * @param host The host to connect to.
		 * @param port The port to connect on.
		 * @throws IOException If failed to connect to server
		 * */
		 
		 public ClientMainControl(String host, int port){
		
			 try {
				 client = new ClientCommunication(host, port, this);
			 } catch (IOException exception) {
				System.out.println("Error: Can't setup connection!" + " Terminating client.");
				client=null;
			 }
	}
		 

		@Override
		public void display(String message) {
			System.out.println("> " + message);
		}
		
		
		public void accept(ClientRequestDataContainer message) {
			client.handleMessageFromClientUI(message);
		}
		
		public ClientCommunication getClient() {
			return client;
		}
}
