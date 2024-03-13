package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AlertPopUp extends Alert {

	public AlertPopUp(AlertType type,String title, String header, String content) {
		super(type);
		this.setTitle(title);
		this.setHeaderText(header);
		this.setContentText(content);
//		setStyling();
		// TODO Auto-generated constructor stub
	}
	
	private void setStyling() {
		String alertType = this.getAlertType().toString();
		switch(alertType) {
		case "ERROR":
			seErrorAlert();
			return;
		case "INFORMATION":
			setInformationAlert();
			return;
		case "WARNING":
			setWarningAlert();
			return;
		}
	};
	
	private void seErrorAlert() {
		DialogPane pane = this.getDialogPane();
		//TODO: add Icons
		((Stage) pane.getScene().getWindow()).getIcons().add(new Image(""));
		//TODO: create css
		pane.getStylesheets().add(getClass().getResource("@errorAlert.css").toExternalForm());
	}
	
	private void setInformationAlert() {
		DialogPane pane = this.getDialogPane();
		//TODO: add Icons
		((Stage) pane.getScene().getWindow()).getIcons().add(new Image(""));
		//TODO: create css
		pane.getStylesheets().add(getClass().getResource("informationAlert.css").toExternalForm());
	}
	
	private void setWarningAlert() {
		DialogPane pane = this.getDialogPane();
		//TODO: add Icons
		((Stage) pane.getScene().getWindow()).getIcons().add(new Image(""));
		//TODO: create css
		pane.getStylesheets().add(getClass().getResource("warningAlert.css").toExternalForm());
	}

}
