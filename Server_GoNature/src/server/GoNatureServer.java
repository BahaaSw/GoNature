package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Connection;
import java.util.Enumeration;

import gui.controller.ServerGuiController;
import jdbc.DBConnectionDetails;
import jdbc.QueryControl;
import logic.ClientRequestDataContainer;
import logic.ServerResponseBackToClient;
import jdbc.DBReturnOptions;
import jdbc.MySqlConnection;
import ocsf.AbstractServer;
import ocsf.ConnectionToClient;
import utils.enums.ClientRequest;
import utils.enums.ServerResponseEnum;



/**
 * 
 * @Author NadavReubens
 * @Author Gal Bitton
 * @Author Tamer Amer
 * @Author Rabea Lahham
 * @Author Bahaldeen Swied
 * @Author Ron Sisso
 * @version 1.0.0 
 */

/**
 * The GoNatureServer class is the main system's server class.
 * This class extends AbstractServer ocsf class, and manage all the Client-Server design.
 */
public class GoNatureServer extends AbstractServer {

	// Use Singleton DesignPattern -> only 1 server may be running in our system.
	private static GoNatureServer server = null;
	private ServerGuiController serverController;
	
	/**
	 * Constructor
	 * @param port - The port number to connect on
	 * @param serverController - ServerGuiController to send information to the server gui view.
	 * @param dbConn - The Connection instance to the database.
	 */
	private GoNatureServer(int port, ServerGuiController serverController) {
		super(port);
		this.serverController=serverController;
	}
	
	/**
	 * This method handle the message from client and sends them to the correct method
	 * according to the sent Object instance.
	 * TODO: in our main project -> this method will include all instances.
	 * @param msg - The Object instance the client sent to the server.
	 * @param client - The ConnectionToClient instance which include the details of the client in order to be able send him back answer.
	 */
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		// get the ip of the client who have sent the message.
		String clientIp = client.getInetAddress().getHostAddress();
		ClientRequestDataContainer data = (ClientRequestDataContainer)msg;
		ClientRequest request = data.getRequest();
		switch(request) {
//		case Login:
//			handleClientLoginToApplication((User)data.getMessage(),client,clientIp);
//			return;
//		case Logout:
//			handleUserLogoutFromApplication((User)data.getMessage(),client,clientIp);
//			return;
//		case SignUpNewUser:
//			handleClientSignupRequest((User)data.getMessage(),client,clientIp);
//			return;
//		
//		case UpdateOrder:
//			handleOrderUpdateFromClient((Order)data.getMessage(),client,clientIp);
//			return;
//			
//		case SearchOrder:
//			handleOrderSearchFromClient((Integer)data.getMessage(),client,clientIp);
//			return;
//			
//		case DisconnectFromServer:
//			handleDisconnectMsgFromClient(client);
//			return;
//			
		default:
			//TODO: implement all relevant actions
			return;
		}

	}
	
//	private void handleUserLogoutFromApplication(User user, ConnectionToClient client, String clientIp) {
//		User currentUser = (User)user;
//		serverController.printToLogConsole(String.format("User : '%s' with IP : '%s' : Request Logout from Application", user.getUsername(),clientIp));
//		DBReturnOptions dbReturn = QueryControl.changeIsConnectedFlagForUser(dbConn, currentUser, false, serverController);
//		try {
//			ServerActionsEnum action;
//			switch(dbReturn) {
//				case Failed:
//				case ExceptionWasThrown:
//				case UserChangedToConnected:
//					throw new IOException();
//					
//				case UserChangedToDisconnected:
//					action=ServerActionsEnum.UserLogout;
//					break;
//
//				default:
//					action=ServerActionsEnum.ForceDisconnect;
//					break;
//			}
//
//			client.sendToClient(new ServerDataContainer(action, currentUser));
//		}catch(IOException ex) {
//			serverController.printToLogConsole("Error while sending update message to client");
//			return;
//		}
//	}
//	
//	private void handleClientLoginToApplication(User user, ConnectionToClient client, String clientIp) {
//		User currentUser = (User)user;
//		serverController.printToLogConsole(String.format("User : '%s' with IP : '%s' : Request Login to Application", user.getUsername(),clientIp));
//		// try to search user in database.
//		DBReturnOptions dbReturn = QueryControl.searchForUser(dbConn, currentUser,serverController);
//		
//		try {
//			ServerActionsEnum action;
//			switch(dbReturn) {
//				case UserNotExists:
//					action=ServerActionsEnum.LoginUserDoesNotExists;
//					break;
//				case PasswordIncorrect:
//					action=ServerActionsEnum.PasswordIncorrect;
//					break;
//				case UserAlreadyLoggedIn:
//					action=ServerActionsEnum.UserAlreadyConnected;
//					break;
//				case Success:
//					action = ServerActionsEnum.LoginUserExists;
//					QueryControl.changeIsConnectedFlagForUser(dbConn, currentUser, true, serverController);
//					break;
//				case ExceptionWasThrown:
//					throw new IOException();
//				default:
//					action=ServerActionsEnum.LoginUserFailed;
//					break;
//			}
//
//			client.sendToClient(new ServerDataContainer(action, currentUser));
//		}catch(IOException ex) {
//			serverController.printToLogConsole("Error while sending update message to client");
//			return;
//		}
//	}
	
