package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import logic.Report;
import utils.CurrentDateAndTime;
import utils.enums.ParkNameEnum;
import utils.enums.ReportType;

public class ViewReportsScreenController implements Initializable {

	@FXML
	public Label dateLabel;
	@FXML
	public ComboBox<ReportType> reportSelector;
	@FXML
	public ComboBox<ParkNameEnum> parkSelector;
	@FXML
	public ComboBox<String> yearSelector;
	@FXML
	public ComboBox<String> monthSelector;
	
	private ObservableList<ReportType> reportsList=FXCollections.observableArrayList(ReportType.UsageReport,ReportType.VisitsReports);
	private ObservableList<ParkNameEnum> parksList=FXCollections.observableArrayList(ParkNameEnum.Banias,ParkNameEnum.Herodium,ParkNameEnum.Masada);
	private ObservableList<String> yearsList=FXCollections.observableArrayList("2024","2023","2022","2021");
	private ObservableList<String> monthsList=FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10","11","12");
	
	private Report requestedReport;
	private ReportType selectedReport = ReportType.Unsupported;
	private ParkNameEnum selectedPark = ParkNameEnum.None;
	private String selectedYear = "";
	private String selectedMonth = "";
	
	public ViewReportsScreenController() {
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		reportSelector.getItems().addAll(reportsList);
		parkSelector.getItems().addAll(parksList);
		yearSelector.getItems().addAll(yearsList);
		monthSelector.getItems().addAll(monthsList);
		
		reportSelector.setOnAction(this::onReportChangeSelection);
		parkSelector.setOnAction(this::onParkChangeSelection);
		yearSelector.setOnAction(this::onYearChangeSelection);
		monthSelector.setOnAction(this::onMonthChangeSelection);
		
	}
	
	private void onReportChangeSelection(ActionEvent event) {
		selectedReport=reportSelector.getValue();
	}
	
	private void onParkChangeSelection(ActionEvent event) {
		selectedPark=parkSelector.getValue();
	}

	private void onYearChangeSelection(ActionEvent event) {
		selectedYear=yearSelector.getValue();
	}
	
	private void onMonthChangeSelection(ActionEvent event) {
		selectedMonth=monthSelector.getValue();
	}

	public void onViewReportClicked() {
		//TODO: import relevant report data from database and open new report screen!
		/* Block parent stage until child stage closes */
//		newStage.initModality(Modality.WINDOW_MODAL);
//		newStage.initOwner(currentWindow);
	}

}
