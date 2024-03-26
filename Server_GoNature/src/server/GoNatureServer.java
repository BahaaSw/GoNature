package server;


import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Connection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;

import gui.controller.ServerScreenController;
import javafx.application.Platform;
import jdbc.DBConnectionDetails;
import jdbc.query.QueryControl;
import logic.ClientConnection;
import logic.ClientRequestDataContainer;
import logic.ClientRequestHandler;
import logic.Order;
import logic.ServerResponseBackToClient;
import logic.User;
import logic.Visitor;
import jdbc.MySqlConnection;
import ocsf.AbstractServer;
import ocsf.ConnectionToClient;
import utils.enums.ClientRequest;
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
 * The GoNatureServer class is the main system's server class. This class
 * extends AbstractServer ocsf class, and manage all the Client-Server design.
 */
public class GoNatureServer extends AbstractServer {

	// Use Singleton DesignPattern -> only 1 server may be running in our system.
	private static GoNatureServer server = null;
	private ServerScreenController serverController;
	private ClientRequestHandler clientRequestHandler;
	private static Thread sendNotifications24HoursBefore = null;
	private static Thread cancelOrdersNotConfirmedWithin2Hours = null;
	private static Thread cancelTimePassedWaitingListOrders = null;

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
		clientRequestHandler = new ClientRequestHandler(this.serverController);
		initializeThreadsAndStartRun();
	}

	/**
	 * This method handle the message from client and sends them to the correct
	 * method according to the sent Object instance.
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
		if (request == ClientRequest.Logout)
			handleUserLogoutFromApplication(data.getData(), client, clientIp);
		else {
			response = clientRequestHandler.handleRequest(data, client);

			try {
				client.sendToClient(response);
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void handleUserLogoutFromApplication(Object user, ConnectionToClient client, String clientIp) {
		try {
			String id = "";
			if (user instanceof Visitor) {
				id = "Visitor" + ((Visitor) user).getCustomerId();

			} else if (user instanceof User) {
				id = ((User) user).getUsername();
			}
			serverController.printToLogConsole(
					String.format("User : '%s' with IP : '%s' : Request Logout from Application", id, clientIp));
			serverController.getClientsList().remove(new ClientConnection("", client));
			serverController.printToLogConsole(
					String.format("User : '%s' with IP : '%s' : Logged Out Successfully", id, clientIp));
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
	@SuppressWarnings("unused")
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
		for (ClientConnection cl : serverController.getClientsList()) {
			if (cl.getConnection().equals(client)) {
				return;
			}
		}
		serverController.printToLogConsole(
				"Client " + details.getHostName() + " with IP:" + details.getHostAddress() + " Connected");
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
			closeAllThreads();
			server.sendToAllClients(new ServerResponseBackToClient(ServerResponse.Server_Disconnected, ""));
			server.stopListening();
			server.close();

		} catch (IOException ex) {
			System.out.println("Error while closing server");
			ex.printStackTrace();
		} finally {
			MySqlConnection.getInstance().closeConnection();
			server = null;
		}
	}

	private static void closeAllThreads() {
//		private static Thread sendNotifications24HoursBefore = null;
//		private static Thread cancelOrdersNotConfirmedWithin2Hours = null;
		// Stop the searchAvailableDates thread if it's running
		try {
			if (sendNotifications24HoursBefore != null && sendNotifications24HoursBefore.isAlive())
				sendNotifications24HoursBefore.interrupt();

			if (cancelOrdersNotConfirmedWithin2Hours != null && cancelOrdersNotConfirmedWithin2Hours.isAlive())
				cancelOrdersNotConfirmedWithin2Hours.interrupt();

			if (cancelTimePassedWaitingListOrders != null && cancelTimePassedWaitingListOrders.isAlive())
				cancelTimePassedWaitingListOrders.interrupt();
			
			sendNotifications24HoursBefore.join(); // Wait for the thread to stop
			cancelOrdersNotConfirmedWithin2Hours.join();
			cancelTimePassedWaitingListOrders.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Restore interrupted status
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
			// Run the 2 relevant threads
			sendNotifications24HoursBefore.start();
			cancelOrdersNotConfirmedWithin2Hours.start();
			cancelTimePassedWaitingListOrders.start();
		} catch (Exception ex) {
			ex.printStackTrace();
			serverController.printToLogConsole("Error - could not listen for clients!");
//			stopServer();
			server = null;
		}
	}

	private void initializeThreadsAndStartRun() {
		if (sendNotifications24HoursBefore != null && sendNotifications24HoursBefore.isAlive()) {
			sendNotifications24HoursBefore.interrupt();
			try {
				sendNotifications24HoursBefore.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		if (cancelOrdersNotConfirmedWithin2Hours != null && cancelOrdersNotConfirmedWithin2Hours.isAlive()) {
			cancelOrdersNotConfirmedWithin2Hours.interrupt();
			try {
				cancelOrdersNotConfirmedWithin2Hours.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		if (cancelTimePassedWaitingListOrders != null && cancelTimePassedWaitingListOrders.isAlive()) {
			cancelTimePassedWaitingListOrders.interrupt();
			try {
				cancelTimePassedWaitingListOrders.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		sendNotifications24HoursBefore = new Thread(() -> {
			while (!Thread.interrupted()) {
				try {
					Thread.sleep(1000);
					LocalDateTime currentTime = LocalDateTime.now();
					LocalDateTime relevantTimeTomorrow = currentTime.plusDays(1);
					ArrayList<Order> ordersToNotify = QueryControl.notificationQueries
							.CheckAllOrdersAndChangeToNotifedfNeeded(relevantTimeTomorrow);

					if (ordersToNotify != null && !ordersToNotify.isEmpty()) {
						Platform.runLater(() -> serverController
								.printToLogConsole("Confirmation Notification was sent to all order 24 before time"));
						for (Order order : ordersToNotify) {
							QueryControl.notificationQueries.UpdateAllWaitNotifyOrdersToNotify(order);
							String message = String.format(
									"Order: %d, Notification was sent by mail to %s and sms to %s", order.getOrderId(),
									order.getEmail(), order.getTelephoneNumber());
							Platform.runLater(() -> serverController.printToLogConsole(message));
						}
					}

				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		});

		cancelOrdersNotConfirmedWithin2Hours = new Thread(() -> {
			while (!Thread.interrupted()) {
				try {
					Thread.sleep(1000);
					LocalDateTime currentTime = LocalDateTime.now();
					LocalDateTime relevantTimeTomorrowMinus2Hours = currentTime.plusHours(22);
					ArrayList<Order> ordersToNotify = QueryControl.notificationQueries
							.CheckAllOrdersAndChangeToCancelledIfNeeded(relevantTimeTomorrowMinus2Hours);

					if (ordersToNotify != null && !ordersToNotify.isEmpty()) {
						for (Order order : ordersToNotify) {
							QueryControl.notificationQueries.automaticallyCancelAllNotifiedOrders(order);
							Platform.runLater(() -> serverController.printToLogConsole("Notification On Cancel Sent"));
						}
					}

				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		});

		cancelTimePassedWaitingListOrders = new Thread(()->{
			while (!Thread.interrupted()) {
				try {
					Thread.sleep(1000);
					LocalDateTime currentTime = LocalDateTime.now();
					ArrayList<Order> ordersToNotify = QueryControl.notificationQueries
							.CheckWaitingListAndRemoveAllIrrelcantOrders(currentTime);

					if (ordersToNotify != null && !ordersToNotify.isEmpty()) {
						Platform.runLater(() -> serverController.printToLogConsole(String.format("All orders in waiting list for %s marked as irrelevant", currentTime.toString())));
						for (Order order : ordersToNotify) {
							QueryControl.notificationQueries.automaticallyMarkOrdersAsIrrelevant(order);
						}
					}

				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		});
	}
}
