package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import logic.Employee;
import utils.CurrentDateAndTime;
import utils.enums.ReportType;

public class CreateReportsScreenController implements Initializable {
	@FXML
	public Label dateLabel;
	@FXML
	public ComboBox<ReportType> selectReportComboBox;
	@FXML
	public Button generateReportButton;
	@FXML
	public HBox errorSection;
	@FXML
	public Label errorMessageLabel;
	
	private Employee employee;
	private ReportType selectedReportType=ReportType.Unsupported;
	private ObservableList<ReportType> reportList = FXCollections.observableArrayList();
	
	public CreateReportsScreenController(Object employee) {
		this.employee=(Employee)employee;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		hideErrorMessage();
		switch(employee.getEmployeeType()) {
		case Department_Manager:
			reportList = FXCollections.observableArrayList(
					ReportType.CancellationsReport,
					ReportType.VisitsReports
					);
			break;
		case Park_Manager:
			reportList = FXCollections.observableArrayList(
					ReportType.TotalVisitorsReport,
					ReportType.UsageReport
					);
			break;
		}
		selectReportComboBox.getItems().addAll(reportList);
		selectReportComboBox.setOnAction(this::onChangeReportSelection);
	}
	
	private void onChangeReportSelection(ActionEvent event) {
		selectedReportType=selectReportComboBox.getValue();
	}
	
	public void onGenerateReportClicked() {
		//TODO: implement generation of report
		//TODO: the client thinks it should always succeed. 
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
