package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import gui.view.ApplicationViewType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import logic.EntitiesContainer;
import logic.Guide;
import logic.SceneLoaderHelper;
import utils.CurrentDateAndTime;

public class ManageGuidesScreenController implements Initializable {
	@FXML
	public BorderPane screen;
	@FXML
	public Label dateLabel;
	@FXML
	public TextField guideIdField;
	@FXML
	public Button searchButton;
	@FXML
	public HBox errorSection;
	@FXML
	public Label errorMessageLabel;
	
	public ManageGuidesScreenController(BorderPane screen) {
		this.screen=screen;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		hideErrorMessage();
		
	}
	
	public void onSearchClicked() {
		SceneLoaderHelper GuiHelper= new SceneLoaderHelper();
		Guide guide = new Guide("1","guide","123456","gal","bitton","1234567890","gal@example.com");
		AnchorPane dashboard = GuiHelper.loadRightScreenToBorderPaneWithController(screen,"/gui/view/AddNewGuideScreen.fxml",
				ApplicationViewType.AddNewGuideScreen,new EntitiesContainer(guide));
		screen.setCenter(dashboard);
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
