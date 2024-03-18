package gui.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import logic.Order;
import utils.CurrentDateAndTime;
import utils.NotificationMessageTemplate;

public class RescheduleOrderScreenController implements Initializable {
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
	public ListView<LocalDate> availableDatesList;
	private ArrayList<LocalDate> availableDatesFromDb;
	private ObservableList<LocalDate> availableDatesToDisplay=FXCollections.observableArrayList(
			LocalDate.now()
			);

	private BorderPane screen;
	private String selectedOption;
	private Order order;
	private LocalDate selectedDate = null;

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
		if(selectOptionComboBox.getValue()==null)
			return;
		
		selectedOption = selectOptionComboBox.getValue();
		if (selectedOption.equals("Choose New Date")) {
			waitingListView.setVisible(false);
			availableDatesView.setVisible(true);
			// TODO: run query to look for available dates
			availableDatesToDisplay.clear();
			showAvailableDates();
		} else if (selectedOption.equals("Enter Waiting List")) {
			waitingListView.setVisible(true);
			availableDatesView.setVisible(false);
			showWaitingListNotificationMessage();
		} else {
			waitingListView.setVisible(false);
			availableDatesView.setVisible(false);
		}
	}
	
	private void showAvailableDates() {
		// TODO: Import all available dates week forward.

		List<LocalDate> dates = new ArrayList<LocalDate>() {{
	        add(LocalDate.now());
	        add(LocalDate.now().plusDays(1));
	        add(LocalDate.now().plusDays(2));
	        add(LocalDate.now().plusDays(3));
	        add(LocalDate.now().plusDays(4));
	        add(LocalDate.now().plusDays(5));
	        add(LocalDate.now().plusDays(6));
	        add(LocalDate.now().plusDays(7));
	    }};
	    
		availableDatesToDisplay = FXCollections.observableArrayList(dates);
		
		new Thread(() -> {
		    while (!dates.isEmpty()) {
		        try {
		            Thread.sleep(1000); // Wait for 10 seconds

		            // Safely update the list and UI from the JavaFX Application Thread
		            Platform.runLater(() -> {
		                if (!dates.isEmpty()) {
		                    dates.remove(0); // Remove the first/oldest date from the list
		                    ObservableList<LocalDate> observableDates = FXCollections.observableArrayList(dates);
		                    availableDatesList.setItems(observableDates);
		                }
		            });
		        } catch (InterruptedException e) {
		            Thread.currentThread().interrupt(); // Restore interrupted status
		            break; // Exit the loop if the thread was interrupted
		        }
		    }
		}).start();

//		availableDatesList.setCellFactory(cell -> new ListCell<LocalDate>() {
//			@Override
//			protected void updateItem(LocalDate item, boolean empty) {
//				super.updateItem(item, empty);
//				if (empty || item == null) {
//					setText(null);
//				} else {
//					// Format the date as you prefer
//					setText(item.toString()); // Default format
//				}
//			}
//		});

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

}
