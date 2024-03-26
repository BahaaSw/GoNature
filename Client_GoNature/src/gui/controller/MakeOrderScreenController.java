package gui.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import client.ClientApplication;
import client.ClientCommunication;
import gui.view.ApplicationViewType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import logic.ClientRequestDataContainer;
import logic.EntitiesContainer;
import logic.ExternalUser;
import logic.ICustomer;
import logic.Order;
import logic.SceneLoaderHelper;
import logic.ServerResponseBackToClient;
import utils.AlertPopUp;
import utils.CurrentDateAndTime;
import utils.ValidationRules;
import utils.enums.ClientRequest;
import utils.enums.OrderStatusEnum;
import utils.enums.OrderTypeEnum;
import utils.enums.ParkNameEnum;
import utils.enums.UserTypeEnum;

public class MakeOrderScreenController implements Initializable {

	@FXML
	public Label dateLabel;
	@FXML
	public TextField firstNameField;
	@FXML
	public TextField lastNameField;
	@FXML
	public TextField idField;
	@FXML
	public TextField phoneNumberField;
	@FXML
	public TextField emailField;
	@FXML
	public DatePicker pickDate;
	@FXML
	public TextField numberOfVisitorsField;
	@FXML
	public HBox errorSection;
	@FXML
	public Label errorMessageLabel;
	@FXML
	public Button makeOrderButton;

	private BorderPane screen;
	private ParkNameEnum selectedPark = ParkNameEnum.None;
	private String selectedTime = "";
	private OrderTypeEnum selectedVisitType = OrderTypeEnum.None;
	private UserTypeEnum customerType = UserTypeEnum.ExternalUser;
	private SceneLoaderHelper GuiHelper = new SceneLoaderHelper();
	private ICustomer customerDetails;

	@FXML
	public ComboBox<ParkNameEnum> parksList;
	@FXML
	public ComboBox<String> pickTime;
	@FXML
	public ComboBox<OrderTypeEnum> visitType;

	private ObservableList<ParkNameEnum> parks = FXCollections.observableArrayList(ParkNameEnum.Banias,
			ParkNameEnum.Herodium, ParkNameEnum.Masada);

	private ObservableList<String> timeForVisits = FXCollections.observableArrayList("08:00","09:00", "10:00", "11:00", "12:00",
			"13:00", "14:00", "15:00", "16:00");

	private ObservableList<OrderTypeEnum> visitTypesList = FXCollections.observableArrayList();

	public MakeOrderScreenController(BorderPane screen, Object customer, ICustomer customerDetails) {
		this.screen = screen;
		customerType = ((ExternalUser) customer).getUserType();
		this.customerDetails = customerDetails;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		parksList.getItems().addAll(parks);
		parksList.setOnAction(this::onParkChangeSelection);
		pickTime.getItems().addAll(timeForVisits);
		pickTime.setOnAction(this::onTimeChangeSelection);
		// initialize date picker up to 3 months forward.
		pickDate.setDayCellFactory(picker -> new DateCell() {
			LocalDate maxDate = LocalDate.now().plusMonths(1);

			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				setDisable(empty || date.compareTo(LocalDate.now()) <= 0 || date.compareTo(maxDate) > 0);
			}
		});

		initializeGuiByCustomerType();
		visitType.setItems(visitTypesList);
		visitType.setOnAction(this::onVisitTypeChangeSelection);

