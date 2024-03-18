package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

import gui.controller.ServerScreenController;
import jdbc.DBConnectionDetails;
import jdbc.QueryType;
import jdbc.query.QueryControl;
import logic.ClientConnection;
import logic.ClientRequestDataContainer;
import logic.Employee;
import logic.Guide;
import logic.Order;
import logic.ServerResponseBackToClient;
import logic.User;
import logic.Visitor;
import jdbc.DatabaseResponse;
import jdbc.MySqlConnection;
import ocsf.AbstractServer;
import ocsf.ConnectionToClient;
import utils.enums.ClientRequest;
import utils.enums.EmployeeTypeEnum;
import utils.enums.ParkNameEnum;
import utils.enums.ServerResponse;
import utils.enums.UserStatus;
import utils.enums.UserTypeEnum;

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
 * The GoNatureServer class is the main system's server class. This class
 * extends AbstractServer ocsf class, and manage all the Client-Server design.
 */
public class GoNatureServer extends AbstractServer {

	// Use Singleton DesignPattern -> only 1 server may be running in our system.
	private static GoNatureServer server = null;
	private ServerScreenController serverController;

	/**
	 * Constructor
	 * 
	 * @param port             - The port number to connect on
	 * @param serverController - ServerGuiController to send information to the
	 *                         server gui view.
	 * @param dbConn           - The Connection instance to the database.
	 */
	private GoNatureServer(int port, ServerScreenController serverController) {
		super(port);
		this.serverController = serverController;
	}

	/**
	 * This method handle the message from client and sends them to the correct
	 * method according to the sent Object instance. TODO: in our main project ->
	 * this method will include all instances.
	 * 
	 * @param msg    - The Object instance the client sent to the server.
	 * @param client - The ConnectionToClient instance which include the details of
	 *               the client in order to be able send him back answer.
	 */
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		// get the ip of the client who have sent the message.
		String clientIp = client.getInetAddress().getHostAddress();
		ClientRequestDataContainer data = (ClientRequestDataContainer) msg;
		ClientRequest request = data.getRequest();
		ServerResponseBackToClient response = null;
		switch (request) {
		case Login_As_Employee:
			response = handleLoginAsEmployee(data, client);
			serverController.addToConnected(client, ((Employee)response.getMessage()).getUsername());
			break;

		case Login_As_Guide:
			response = handleLoginAsGuide(data, client);
			serverController.addToConnected(client, ((Guide)response.getMessage()).getUsername());
			break;

		case Login_As_Visitor:
			response = handleLoginAsVisitor(data, client);
			serverController.addToConnected(client, "Visitor "+((Visitor)response.getMessage()).getCustomerId());
			break;
			
		case Search_For_Relevant_Order:
			response = handleSearchForRelevantOrder(data,client);
			break;
		case Logout:
			handleUserLogoutFromApplication(data.getData(), client, clientIp);
			break;

		default:
			// TODO: implement all relevant actions
			break;
		}

