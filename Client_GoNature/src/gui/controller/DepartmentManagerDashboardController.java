package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class DepartmentManagerDashboardController implements Initializable {
	@FXML
	public Text user_name;
	@FXML
	public Label login_date;
	@FXML
	public Label top_visited_park;
	@FXML
	public Label notification;
	@FXML
	public Label income_lbl;
	@FXML
	public Label expense_lbl;
	@FXML
	public TableView<?> orders_table_view;
	@FXML
	public TextArea message_fld;
	@FXML
	public Button update_request;
	@FXML
	public Button generate_report;
	@FXML
	public ImageView refresh_btn;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
}
