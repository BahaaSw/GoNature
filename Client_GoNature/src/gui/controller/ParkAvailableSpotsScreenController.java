package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import logic.Employee;
import logic.Park;
import utils.CurrentDateAndTime;
import utils.enums.ParkNameEnum;

public class ParkAvailableSpotsScreenController implements Initializable {
	@FXML
	public Label dateLabel;
	@FXML
	public ComboBox<ParkNameEnum> parkSelect;
	@FXML
	public Label currentInParkLabel;
	@FXML
	public Label availableSpotsLabel;
	
	private ObservableList<ParkNameEnum> parks = FXCollections.observableArrayList();
	
	private Employee employee;
	private Park selectedPark;
	private ParkNameEnum selectedParkName=ParkNameEnum.None;
	
	public ParkAvailableSpotsScreenController(Object employee,Object park) {
		this.employee=(Employee)employee;
		selectedPark=(Park)park;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		switch(employee.getEmployeeType()) {
		case Department_Manager:
			parks.add(ParkNameEnum.Banias);
			parks.add(ParkNameEnum.Herodium);
			parks.add(ParkNameEnum.Masada);
			break;
		case Park_Manager:
		case Park_Employee:
			parks.add(employee.getRelatedPark());
			break;
		}
		
		parkSelect.getItems().addAll(parks);
		parkSelect.setOnAction(this::onChangeParkSelection);
//		parkSelect.setValue(defaultPark.getParkName());
		
		
	}
	
	private void onChangeParkSelection(ActionEvent event) {
		selectedParkName=parkSelect.getValue();
		//TODO: update the gui from database.
		//TODO: update selectedPark as server response
//		currentInParkLabel.setText(selectedPark.getCurrentInPark());
//		availableSpotsLabel.setText(selectedPark.getAvailableSpots());
	}

}
