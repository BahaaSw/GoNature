package gui.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import logic.Employee;
import utils.AlertPopUp;
import utils.CurrentDateAndTime;
import utils.enums.ParkNameEnum;

public class HandleOccasionalVisitorScreenController implements Initializable {
	@FXML
	public Label dateLabel;
	@FXML
	public ComboBox<ParkNameEnum> parksList;
	@FXML
	public Label firstNameField;
	@FXML
	public Label lastNameField;
	@FXML
	public Label idField;
	@FXML
	public Label phoneNumberField;
	@FXML
	public Label emailField;
	@FXML
	public Label orderDateOfVisitField;
	@FXML
	public Label numberOfVisitorsField;
	@FXML
	public HBox errorSection;
	@FXML
	public Label errorMessageLabel;
	@FXML
	public Button newVisitButton;
	
	private BorderPane screen;
	private Employee employee;
	private ParkNameEnum selectedPark=ParkNameEnum.None;
	
	private ObservableList<ParkNameEnum> parks = FXCollections.observableArrayList(
			ParkNameEnum.Banias,
			ParkNameEnum.Herodium,
			ParkNameEnum.Masada
			);
	
	public HandleOccasionalVisitorScreenController(BorderPane screen,Employee employee) {
		this.screen=screen;
		this.employee=employee;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		parksList.setItems(parks);
		parksList.setValue(employee.getRelatedPark());
		hideErrorMessage();
	}
	
	public void onNewVisitClicked() {
		//TODO: validate Gui
		//TODO: try to create new visit in database.
		//TODO: if there is not space, just cancel the order (do not insert into database)
		AlertPopUp alert = new AlertPopUp(AlertType.CONFIRMATION, "New Visit", "Are you sure?", "Order will not be saved.",ButtonType.YES,ButtonType.CLOSE);
		Optional<ButtonType> result = alert.showAndWait();
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
