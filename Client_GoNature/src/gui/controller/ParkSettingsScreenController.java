package gui.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import client.ClientApplication;
import client.ClientCommunication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logic.ClientRequestDataContainer;
import logic.Employee;
import logic.Park;
import logic.Request;
import logic.ServerResponseBackToClient;
import utils.AlertPopUp;
import utils.enums.ClientRequest;
import utils.enums.RequestTypeEnum;

public class ParkSettingsScreenController implements Initializable{

	@FXML
	public Label datelabel;
	@FXML
	public TextField parkField1;
	@FXML
	public TextField parkField2;
	@FXML
	public TextField parkField3;
	@FXML
	public TextField oldCapacityField;
	@FXML
	public TextField oldReservedEntriesField;
	@FXML
	public TextField oldEstimatedVisitTimeField;
	@FXML
	public TextField newCapacityField;
	@FXML
	public TextField newReservedEntriesField;
	@FXML
	public TextField newEstimatedVisitTimeField;
	@FXML
	public Button changeEstimatedVisitTimeRequest;
	@FXML
	public Button changeReservedEntriesRequest;
	@FXML
	public Button changeCapacityRequest;
	
	@SuppressWarnings("unused")
	private Employee employee;
	private Park park;
	private AlertPopUp alert;
	
	public ParkSettingsScreenController(Object park, Object employee) {
		this.employee=(Employee)employee;
		this.park=(Park)park;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.Search_For_Specific_Park,park);
		ClientApplication.client.accept(request);
		ServerResponseBackToClient response = ClientCommunication.responseFromServer;
		park = (Park)response.getMessage();
		parkField1.setText(park.getParkName().toString());
		parkField2.setText(park.getParkName().toString());
		parkField3.setText(park.getParkName().toString());
		oldCapacityField.setText(String.format("%d", park.getCurrentMaxCapacity()));
		oldReservedEntriesField.setText(String.format("%d", park.getCurrentEstimatedReservedSpots()));
		oldEstimatedVisitTimeField.setText(String.format("%d", park.getCurrentEstimatedStayTime()));
	}
	
	@SuppressWarnings("incomplete-switch")
	public void onChangeEstimatedVisitTimeRequest() {
//		public Request(int parkId,RequestTypeEnum requestType, int oldValue, int newValue, LocalDateTime requestDate )
		Request parametersRequest = new Request(park.getParkId(),RequestTypeEnum.EstimatedVisitTime,Integer.parseInt(oldEstimatedVisitTimeField.getText()),
				Integer.parseInt(newEstimatedVisitTimeField.getText()),LocalDateTime.now());
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.Make_New_Park_Estimated_Visit_Time_Request,parametersRequest);
		ClientApplication.client.accept(request);
		ServerResponseBackToClient response = ClientCommunication.responseFromServer;
		
		switch(response.getRensponse()) {
		case Last_Request_With_Same_Type_Still_Pending:
			alert = new AlertPopUp(AlertType.WARNING,"Warning","Estimated Visit Time Request","Last Request With Same Type Still Pending");
			alert.showAndWait();
			return;
		case Request_Sent_To_Department_Successfully:
			alert = new AlertPopUp(AlertType.INFORMATION,"Information","Estimated Visit Time Request","Request has been sent to department manager");
			alert.showAndWait();
			return;
		}

	}
	
	@SuppressWarnings("incomplete-switch")
	public void onChangeReservedEntriesRequest() {
		Request parametersRequest = new Request(park.getParkId(),RequestTypeEnum.ReservedSpots,Integer.parseInt(oldReservedEntriesField.getText()),
				Integer.parseInt(newReservedEntriesField.getText()),LocalDateTime.now());
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.Make_New_Park_Reserved_Entries_Request,parametersRequest);
		ClientApplication.client.accept(request);
		ServerResponseBackToClient response = ClientCommunication.responseFromServer;
		
		switch(response.getRensponse()) {
		case Last_Request_With_Same_Type_Still_Pending:
			alert = new AlertPopUp(AlertType.WARNING,"Warning","Reserved Entries Request","Last Request With Same Type Still Pending");
			alert.showAndWait();
			return;
		case Request_Sent_To_Department_Successfully:
			alert = new AlertPopUp(AlertType.INFORMATION,"Information","Reserved Entries Request","Request has been sent to department manager");
			alert.showAndWait();
			return;
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	public void onChangeCapacityRequest() {
		Request parametersRequest = new Request(park.getParkId(),RequestTypeEnum.MaxCapacity,Integer.parseInt(oldCapacityField.getText()),
				Integer.parseInt(newCapacityField.getText()),LocalDateTime.now());
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.Make_New_Park_Capacity_Request,parametersRequest);
		ClientApplication.client.accept(request);
		ServerResponseBackToClient response = ClientCommunication.responseFromServer;
		
		switch(response.getRensponse()) {
		case Last_Request_With_Same_Type_Still_Pending:
			alert = new AlertPopUp(AlertType.WARNING,"Warning","Max park Capacity Request","Last Request With Same Type Still Pending");
			alert.showAndWait();
			return;
		case Request_Sent_To_Department_Successfully:
			alert = new AlertPopUp(AlertType.INFORMATION,"Information","Max park Capacity Request","Request has been sent to department manager");
			alert.showAndWait();
			return;
		}

	}

}
