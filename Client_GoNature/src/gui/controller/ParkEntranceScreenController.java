package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import utils.CurrentDateAndTime;

public class ParkEntranceScreenController implements Initializable {
	@FXML
	public Label dateLabel;
	@FXML
	public TextField orderIdField;
	@FXML
	public Button enterParkButton;
	@FXML
	public Button newVisitButton;
	@FXML
	public HBox errorSection;
	@FXML
	public Label errorMessageLabel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		hideErrorMessage();
		
	}

	public void onEnterParkClicked() {
		//TODO: search for relevant order in database
		//if found, make it as In Park status.
		//update current in park, and available spots.
	}
	
	public void onNewVisitClicked() {
		//TODO: open handleoccasionalvisit screen
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
