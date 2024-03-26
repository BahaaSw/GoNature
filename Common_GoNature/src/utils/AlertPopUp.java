package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertPopUp extends Alert {

	public AlertPopUp(AlertType type,String title, String header, String content) {
		super(type);
		this.setTitle(title);
		this.setHeaderText(header);
		this.setContentText(content);
//		setStyling();
		// TODO Auto-generated constructor stub
	}
	
	public AlertPopUp(AlertType type, String title, String header, String content, ButtonType... buttons) {
		super(type);
		this.setTitle(title);
		this.setHeaderText(header);
		this.setContentText(content);
		this.getButtonTypes().setAll(buttons);
	}
	

}
