package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import logic.Guide;
import utils.CurrentDateAndTime;
import utils.enums.ParkNameEnum;

public class AddNewGuideScreenController implements Initializable{

	@FXML
	public Label dateLabel;
	@FXML
	public ComboBox<ParkNameEnum> parksList;
	@FXML
	public TextField firstNameField;
	@FXML
	public TextField lastNameField;
	@FXML
	public TextField guideIdField;
	@FXML
	public TextField phoneNumberField;
	@FXML
	public TextField emailField;
	@FXML
	public TextField guideStatusField;
	@FXML
	public HBox errorSection;
	@FXML
	public Label errorMessageLabel;
	@FXML
	public Button approveButton;
	@FXML
	public Button denyButton;
	
	private BorderPane screen;
	
	private ObservableList<ParkNameEnum> parks = FXCollections.observableArrayList(
			ParkNameEnum.None,
			ParkNameEnum.Banias,
			ParkNameEnum.Herodium,
			ParkNameEnum.Masada
			);
	
	private Guide guideToAdd;
	
	public AddNewGuideScreenController(BorderPane screen, Object guideToAdd) {
		this.screen=screen;
		this.guideToAdd=(Guide)guideToAdd;
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Init all fields
		
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		parksList.setValue(ParkNameEnum.None);
		firstNameField.setText(guideToAdd.getFirstName());
		lastNameField.setText(guideToAdd.getLastName());
		guideIdField.setText(guideToAdd.getUserId());
		phoneNumberField.setText(guideToAdd.getPhoneNumber());
		emailField.setText(guideToAdd.getEmailAddress());
		guideStatusField.setText(guideToAdd.getUserStatus().toString());
		hideErrorMessage();
	}
	
	private void hideErrorMessage() {
		errorMessageLabel.setText("");
		errorSection.setVisible(false);
	}
	
	private void showErrorMessage(String error) {
		errorSection.setVisible(true);
		errorMessageLabel.setText(error);
	}
	
	public void onDenyClicked(){
		//TODO: Communicate with database to update the guide as approved
		//TODO: open load home screen to center
	}
	
	public void onApproveClicked() {
		//TODO: Communicate with the database to delete the guide from table.
		//TODO: open load home screen to center
	}
	
}
