package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logic.Employee;
import logic.Park;

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
	
	private Employee employee;
	private Park park;
	
	public ParkSettingsScreenController(Object park, Object employee) {
		this.employee=(Employee)employee;
		this.park=(Park)park;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		parkField1.setText(park.getParkName().toString());
		parkField2.setText(park.getParkName().toString());
		parkField3.setText(park.getParkName().toString());
		oldCapacityField.setText(String.format("%d", park.getCurrentMaxCapacity()));
		oldReservedEntriesField.setText(String.format("%d", park.getCurrentEstimatedReservedSpots()));
		oldEstimatedVisitTimeField.setText(String.format("%d", park.getCurrentEstimatedStayTime()));
	}
	
	public void onChangeEstimatedVisitTimeRequest() {
		//TODO: create request 
	}
	
	public void onChangeReservedEntriesRequest() {
		//TODO: create request
	}
	
	public void onChangeCapacityRequest() {
		//TODO: create request
	}

}
