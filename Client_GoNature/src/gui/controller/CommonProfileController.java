package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import utils.CurrentDate;

public class CommonProfileController implements Initializable {

	@FXML
	public Label date_lbl;
	
	@FXML
	public TextField fname_txt;
	@FXML
	public TextField lname_txt;
	@FXML
	public TextField username_txt;
	@FXML
	public TextField password_txt;
	@FXML
	public TextField account_type_txt;
	@FXML
	public TextField related_park_txt;
	@FXML
	public TextField phone_number_txt;
	@FXML
	public TextField email_txt;
	
	private boolean isEmployee=false;
	
	public CommonProfileController() {
		
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		date_lbl.setText(CurrentDate.getCurrentDate("'Today' yyyy-MM-dd"));
	}
	
	public void setIsEmployee(boolean flag) {
		isEmployee=flag;
	}

}