		try {
			client.sendToClient(response);
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ServerResponseBackToClient handleLoginAsEmployee(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Employee employee = (Employee) data.getData();
		ServerResponseBackToClient response;
		DatabaseResponse DbResponse = QueryControl.employeeQueries.searchForApprovedEmployee(employee);
		if (DbResponse == DatabaseResponse.Such_Employee_Not_Found) {
			response = new ServerResponseBackToClient(ServerResponse.User_Does_Not_Found, employee);
			
		} else if (DbResponse == DatabaseResponse.Password_Incorrect) {
			response = new ServerResponseBackToClient(ServerResponse.Password_Incorrect, employee);
			
		} else if (DbResponse == DatabaseResponse.Employee_Connected_Successfully) {
			response = new ServerResponseBackToClient(ServerResponse.Employee_Connected_Successfully, employee);
			
			for (ClientConnection cl : serverController.getClientsList()) {
				if (cl.equals(new ClientConnection("", client))) {
					cl.setStatus("Connected As - " + employee.getUsername());
					break;
				}
			}
		} else {
			serverController.printToLogConsole("SQL Exception was thrown during search for approved employee query");
			response = new ServerResponseBackToClient(ServerResponse.Query_Failed, null);
		}
		return response;
	}

	private ServerResponseBackToClient handleLoginAsGuide(ClientRequestDataContainer data, ConnectionToClient client) {
		Guide guide = (Guide) data.getData();
		ServerResponseBackToClient response;
		DatabaseResponse DbResponse = QueryControl.customerQueries.searchForApprovedGuide(guide);
		if (DbResponse == DatabaseResponse.Such_Guide_Not_Found) {
			response = new ServerResponseBackToClient(ServerResponse.User_Does_Not_Found, guide);

		} else if (DbResponse == DatabaseResponse.Password_Incorrect) {
			response = new ServerResponseBackToClient(ServerResponse.Password_Incorrect, guide);

		} else if (DbResponse == DatabaseResponse.Guide_Connected_Successfully) {
			response = new ServerResponseBackToClient(ServerResponse.Guide_Connected_Successfully, guide);
			for (ClientConnection cl : serverController.getClientsList()) {
				if (cl.equals(new ClientConnection("", client))) {
					cl.setStatus("Connected As - " + guide.getUsername());
					break;
				}
			}

		} else if (DbResponse == DatabaseResponse.Guide_Not_Approve_Yet) {
			response = new ServerResponseBackToClient(ServerResponse.Guide_Status_Pending, guide);
		}

		else {
			serverController.printToLogConsole("SQL Exception was thrown during search for approved guide query");
			response = new ServerResponseBackToClient(ServerResponse.Query_Failed, null);
		}
		return response;
	}

	private ServerResponseBackToClient handleLoginAsVisitor(ClientRequestDataContainer data, ConnectionToClient client) {
		Visitor visitor = (Visitor) data.getData();
		ServerResponseBackToClient response;
		DatabaseResponse DbResponse = QueryControl.customerQueries.searchAccessForVisitor(visitor);
		if (DbResponse == DatabaseResponse.Doesnt_Have_Active_Order) {
			response = new ServerResponseBackToClient(ServerResponse.Visitor_Have_No_Orders_Yet, visitor);
			
		} else if (DbResponse == DatabaseResponse.Visitor_Connected_Successfully) {
			response = new ServerResponseBackToClient(ServerResponse.Visitor_Connected_Successfully, visitor);
		}
		else {
			serverController.printToLogConsole("SQL Exception was thrown during search for login visitor query");
			response = new ServerResponseBackToClient(ServerResponse.Query_Failed, null);
		}
		return response;
	}
	
	private ServerResponseBackToClient handleSearchForRelevantOrder(ClientRequestDataContainer data, ConnectionToClient client) {
		Order order = (Order)data.getData();
		ServerResponseBackToClient response;
		DatabaseResponse DbResponse = QueryControl.orderQueries.fetchOrderDetailsByID(order);
		if(DbResponse == DatabaseResponse.Such_Order_Does_Not_Exists) {
			response = new ServerResponseBackToClient(ServerResponse.Order_Not_Found,order);
		}
		else if(DbResponse == DatabaseResponse.Order_Found_Successfully) {
			response = new ServerResponseBackToClient(ServerResponse.Order_Found,order);
		}
		else {
			serverController.printToLogConsole("SQL Exception was thrown during search relevant order query");
			response = new ServerResponseBackToClient(ServerResponse.Query_Failed, null);
		}
		return response;
	}
	
	private void handleUserLogoutFromApplication(Object user, ConnectionToClient client, String clientIp) {
		try {
			String id="";
			if (user instanceof Visitor) {
				id="Visitor"+((Visitor)user).getCustomerId();
				
			}
			else if(user instanceof User) {
				id=((User)user).getUsername();
			}
				serverController.printToLogConsole(String.format("User : '%s' with IP : '%s' : Request Logout from Application",id, clientIp));
				serverController.getClientsList().remove(new ClientConnection("", client));
				serverController.printToLogConsole(String.format("User : '%s' with IP : '%s' : Logged Out Successfully",id, clientIp));
			client.sendToClient(new ServerResponseBackToClient(ServerResponse.User_Logout_Successfully, null));
		} catch (IOException ex) {
			serverController.printToLogConsole("Error while sending update message to client");
			return;
		}
	}

	/**
	 * This method handle a specific String message from client for now relevant
	 * only for message when client disconnecting the server.
	 * 
	 * @param msg    - The string message the client sent to the server.
	 * @param client - The ConnectionToClient instance which include the details of
	 *               the client in order to be able send him back answer.
	 */
	private void handleDisconnectMsgFromClient(ConnectionToClient client) {
		clientDisconnected(client);
		return;
	}

	/**
	 * This method write to log screen the server has been started.
	 */
	@Override
	protected void serverStarted() {
		serverController.printToLogConsole(
				String.format("Server listening for connnections on address %s:%s", getServerIpAddress(), getPort()));
	}

	/**
	 * The method enumerate through the network interface eliminate local host and
	 * virtual machines to return the right IP;
	 * 
	 * @return a String containing the correct IP
	 */
	private String getServerIpAddress() {
		String ip;
		try {
			@SuppressWarnings("rawtypes")
			Enumeration e1 = NetworkInterface.getNetworkInterfaces();
			while (e1.hasMoreElements()) {
				NetworkInterface network = (NetworkInterface) e1.nextElement();
				@SuppressWarnings("rawtypes")
				Enumeration e2 = network.getInetAddresses();
				while (e2.hasMoreElements()) {
					InetAddress inetAddress = (InetAddress) e2.nextElement();
					ip = inetAddress.getHostAddress();
					if (ip.contains(".") && !ip.equals("127.0.0.1")
							&& !network.getDisplayName().toLowerCase().contains("virtual")) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return "Not found network addresses. please use ipconfig in commandline";
	}

	/**
	 * This method write to log screen the server has been stopped.
	 */
	@Override
	protected void serverStopped() {
		serverController.printToLogConsole("Server has stopped listening for connections\n");
	}

	/**
	 * This method write to log screen the server has been stopped.
	 */
	@Override
	protected void serverClosed() {
		serverController.printToLogConsole("Server has been closed\n");
	}

	/**
	 * This method called when new client connect to the server. add the client to
	 * the server table view and write to log.
	 * 
	 * @param client - The ConnectionToClient instance which include the details of
	 *               the client in order to be able send him back answer.
	 */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		InetAddress details = client.getInetAddress();
		String test = client.toString();
		for (ClientConnection cl : serverController.getClientsList()) {
			if (cl.getConnection().equals(client)) {
				return;
			}
		}
		serverController.printToLogConsole("Client " + details.getHostName() + " with IP:" + details.getHostAddress() + " Connected");
	}

	/**
	 * This method called when a client disconnected from the server. remove the
	 * client from the server's table view and write to log.
	 * 
	 * @param client - The ConnectionToClient instance which include the details of
	 *               the client in order to be able send him back answer.
	 */
	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		InetAddress details = client.getInetAddress();
		serverController.printToLogConsole(
				"Client " + details.getHostName() + " with IP:" + details.getHostAddress() + " Disconnected");
		serverController.removeFromConnected(client);
	}

	/**
	 * This method stop the server to listen on a specific port and close the
	 * server.
	 */
	public static void stopServer() {
		// there is no server yet
		if (server == null)
			return;

		try {
			// first tell all the clients to disconnect.
			server.sendToAllClients(new ServerResponseBackToClient(ServerResponse.Server_Disconnected, ""));
			server.stopListening();
			server.close();
			server = null;
		} catch (IOException ex) {
			System.out.println("Error while closing server");
			ex.printStackTrace();
		}
	}

	/**
	 * This method start the server to listen on a specific port and connect to
	 * database via database controller.
	 * 
	 * @param db               - The DBConnectionDetails entity which contains all
	 *                         required data to connect the database.
	 * @param port             - The port number to connect on
	 * @param serverController - ServerGuiController to send information to the
	 *                         server gui view.
	 */
	public static void startServer(DBConnectionDetails db, Integer port, ServerScreenController serverController) {
		// try to connect the database
		MySqlConnection.setDBConnectionDetails(db);
		Connection conn = (Connection) MySqlConnection.getInstance().getConnection();
		// if failed -> can't start the server.
		if (conn == null) {
			serverController.printToLogConsole("Can't start server! Connection to database failed!");
			return;
		}

		serverController.printToLogConsole("Connection to database succeed");

		// Singleton DesignPattern. Only 1 instance of server is available.
		if (server != null) {
			serverController.printToLogConsole("There is already a connected server");
			return;
		}

		server = new GoNatureServer(port, serverController);

		try {
			server.listen();
			// update connection in server gui.
			serverController.connectionSuccessfull();
		} catch (Exception ex) {
			ex.printStackTrace();
			serverController.printToLogConsole("Error - could not listen for clients!");
//			stopServer();
			server = null;
		}
	}

}