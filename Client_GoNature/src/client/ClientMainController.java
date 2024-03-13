package client;

import java.io.IOException;

import logic.ClientRequestFromServer;
import ocsf.ChatIF;

public class ClientMainController implements ChatIF {

	// Class variables *************************************************

		/**
		 * The default port to connect on.
		 */
		public static int DEFAULT_PORT;
		public ClientCommunication client;

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
		 
		 public ClientMainController(String host, int port) throws IOException {
		try {
			client = new ClientCommunication(host, port, this);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			throw exception;
		}
	}

		@Override
		public void display(String message) {
			System.out.println("> " + message);
		}
		
		public void accept(String str) {
			client.handleMessageFromClientUI(str);
		}
		
		public void accept(ClientRequestFromServer<?> message) {
			client.handleMessageFromClientUI(message);
		}
		
		public ClientCommunication getClient() {
			return client;
		}
}
