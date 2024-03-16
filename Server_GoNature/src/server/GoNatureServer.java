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
import jdbc.QueryType;
import logic.ClientConnection;
import logic.ClientRequestDataContainer;
import logic.Employee;
import logic.Guide;
import logic.ServerResponseBackToClient;
import logic.User;
import logic.Visitor;
import jdbc.DBReturnOptions;
import jdbc.MySqlConnection;
import ocsf.AbstractServer;
import ocsf.ConnectionToClient;
import utils.enums.ClientRequest;
import utils.enums.EmployeeTypeEnum;
import utils.enums.ParkNameEnum;
import utils.enums.ServerResponse;



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
		case Login_As_Employee:
//			handleClientLoginToApplication(data.getData(),client,clientIp);
			Employee employee = new Employee(ParkNameEnum.Banias,EmployeeTypeEnum.Department_Manager,"1","department","123456","gal","bitton","1234567890","gal@example.com");
			try {
				client.sendToClient(new ServerResponseBackToClient(ServerResponse.Employee_Connected_Successfully, employee));
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		case Login_As_Guide:
//			handleClientLoginToApplication(data.getData(),client,clientIp);
			Guide guide = new Guide("1","guide","123456","gal","bitton","1234567890","gal@example.com");
			try {
				client.sendToClient(new ServerResponseBackToClient(ServerResponse.Guide_Connected_Successfully, guide));
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		case Login_As_Visitor:
//			handleClientLoginToApplication(data.getData(),client,clientIp);
			Visitor visitor = new Visitor("123456","gal","bitton","1234567890","gal@example.com");
			try {
				client.sendToClient(new ServerResponseBackToClient(ServerResponse.Visitor_Connected_Successfully, visitor));
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		case Logout:
			handleUserLogoutFromApplication(data.getData(),client,clientIp);
			return;
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
	
	private void handleUserLogoutFromApplication(Object user, ConnectionToClient client, String clientIp) {
		try {
			User currentUser = (User)user;
			serverController.printToLogConsole(String.format("User : '%s' with IP : '%s' : Request Logout from Application", currentUser.getUsername(),clientIp));
			serverController.getConnectedUsers().remove(user);
			serverController.printToLogConsole(String.format("User : '%s' with IP : '%s' : Logged Out Successfully", currentUser.getUsername(),clientIp));
			client.sendToClient(new ServerResponseBackToClient(ServerResponse.User_Logout_Successfully, null));
		}catch(IOException ex) {
			serverController.printToLogConsole("Error while sending update message to client");
			return;
		}
	}
	
//	private void handleClientLoginToApplication(Object data, ConnectionToClient client, String clientIp) {
//		// try to search user in database.
//		User currentUser = (User)data;
//		serverController.printToLogConsole(String.format("User : '%s' with IP : '%s' : Request Login to Application", currentUser.getUsername(),clientIp));
////		DBReturnOptions dbReturn = QueryControl.searchForUser(currentUser,serverController);
//		ServerResponse response = null;
//		
//		try {
//			switch(dbReturn) {
//				case User_Not_Exists:
//					response=ServerResponse.User_Does_Not_Found;
//					break;
//				case Password_Incorrect:
//					response=ServerResponse.Password_Incorrect;
//					break;
//				case Success:
//					boolean isConnected=false;
//					for(User user: serverController.getConnectedUsers()) {
//						if(currentUser.equals(user)) {
//							response=ServerResponse.User_Already_Connected;
//							serverController.printToLogConsole(String.format("User : '%s' with IP : '%s' : Already Connected", currentUser.getUsername(),clientIp));
//							isConnected=true;
//							break;
//						}
//					}
//					if(isConnected)
//						break;
//					response=ServerResponse.User_Connected_Successfully;
//					serverController.getConnectedUsers().add(currentUser);
//					serverController.addToConnected(client,currentUser.getUsername());
//					serverController.printToLogConsole(String.format("User : '%s' with IP : '%s' : Login Successfully", currentUser.getUsername(),clientIp));
//					break;
//			}
//
//			client.sendToClient(new ServerResponseBackToClient(response, currentUser,""));
//		}catch(IOException ex) {
//			serverController.printToLogConsole("Error while sending update message to client");
//			return;
//		}
//	}
	
	
	/**
	 * This method handle a specific String message from client
	 * for now relevant only for message when client disconnecting the server.
	 * @param msg - The string message the client sent to the server.
	 * @param client -  The ConnectionToClient instance which include the details of the client in order to be able send him back answer.
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
		serverController.printToLogConsole(String.format("Server listening for connnections on address %s:%s",getServerIpAddress(),getPort()));
	}
	
	/**
	 * The method enumerate through the network interface eliminate local host and virtual machines to 
	 * return the right IP;
	 * @return a String containing the correct IP
	 */
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
	 * This method called when new client connect to the server. 
	 * add the client to the server table view and write to log.
	 * @param client - The ConnectionToClient instance which include the details of the client in order to be able send him back answer.
	 */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		InetAddress details = client.getInetAddress();
		for(ClientConnection cl : serverController.getClientsList()) {
			if(details.getHostAddress().equals(cl.getHostIp())) {
				return;
			}
		}
		serverController.printToLogConsole("Client "+details.getHostName()+" with IP:"+details.getHostAddress()+ " Connected");
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
			server.sendToAllClients(new ServerResponseBackToClient(ServerResponse.Server_Disconnected,""));
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
