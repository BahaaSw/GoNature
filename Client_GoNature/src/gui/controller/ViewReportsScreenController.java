package gui.controller;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;

import client.ClientApplication;
import client.ClientCommunication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import logic.CancellationsReport;
import logic.ClientRequestDataContainer;
import logic.ParkDailySummary;
import logic.Report;
import logic.ServerResponseBackToClient;
import utils.CurrentDateAndTime;
import utils.enums.ClientRequest;
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

	private ObservableList<ReportType> reportsList = FXCollections.observableArrayList(ReportType.UsageReport,
			ReportType.VisitsReports, ReportType.CancellationsReport, ReportType.TotalVisitorsReport);
	private ObservableList<ParkNameEnum> parksList = FXCollections.observableArrayList(ParkNameEnum.Banias,
			ParkNameEnum.Herodium, ParkNameEnum.Masada);
	private ObservableList<String> yearsList = FXCollections.observableArrayList("2024", "2023", "2022", "2021");
	private ObservableList<String> monthsList = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7",
			"8", "9", "10", "11", "12");

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
		selectedReport = reportSelector.getValue();
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

	public void onViewReportClicked() {

		CancellationsReport requestedReport = new CancellationsReport(Integer.parseInt(selectedMonth),
				Integer.parseInt(selectedYear), selectedPark);
		ClientRequest reportToOpen = (selectedReport == ReportType.CancellationsReport)
				? ClientRequest.Import_Cancellations_Report
				: (selectedReport == ReportType.TotalVisitorsReport) ? ClientRequest.Import_Total_Visitors_Report
						: (selectedReport == ReportType.UsageReport) ? ClientRequest.Import_Usage_Report
								: (selectedReport == ReportType.VisitsReports) ? ClientRequest.Import_Visits_Report
										: null;
		
		ClientRequestDataContainer request = new ClientRequestDataContainer(reportToOpen, requestedReport);
		ClientApplication.client.accept(request);
		ServerResponseBackToClient response = ClientCommunication.responseFromServer;

		switch (response.getRensponse()) {
		case Such_Report_Not_Found:
			return;
		case Cancellations_Report_Found:
			try {
				// Create a temp file
				String reportName = selectedReport+"_"+selectedYear+"_"+selectedMonth;
				File tempFile = File.createTempFile(reportName, ".pdf");
				tempFile.deleteOnExit(); // Request the file be deleted when the application exits

				// Write the PDF content to the temp file
				try (FileOutputStream fos = new FileOutputStream(tempFile)) {
					fos.write((byte[]) response.getMessage()); // Assuming response.getMessage() returns the correct
																// byte array for the PDF
				}

				// Open the file with the default system viewer
				Desktop.getDesktop().open(tempFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}

}