		hideErrorMessage();
	}

	@SuppressWarnings("incomplete-switch")
	private void initializeGuiByCustomerType() {
		switch (customerType) {
		case Visitor:
			visitTypesList.add(OrderTypeEnum.Solo_PreOrder);
			visitTypesList.add(OrderTypeEnum.Family_PreOrder);
			firstNameField.setEditable(false);
			firstNameField.setText(customerDetails.getFirstName());
			lastNameField.setEditable(false);
			lastNameField.setText(customerDetails.getLastName());
			emailField.setEditable(false);
			emailField.setText(customerDetails.getEmailAddress());
			phoneNumberField.setEditable(false);
			phoneNumberField.setText(customerDetails.getPhoneNumber());
			idField.setEditable(false);
			idField.setText(customerDetails.getCustomerId());
			break;
		case ExternalUser:
			visitTypesList.add(OrderTypeEnum.Solo_PreOrder);
			visitTypesList.add(OrderTypeEnum.Family_PreOrder);
			break;
		case Guide:
			visitTypesList.add(OrderTypeEnum.Group_PreOrder);
			firstNameField.setEditable(false);
			firstNameField.setText(customerDetails.getFirstName());
			lastNameField.setEditable(false);
			lastNameField.setText(customerDetails.getLastName());
			emailField.setEditable(false);
			emailField.setText(customerDetails.getEmailAddress());
			phoneNumberField.setEditable(false);
			phoneNumberField.setText(customerDetails.getPhoneNumber());
			idField.setEditable(false);
			idField.setText(customerDetails.getCustomerId());
			break;
		}
	}
	
	private void onParkChangeSelection(ActionEvent event) {
		selectedPark = parksList.getValue();
	}

	private void onTimeChangeSelection(ActionEvent event) {
		selectedTime = pickTime.getValue();
	}

	private void onVisitTypeChangeSelection(ActionEvent event) {
		selectedVisitType = visitType.getValue();
	}

	private boolean validateGuiFields() {
		if(selectedTime.equals("") || selectedPark==ParkNameEnum.None || selectedVisitType == OrderTypeEnum.None||
				firstNameField.getText().equals("") || lastNameField.getText().equals("")|| idField.getText().equals("")||
				phoneNumberField.getText().equals("")|| emailField.getText().equals("")||pickDate.getValue()==null||
				numberOfVisitorsField.getText().equals("")) {
			showErrorMessage("All Fields must be filled!");
			return false;
		}
		
		if(ValidationRules.isValidIsraeliId(idField.getText())==false) {
			showErrorMessage("ID is not valid israeli ID");
			return false;
		}
		
		if(ValidationRules.isValidName(firstNameField.getText())==false ||
				ValidationRules.isValidName(lastNameField.getText())==false) {
			showErrorMessage("Name should be only letters");
			return false;
		}
		
		if(ValidationRules.isValidEmail(emailField.getText())==false) {
			showErrorMessage("Invalid email");
			return false;
		}
		
		if(ValidationRules.isValidPhone(phoneNumberField.getText())==false) {
			showErrorMessage("Invalid phone, should be 10 digits");
			return false;
		}
		
		if(ValidationRules.isPositiveNumeric(numberOfVisitorsField.getText())==false) {
			showErrorMessage("Number of Visitors should be positive number (above 0)!");
			return false;
		}
		
		if(selectedVisitType==OrderTypeEnum.Group_PreOrder && Integer.parseInt(numberOfVisitorsField.getText())>15) {
			showErrorMessage("Group order is limited up to 15 visitors");
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("incomplete-switch")
	public void onMakeOrderClicked() {
		
		if(!validateGuiFields()) {
			return;
		}
		
		Order order = createOrderFromFields();
		
		ClientRequestDataContainer requestMessage = new ClientRequestDataContainer(ClientRequest.Add_New_Order_If_Available,order);
		ClientApplication.client.accept(requestMessage);
		ServerResponseBackToClient response = ClientCommunication.responseFromServer;
		AnchorPane view;
		
		switch(response.getRensponse()) {
		case Requested_Order_Date_Is_Available:
			view = GuiHelper.loadRightScreenToBorderPaneWithController(screen,
					"/gui/view/OrderSummaryScreen.fxml", ApplicationViewType.Order_Summary_Screen,
					new EntitiesContainer(response.getMessage()));
			screen.setCenter(view);
			break;
			
		case Requested_Order_Date_Unavaliable:
			view = GuiHelper.loadRightScreenToBorderPaneWithController(screen,
					"/gui/view/RescheduleOrderScreen.fxml", ApplicationViewType.Reschedule_Order_Screen,
					new EntitiesContainer(response.getMessage()));
			screen.setCenter(view);
			break;
		case Too_Many_Visitors:
			AlertPopUp alert = new AlertPopUp(AlertType.INFORMATION, "Notification","Order Limit","This are too many visitors for our park");
			alert.showAndWait();
			break;
		}
		
	}
	
	private Order createOrderFromFields() {
		// TODO: check all fields validation.
		Order order = new Order();
		order.setParkName(selectedPark);
		order.setOwnerType((customerType==UserTypeEnum.ExternalUser)?UserTypeEnum.Visitor:customerDetails.getUserType());
		order.setFirstName(firstNameField.getText());
		order.setLastName(lastNameField.getText());
		order.setUserId(idField.getText());
		order.setPaid(false);
		order.setStatus(OrderStatusEnum.Wait_Notify);
		
		order.setTelephoneNumber(phoneNumberField.getText());
		order.setEmail(emailField.getText());
		LocalDate date = pickDate.getValue();
		String fullDateTime = date.toString() + "T" + selectedTime;
		order.setEnterDate(LocalDateTime.parse(fullDateTime));
		order.setNumberOfVisitors(Integer.parseInt(numberOfVisitorsField.getText()));
		order.setOrderType(selectedVisitType);
		return order;
	}

	private void hideErrorMessage() {
		errorMessageLabel.setText("");
		errorSection.setVisible(false);
	}

	private void showErrorMessage(String error) {
		errorSection.setVisible(true);
		errorMessageLabel.setText(error);
	}

}
