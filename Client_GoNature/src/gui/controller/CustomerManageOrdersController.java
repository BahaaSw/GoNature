package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import utils.CurrentDate;

public class CustomerManageOrdersController implements Initializable {

	@FXML
	public Label date_lbl;
	@FXML
	public Text fullname_text;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		date_lbl.setText(CurrentDate.getCurrentDate("'Today' yyyy-MM-dd"));
		
	}
	
}
