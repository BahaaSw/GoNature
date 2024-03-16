package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
	private ParkNameEnum selectedPark=ParkNameEnum.None;
	
	private ObservableList<ParkNameEnum> parks = FXCollections.observableArrayList(
			ParkNameEnum.Banias,
			ParkNameEnum.Herodium,
			ParkNameEnum.Masada
			);
	
	public HandleOccasionalVisitorScreenController(BorderPane screen) {
		this.screen=screen;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		parksList.getItems().addAll(parks);
		parksList.setOnAction(this::onChangeSelection);
		hideErrorMessage();
	}
	
	private void onChangeSelection(ActionEvent event) {
		selectedPark=parksList.getValue();
	}
	
	public void onNewVisitClicked() {
		//TODO: validate Gui
		//TODO: try to create new visit in database.
		//TODO: if there is not space, just cancel the order (do not insert into database)
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
