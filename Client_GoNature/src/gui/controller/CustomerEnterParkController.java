package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import utils.CurrentDate;

public class CustomerEnterParkController implements Initializable {

	@FXML
	public Label date_lbl;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		date_lbl.setText(CurrentDate.getCurrentDate("'Today' yyyy-MM-dd"));
		
	}
	
}