//	private void handleClientSignupRequest(User user, ConnectionToClient client, String clientIp) {
////		User currentUser = (User)user;
////		serverController.printToLogConsole(String.format("Client with IP : %s : Requested Signup New Account",clientIp));
////		boolean isExists = DBController.searchForUser(dbConn, currentUser, serverController);
//	}
//	
//	/**
//	 * This method handle a specific String message from client
//	 * for now relevant only for message when client disconnecting the server.
//	 * @param msg - The string message the client sent to the server.
//	 * @param client -  The ConnectionToClient instance which include the details of the client in order to be able send him back answer.
//	 */
//	private void handleDisconnectMsgFromClient(ConnectionToClient client) {
//			clientDisconnected(client);
//			return;
//		}	
//	/**
//	 * This method handle a specific Integer msg from client.
//	 * for now relevant only for searching Order in database by it's order ID (Integer).
//	 * @param msg - The Integer message the client sent to the server.
//	 * @param client - The ConnectionToClient instance which include the details of the client in order to be able send him back answer.
//	 * @param clientIp - The client IP.
//	 */
//	private void handleOrderSearchFromClient(Integer msg, ConnectionToClient client, String clientIp) {
//		server.serverController.printToLogConsole(String.format("Requested recieved from client %s to search for order id %s.",clientIp,msg.toString()));
//		// search for the order in database.
//		Order returnOrder = QueryControl.searchOrder(dbConn,msg,server.serverController);
//		// order found.
//		if(returnOrder!=null) {
//			respondOrderSearchToClient(returnOrder,client);
//			return;
//		}
//		
//		try {
//			//notify the client that the order wasn't found.
//			client.sendToClient("Order not found");
//			//write to log
//			serverController.printToLogConsole("Order with id - "+msg+" was not found!");
//		}catch(IOException ex) {
//			serverController.printToLogConsole("Error while sending order not found to client");
//		}
//	}
//	
//	/**
//	 * The method called only if the order was found in database.
//	 * It sends the client the requested Order entity.
//	 * @param returnOrder - The requested Order.
//	 * @param client -  The ConnectionToClient instance which include the details of the client in order to be able send him back answer.
//	 */
//	private void respondOrderSearchToClient(Order returnOrder,ConnectionToClient client) {
//		try {
//			serverController.printToLogConsole(String.format("Order with id %s was found and sent to client",returnOrder.getOrderNumber().toString()));
//			// try to send the client the requested order.
//			client.sendToClient(new ServerDataContainer(ServerActionsEnum.SearchOrderFound,returnOrder));
//		}catch(IOException ex) {
//			serverController.printToLogConsole("Error while sending order to client");
//		}
//	}
//	
//	/**
//	 * This method handle specific "Order" message from client.
//	 * for now, this method is relevant only for updating old order in database.
//	 * In the main project, this method should handle multiple actions on Order instance.
//	 * @param order - The new order instance.
//	 * @param client - The ConnectionToClient instance which include the details of the client in order to be able send him back answer.
//	 * @param clientIp - The client's IP.
//	 */
//	private void handleOrderUpdateFromClient(Order order, ConnectionToClient client, String clientIp) {
//		Order newOrder = (Order)order;
//		serverController.printToLogConsole(String.format("Request received from client %s to update details of order with id %s", clientIp,newOrder.getOrderNumber().toString()));
//		// try to update the old order in database.
//		boolean isUpdated = QueryControl.updateOrder(dbConn, newOrder.getOrderNumber(),newOrder.getParkName(),newOrder.getTelephoneNumber(),serverController);
//		// assume the order didn't update.
//		String message = "Order not updated";
//		
//		if(isUpdated) {
//			message="Order updated";
//			serverController.printToLogConsole(String.format("Order (%s) details were updated in database",newOrder.getOrderNumber().toString()));
//		}
//		else
//			serverController.printToLogConsole(String.format("Order (%s) details failed to update in database",newOrder.getOrderNumber().toString()));
//		
//		try {
//			// send the client the answer (updated or not updated)
//			ServerActionsEnum action = isUpdated ? ServerActionsEnum.UpdateOrderSuccessfully  : ServerActionsEnum.UpdateOrderFailed;
//			client.sendToClient(new ServerDataContainer(action, ""));
//		}catch(IOException ex) {
//			serverController.printToLogConsole("Error while sending update message to client");
//		}
//	}
//	
//	/**
//	 * This method write to log screen the server has been started.
//	 */
	@Override
	protected void serverStarted() {
		serverController.printToLogConsole(String.format("Server listening for connnections on address %s:%s",getServerIpAddress(),getPort()));
	}
