package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientApplication;
import client.ClientCommunication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import logic.AmountDivisionReport;
import logic.CancellationsReport;
import logic.ClientRequestDataContainer;
import logic.Employee;
import logic.Report;
import logic.ServerResponseBackToClient;
import logic.UsageReport;
import logic.VisitsReport;
import utils.AlertPopUp;
import utils.CurrentDateAndTime;
import utils.enums.ClientRequest;
import utils.enums.EmployeeTypeEnum;
import utils.enums.ParkNameEnum;
import utils.enums.ReportType;

public class CreateReportsScreenController implements Initializable {
	@FXML
	public Label dateLabel;
	@FXML
	public ComboBox<ReportType> selectReportComboBox;
	@FXML
	public ComboBox<ParkNameEnum> parkSelector;
	@FXML
	public ComboBox<String> yearSelector;
	@FXML
	public ComboBox<String> monthSelector;
	@FXML
	public Button generateReportButton;
	@FXML
	public HBox errorSection;
	@FXML
	public Label errorMessageLabel;

	@SuppressWarnings("unused")
	private ObservableList<ReportType> reportsList = FXCollections.observableArrayList(ReportType.UsageReport,
			ReportType.VisitsReports, ReportType.CancellationsReport, ReportType.TotalVisitorsReport);
	private ObservableList<ParkNameEnum> parksList = FXCollections.observableArrayList(ParkNameEnum.Banias,
			ParkNameEnum.Herodium, ParkNameEnum.Masada, ParkNameEnum.North, ParkNameEnum.South);
	private ObservableList<String> yearsList = FXCollections.observableArrayList("2024", "2023", "2022", "2021");
	private ObservableList<String> monthsList = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7",
			"8", "9", "10", "11", "12");

	private String selectedYear = "";
	private String selectedMonth = "";
	private ParkNameEnum selectedPark = ParkNameEnum.None;
	private Employee employee;
	private ReportType selectedReportType = ReportType.Unsupported;
	private ObservableList<ReportType> reportList = FXCollections.observableArrayList();

	public CreateReportsScreenController(Object employee) {
		this.employee = (Employee) employee;
		if (this.employee.getEmployeeType() == EmployeeTypeEnum.Park_Manager) {
			reportsList = FXCollections.observableArrayList(ReportType.UsageReport, ReportType.TotalVisitorsReport);
			parksList = FXCollections.observableArrayList(this.employee.getRelatedPark());
		} else {
			reportsList = FXCollections.observableArrayList(ReportType.UsageReport, ReportType.VisitsReports,
					ReportType.CancellationsReport, ReportType.TotalVisitorsReport);
			parksList = FXCollections.observableArrayList(ParkNameEnum.Banias, ParkNameEnum.Herodium,
					ParkNameEnum.Masada);
		}
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		hideErrorMessage();
		switch (employee.getEmployeeType()) {
		case Department_Manager:
			reportList = FXCollections.observableArrayList(ReportType.CancellationsReport, ReportType.VisitsReports);
			break;
		case Park_Manager:
			reportList = FXCollections.observableArrayList(ReportType.TotalVisitorsReport, ReportType.UsageReport);
			parksList = FXCollections.observableArrayList(employee.getRelatedPark());
			break;
		}
		selectReportComboBox.setItems(reportList);
		parkSelector.setItems(parksList);
		yearSelector.setItems(yearsList);
		monthSelector.setItems(monthsList);
		selectReportComboBox.setOnAction(this::onChangeReportSelection);
		parkSelector.setOnAction(this::onParkChangeSelection);
		yearSelector.setOnAction(this::onYearChangeSelection);
		monthSelector.setOnAction(this::onMonthChangeSelection);
	}

	private void onParkChangeSelection(ActionEvent event) {
		selectedPark = parkSelector.getValue();
	}

	private void onYearChangeSelection(ActionEvent event) {
		selectedYear = yearSelector.getValue();
	}

	private void onMonthChangeSelection(ActionEvent event) {
		selectedMonth = monthSelector.getValue();
	}

	private void onChangeReportSelection(ActionEvent event) {
		selectedReportType = selectReportComboBox.getValue();
		if (this.employee.getEmployeeType() == EmployeeTypeEnum.Department_Manager) {
			switch (selectedReportType) {
			case CancellationsReport:
				parksList = FXCollections.observableArrayList(ParkNameEnum.Banias, ParkNameEnum.Herodium,
						ParkNameEnum.Masada, ParkNameEnum.North, ParkNameEnum.South);
				break;
			default:
				parksList = FXCollections.observableArrayList(ParkNameEnum.Banias, ParkNameEnum.Herodium,
						ParkNameEnum.Masada);
				break;
			}
			parkSelector.setItems(parksList); // Set the items of parkSelector with the updated parksList
		}
	}

	@SuppressWarnings("incomplete-switch")
	public void onGenerateReportClicked() {
		Report report;
		ClientRequest reportRequest;
		if (!validateGuiFields()) {
			return;
		}
		hideErrorMessage();
		if (selectedReportType == ReportType.CancellationsReport) {
			report = new CancellationsReport(Integer.parseInt(selectedMonth), Integer.parseInt(selectedYear),
					selectedPark);
			reportRequest = ClientRequest.Create_Cancellations_Report;
		} else if (selectedReportType == ReportType.VisitsReports) {
			report = new VisitsReport(Integer.parseInt(selectedMonth), Integer.parseInt(selectedYear), selectedPark);
			reportRequest = ClientRequest.Create_Visits_Report;
		} else if (selectedReportType == ReportType.TotalVisitorsReport) {
			report = new AmountDivisionReport(Integer.parseInt(selectedMonth), Integer.parseInt(selectedYear),
					selectedPark);
			reportRequest = ClientRequest.Create_Total_Visitors_Report;
		} else {
			report = new UsageReport(Integer.parseInt(selectedMonth), Integer.parseInt(selectedYear), selectedPark);
			reportRequest = ClientRequest.Create_Usage_Report;
		}

		ClientRequestDataContainer request = new ClientRequestDataContainer(reportRequest, report);
		ClientApplication.client.accept(request);
		ServerResponseBackToClient response = ClientCommunication.responseFromServer;
		AlertPopUp alert;

		switch (response.getRensponse()) {
		case Report_Generated_Successfully:
			alert = new AlertPopUp(AlertType.INFORMATION, "Success", "Report generated successfully", "...");
			alert.showAndWait();
			break;
		case Report_Failed_Generate:
			alert = new AlertPopUp(AlertType.ERROR, "FAIL", "fail", "fail");
			alert.showAndWait();
			break;
		}
	}

	private boolean validateGuiFields() {
		if (selectedReportType == ReportType.Unsupported) {
			showErrorMessage("You must select reportType");
			return false;
		}
		if (selectedPark == ParkNameEnum.None) {
			showErrorMessage("You must select park");
			return false;
		}
		if (selectedYear.equals("")) {
			showErrorMessage("You must select year");
			return false;
		}
		if (selectedMonth.equals("")) {
			showErrorMessage("You must select month");
			return false;
		}

		return true;
	}

	private void hideErrorMessage() {
		errorMessageLabel.setText("");
		errorSection.setVisible(false);
	}

	@SuppressWarnings("unused")
	private void showErrorMessage(String error) {
		errorSection.setVisible(true);
		errorMessageLabel.setText(error);
	}

}
