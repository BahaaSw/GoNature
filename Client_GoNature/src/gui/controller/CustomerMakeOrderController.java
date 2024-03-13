package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class CustomerMakeOrderController implements Initializable {

	@FXML
	public ChoiceBox<String> parks_list;
	@FXML
	public TextField first_name_fld;
	@FXML
	public TextField last_name_fld;
	@FXML
	public TextField id_fld;
	@FXML
	public TextField phone_number_fld;
	@FXML
	public TextField email_fld;
	@FXML
	public DatePicker pick_visit_date;
	@FXML
	public ComboBox<String> pick_visit_time;
	@FXML
	public TextField number_of_visitors_fld;
	@FXML
	public ChoiceBox<String> visit_type;
	@FXML
	public Button make_order_btn;
	@FXML
	public Button cancel_order_btn;
	
	
	private ObservableList<String> timeForVisits=FXCollections.observableArrayList(
			"09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00"
			);
	
//	private ObservableList<VisitTypeEnum> visitTypesList=FXCollections.observableArrayList(
//			VisitTypeEnum.VisitorSoloVisit,
//			VisitTypeEnum.VisitorGroupVisit,
//			VisitTypeEnum.GuideGroupVisit
//			);
//	
//	private ObservableList<ParkNameEnum> parksList=FXCollections.observableArrayList(
//			ParkNameEnum.AgamonHaHula,
//			ParkNameEnum.CarmelForest,
//			ParkNameEnum.HermonMountain
//			);
	
	private TextFormatter<String> phoneFormat;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		parks_list.setItems(parksList);
		pick_visit_time.setItems(timeForVisits);
//		visit_type.setItems(visitTypesList);
		
		
	}
	
	
	public void onMakeOrderButtonClicked() {
		int isValid = validateFields();
		// Create an information alert
		if(isValid==1 || isValid ==2 ||isValid ==3) {
			// Create an information alert
	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Order Canceled");
	        alert.setHeaderText("FAILED!"); // No header text
	        alert.setContentText("Order was Canceled!");

	        // Show the alert and wait for user response
	        alert.showAndWait().ifPresent(response -> {
	            if (response == ButtonType.OK) {
//	                login_vbox.setVisible(false);
//	                options_vbox.setVisible(true);
	                // Perform action when user clicks OK
	            	
	            }
	        });
			return;
		}
		
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Created");
        alert.setHeaderText("SUCCESS!"); // No header text
        alert.setContentText("Order was Created!");

        // Show the alert and wait for user response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
//                login_vbox.setVisible(false);
//                options_vbox.setVisible(true);
                // Perform action when user clicks OK
            	
            }
        });
	}
	
	private int validateFields() {
		String phonePattern = "\\d{3}-\\d{3}-\\d{4}";
		String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
		boolean isValidPhone = Pattern.matches(phonePattern, phone_number_fld.getText());
		boolean isValidEmail = Pattern.matches(emailPattern, email_fld.getText());
		if(isValidPhone && isValidEmail)
			return 0;
		if(!isValidPhone && isValidEmail)
			return 1;
		if(isValidPhone && !isValidEmail)
			return 2;
		return 3;
		
	}
	
	public void onCancelOrderButtonClicked() {
		clearAllOrderFields();
		
		// Create an information alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Canceled");
        alert.setHeaderText("FAILED!"); // No header text
        alert.setContentText("Order was Canceled!");

        // Show the alert and wait for user response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
//                login_vbox.setVisible(false);
//                options_vbox.setVisible(true);
                // Perform action when user clicks OK
            	
            }
        });
	}
	
	private void clearAllOrderFields() {
		first_name_fld.clear();
		last_name_fld.clear();
		id_fld.clear();
		phone_number_fld.clear();
		email_fld.clear();
		number_of_visitors_fld.clear();
	}
	

}
