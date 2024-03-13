package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import utils.CurrentDate;

public class CustomerDashboardController implements Initializable{
	@FXML
	public Text user_name;
	@FXML
	public Label login_date;
	@FXML
	public Label checking_bal;
	@FXML
	public Label checking_acc_num;
	@FXML
	public Label savings_bal;
	@FXML
	public Label savings_acc_num;
	@FXML
	public Label income_lbl;
	@FXML
	public Label expense_lbl;
	@FXML
	public ListView orders_listview;
	@FXML
	public TextField payee_fld;
	@FXML
	public TextField amount_fld;
	@FXML
	public TextArea message_fld;
	@FXML
	public Button send_money_btn;
	@FXML
	public Text acc_type_text;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		login_date.setText(CurrentDate.getCurrentDate("'Today' yyyy-MM-dd"));
	}
	

	
}
