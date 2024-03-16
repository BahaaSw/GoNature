package gui.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import logic.Order;
import utils.CurrentDateAndTime;
import utils.NotificationMessageTemplate;

public class FinishOrderScreenController implements Initializable {
	@FXML
	public Label dateLabel;
	@FXML
	public ComboBox<String> selectOptionComboBox;
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
	public ListView<LocalDate> availableDatesList;
	private ArrayList<LocalDate> availableDatesFromDb;
	private ObservableList<LocalDate> availableDatesToDisplay;
	private BorderPane screen;
	private ObservableList<String> options = FXCollections.observableArrayList("Choose New Date","Enter Waiting List");
	private String selectedOption;
	private Order order;
	
	public FinishOrderScreenController(BorderPane screen, Object order) {
		this.screen=screen;
		this.order=(Order)order;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		selectOptionComboBox.getItems().addAll(options);
		selectOptionComboBox.setOnAction(this::onChangeSelection);
		
	}
	
	private void onChangeSelection(ActionEvent event) {
		selectedOption = selectOptionComboBox.getValue();
		if(selectedOption.equals("Choose New Date")) {
			waitingListView.setVisible(false);
			availableDatesView.setVisible(true);
			availableDatesToDisplay.clear();
			showAvailableDates();
		}
		else if(selectedOption.equals("Enter Waiting List")) {
			waitingListView.setVisible(true);
			availableDatesView.setVisible(false);
			showWaitingListNotificationMessage();
		}
		else {
			waitingListView.setVisible(false);
			availableDatesView.setVisible(false);
		}
	}
	
	private void showAvailableDates() {
		//TODO: Import all available dates week forward.
		/*
		 * 	new Thread(() -> {
	        List<LocalDate> dates = fetchAvailableDates(); // Fetch the dates in a background thread
	        Platform.runLater(() -> {
	            ObservableList<LocalDate> observableDates = FXCollections.observableArrayList(dates);
	            availableDatesList.setItems(observableDates);
	        });
	    }).start();
		 * */
		availableDatesFromDb = new ArrayList<LocalDate>();
		availableDatesFromDb.add(LocalDate.now());
		
		availableDatesToDisplay=FXCollections.observableArrayList(availableDatesFromDb);
	    availableDatesList.setCellFactory(cell -> new ListCell<LocalDate>() {
	        @Override
	        protected void updateItem(LocalDate item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty || item == null) {
	                setText(null);
	            } else {
	                // Format the date as you prefer
	                setText(item.toString()); // Default format
	            }
	        }
	    });
	    

		
	}
	
	private void showWaitingListNotificationMessage() {
		enterWaitingListMsg.setText(NotificationMessageTemplate.enterWaitingListMessage(order.getParkName().toString(),order.getOrderDate(),
				String.format("%d", order.getNumberOfVisitors())));
	}
	
	public void onCancelReserveClicked() {
		//TODO: go back to homepage
	}
	
	public void onConfirmClicked() {
		//TODO: update new order in database with it's relevant status
	}

}
