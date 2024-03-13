package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ParkManagerDashboardController implements Initializable{

	@FXML
	public Text user_name;
	@FXML
	public Label related_park;
	@FXML
	public Label current_in_park;
	@FXML
	public Label park_max_capacity;
	@FXML
	public Label reserved_orders_gap;
	@FXML
	public Label visit_idle_time;
	@FXML
	public ComboBox<?> select_parameters;
	@FXML
	public TextField new_value;
	@FXML
	public TextArea message_fld;
	@FXML
	public Button make_request;
	@FXML
	public ComboBox<?> select_report;
	@FXML
	public Button generate_report;
	@FXML
	public ImageView refresh_btn;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
}
