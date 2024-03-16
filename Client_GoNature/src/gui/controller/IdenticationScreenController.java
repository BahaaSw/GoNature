package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import logic.ExternalUser;
import utils.CurrentDateAndTime;

public class IdenticationScreenController implements Initializable{

	@FXML
	public Label dateLabel;
	@FXML
	public TextField orderIdField;
	@FXML
	public Button searchButton;
	@FXML
	public HBox errorSection;
	@FXML
	public Label errorMessageLabel;
	
	private BorderPane screen;
	private ExternalUser customer;
	
	public IdenticationScreenController(BorderPane screen,Object customer) {
		this.screen=screen;
		this.customer=(ExternalUser)customer;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		hideErrorMessage();
		
	}
	
	public void onSearchClicked() {
		//TODO: search for order in database. if found, open handle order screen!
		
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