//	
//	/**
//	 * The method enumerate through the network interface eliminate local host and virtual machines to 
//	 * return the right IP;
//	 * @return a String containing the correct IP
//	 */
	private String getServerIpAddress() {
		String ip;
		try {
			@SuppressWarnings("rawtypes")
			Enumeration e1 =NetworkInterface.getNetworkInterfaces();
			while(e1.hasMoreElements()) {
				NetworkInterface network = (NetworkInterface)e1.nextElement();
				@SuppressWarnings("rawtypes")
				Enumeration e2 = network.getInetAddresses();
				while(e2.hasMoreElements()) {
					InetAddress inetAddress = (InetAddress) e2.nextElement();
					ip=inetAddress.getHostAddress();
					if(ip.contains(".")&&!ip.equals("127.0.0.1")&&!network.getDisplayName().toLowerCase().contains("virtual")) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		}catch(SocketException ex) {
			ex.printStackTrace();
		}
		return "Not found network addresses. please use ipconfig in commandline";
	}
//	
//	/**
//	 * This method write to log screen the server has been stopped.
//	 */
	@Override
	protected void serverStopped() {
		serverController.printToLogConsole("Server has stopped listening for connections");
	}
	
	/**
	 * This method write to log screen the server has been stopped.
	 */
	@Override
	protected void serverClosed() {
		serverController.printToLogConsole("Server has been closed");
	}
	
	/**
	 * This method called when new client connect to the server. 
	 * add the client to the server table view and write to log.
	 * @param client - The ConnectionToClient instance which include the details of the client in order to be able send him back answer.
	 */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		InetAddress details = client.getInetAddress();
//		for(ClientConnection cl : serverController.getClientsList()) {
//			if(details.getHostAddress().equals(cl.getHostIp())) {
//				try {
//					client.sendToClient(new ServerRes(ServerActionsEnum.ForceDisconnect,""));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return;
//			}
//		}
		serverController.printToLogConsole("Client "+details.getHostName()+" with IP:"+details.getHostAddress()+ " Connected");
		serverController.addToConnected(client);
	}
	
	/**
	 * This method called when a client disconnected from the server.
	 * remove the client from the server's table view and write to log.
	 * @param client - The ConnectionToClient instance which include the details of the client in order to be able send him back answer.
	 */
	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		InetAddress details = client.getInetAddress();
		serverController.printToLogConsole("Client "+details.getHostName()+" with IP:"+details.getHostAddress()+ " Disconnected");
		serverController.removeFromConnected(client);
	}
	
	/**
	 * This method stop the server to listen on a specific port and close the server.
	 */
	public static void stopServer() {
		// there is no server yet
		if(server==null)
			return;
		
		try {
			// first tell all the clients to disconnect.
			server.sendToAllClients(new ServerResponseBackToClient(ServerResponseEnum.Server_Disconnected,"",""));
			server.stopListening();
			server.close();
			server=null;
		}catch(IOException ex) {
			System.out.println("Error while closing server");
			ex.printStackTrace();
		}
	}
	
	/**
	 * This method start the server to listen on a specific port and connect to database via database controller.
	 * @param db - The DBConnectionDetails entity which contains all required data to connect the database.
	 * @param port - The port number to connect on
	 * @param serverController - ServerGuiController to send information to the server gui view.
	 */
	public static void startServer(DBConnectionDetails db, Integer port, ServerGuiController serverController) {
		// try to connect the database
		MySqlConnection.setDBConnectionDetails(db);
		Connection conn = (Connection) MySqlConnection.getInstance().getConnection();
		// if failed -> can't start the server.
		if(conn==null) {
			serverController.printToLogConsole("Can't start server! Connection to database failed!");
			return;
		}
		
		serverController.printToLogConsole("Connection to database succeed");
		
		// Singleton DesignPattern. Only 1 instance of server is available.
		if(server!=null) {
			serverController.printToLogConsole("There is already a connected server");
			return;
		}
		
		server=new GoNatureServer(port, serverController);
		
		try {
			server.listen();
			// update connection in server gui.
			serverController.connectionSuccessfull();
		}catch(Exception ex) {
			ex.printStackTrace();
			serverController.printToLogConsole("Error - could not listen for clients!");
//			stopServer();
			server=null;
		}
	}
	
	
	
}
