package gui.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientApplication;
import client.ClientCommunication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.ClientRequestDataContainer;
import logic.Order;
import logic.ServerResponseBackToClient;
import utils.CurrentDateAndTime;
import utils.NotificationMessageTemplate;
import utils.enums.ClientRequest;
import utils.enums.ServerResponse;

public class RescheduleOrderScreenController implements Initializable,IThreadController {
	@FXML
	public Label dateLabel;
	@FXML
	public ComboBox<String> selectOptionComboBox;
	private ObservableList<String> options = FXCollections.observableArrayList("Choose New Date", "Enter Waiting List");
	@FXML
	public HBox errorSection;
	@FXML
	public Label errorMessageLabel;
	@FXML
	public Button cancelReserveButton;
	@FXML
	public Button confirmButton;
	@FXML
	public HBox waitingListView;
	@FXML
	public Label enterWaitingListMsg;
	@FXML
	public VBox availableDatesView;

	@FXML
	public ListView<LocalDateTime> availableDatesList;
	private ArrayList<LocalDateTime> availableDatesFromDb;
	private ObservableList<LocalDateTime> availableDatesToDisplay = FXCollections.observableArrayList();

	private BorderPane screen;
	private String selectedOption;
	private Order order;
	private LocalDateTime selectedDate = null;
	private Thread searchAvailableDates=null;

	public RescheduleOrderScreenController(BorderPane screen, Object order) {
		this.screen = screen;
		this.order = (Order) order;

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		selectOptionComboBox.getItems().addAll(options);
		selectOptionComboBox.setOnAction(this::onChangeSelection);

		availableDatesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				// newValue contains the selected LocalDate
				selectedDate = newValue;
				// Perform actions based on the selected date here
			}
		});

	}

	private void onChangeSelection(ActionEvent event) {
		if (selectOptionComboBox.getValue() == null)
			return;

		selectedOption = selectOptionComboBox.getValue();
		if (selectedOption.equals("Choose New Date")) {
			waitingListView.setVisible(false);
			availableDatesView.setVisible(true);
			// TODO: run query to look for available dates
			availableDatesToDisplay.clear();
			showAvailableDates();
		} else if (selectedOption.equals("Enter Waiting List")) {
			cleanUp();
			waitingListView.setVisible(true);
			availableDatesView.setVisible(false);
			showWaitingListNotificationMessage();
		} else {
			waitingListView.setVisible(false);
			availableDatesView.setVisible(false);
		}
	}

	private void showAvailableDates() {
	    // Check if the previous thread is alive and interrupt it
	    if (searchAvailableDates != null && searchAvailableDates.isAlive()) {
	        searchAvailableDates.interrupt();
	        try {
	            searchAvailableDates.join(); // Wait for the thread to stop
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt(); // Restore interrupted status
	        }
	    }

	    // Instantiate a new Thread object for searchAvailableDates
	    searchAvailableDates = new Thread(() -> {
	        while (!Thread.interrupted()) {
	            try {
	                Thread.sleep(2000); // Wait for 2 seconds, not 10

	                // Safely update the list and UI from the JavaFX Application Thread
	                Platform.runLater(() -> {
	                    ClientApplication.client
	                            .accept(new ClientRequestDataContainer(ClientRequest.Search_For_Available_Date, order));
	                    ServerResponseBackToClient response = ClientCommunication.responseFromServer;
	                    if (response.getRensponse() == ServerResponse.Query_Failed)
	                        Thread.currentThread().interrupt();
	                    List<LocalDateTime> dates = (ArrayList<LocalDateTime>) response.getMessage();
	                    ObservableList<LocalDateTime> observableDates = FXCollections.observableArrayList(dates);
	                    availableDatesList.setItems(observableDates);
	                });
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt(); // Restore interrupted status
	                break; // Exit the loop if the thread was interrupted
	            }
	        }
	    });

	    // Start the new thread
	    searchAvailableDates.start();
	}

	private void showWaitingListNotificationMessage() {
		enterWaitingListMsg.setText(NotificationMessageTemplate.enterWaitingListMessage(order.getParkName().toString(),
				order.getOrderDate(), String.format("%d", order.getNumberOfVisitors())));
	}

	public void onCancelReserveClicked() {
		// TODO: go back to homepage
	}

	public void onConfirmClicked() {
		// TODO: update new order in database with it's relevant status
	}

	@Override
	public void cleanUp() {
	    // Stop the searchAvailableDates thread if it's running
	    if (searchAvailableDates != null && searchAvailableDates.isAlive()) {
	        searchAvailableDates.interrupt();
	        try {
	            searchAvailableDates.join(); // Wait for the thread to stop
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt(); // Restore interrupted status
	        }
	    }
		
	}

}
