package logic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import utils.enums.ParkNameEnum;
import utils.enums.ReportType;

public class CancellationsReport extends Report implements Serializable {
	private HashMap<Integer, ParkDailySummary> reportData;
	private double averageCancels = 0;
	private double medianCancels = 0;
	private byte[] blobPdfContent;

	public CancellationsReport(ReportType reportType) {
		super(reportType);
		// TODO Auto-generated constructor stub
	}

	public CancellationsReport(int month, int year, ParkNameEnum park) {
		super(ReportType.CancellationsReport);
		this.month = month;
		this.year = year;
		this.requestedPark = park;
	}

	public void showReport() {
	}

	public HashMap<Integer, ParkDailySummary> getReportData() {
		return reportData;
	}

	public void setReportData(HashMap<Integer, ParkDailySummary> reportData) {
		this.reportData = reportData;

		double sum = 0;
		for (ParkDailySummary summary : reportData.values()) {
			sum += summary.getCancelsOrders();
		}
		averageCancels = sum / reportData.size();

		List<Integer> cancelsList = reportData.values().stream().map(ParkDailySummary::getCancelsOrders).sorted()
				.collect(Collectors.toList());

		int size = cancelsList.size();
		if (size % 2 == 0) {
			medianCancels = (cancelsList.get(size / 2 - 1) + cancelsList.get(size / 2)) / 2.0;
		} else {
			medianCancels = cancelsList.get(size / 2);
		}

	}

	public byte[] getBlobPdfContent() {
		return blobPdfContent;
	}

	public void generateReportAsPdfBlob() {

		Document document = new Document();
		// Create a temporary file
		Path tempFilePath = null;
		File tempFile = null;
		try {
			tempFilePath = Files.createTempFile("cancellations_report", ".pdf");
			tempFile = tempFilePath.toFile();
			// Initialize PdfWriter to write to the temporary file
			PdfWriter.getInstance(document, new FileOutputStream(tempFile));
			document.open();

			// Header Font
			Font headerFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
			Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
			Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

			// Header
			Paragraph header = new Paragraph("Cancellations Report", headerFont);
			header.setAlignment(Element.ALIGN_CENTER);
			document.add(header);

			// Adding some space
			document.add(new Paragraph("\n"));

			// Park, Year, Month Info
			document.add(new Paragraph("Park: " + requestedPark.name(), boldFont));
			document.add(new Paragraph("Year: " + year, boldFont));
			document.add(new Paragraph("Month: " + month, boldFont));

			// Adding some space before the table
			document.add(new Paragraph("\n"));

			// Table
			PdfPTable table = new PdfPTable(4); // 4 columns.
			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(10f); // Space before table

			// Table headers
			String[] tableHeaders = { "Day", "Cancels", "Time Passed", "Total Orders" };
			for (String headerText : tableHeaders) {
				PdfPCell headerCell = new PdfPCell(new Paragraph(headerText, boldFont));
				headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(headerCell);
			}

			// Table data
			// Assuming reportData is a LinkedHashMap or TreeMap to maintain order
			for (Integer day : reportData.keySet()) {
				ParkDailySummary summary = reportData.get(day);
				table.addCell(new PdfPCell(new Paragraph(day.toString(), normalFont)));
				table.addCell(new PdfPCell(new Paragraph(String.valueOf(summary.getCancelsOrders()), normalFont)));
				table.addCell(new PdfPCell(new Paragraph(String.valueOf(summary.getTimePassedOrders()), normalFont)));
				table.addCell(new PdfPCell(new Paragraph(String.valueOf(summary.getTotalOrders()), normalFont)));
			}

			document.add(table);

			// Line Chart
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			for (Integer day : reportData.keySet()) {
				ParkDailySummary summary = reportData.get(day);
				dataset.addValue(summary.getCancelsOrders(), "Cancels", day);
				dataset.addValue(summary.getTimePassedOrders(), "Time Passed", day);
				dataset.addValue(summary.getTotalOrders(), "Total Orders", day);
			}

			JFreeChart lineChart = ChartFactory.createLineChart("Monthly Statistics", "Day", "Count", dataset,
					PlotOrientation.VERTICAL, true, true, false);

			// Get the plot and configure the range (Y) and domain (X) axes
			CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
			CategoryAxis domainAxis = plot.getDomainAxis();

			// Optional: rotate domain axis labels to make them more readable
			domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

			// Optional: set the lower margin or category margin if needed
			domainAxis.setLowerMargin(0.01);
			domainAxis.setCategoryMargin(0.01);
			// Save chart as image and add to document
			Path chartPath = Files.createTempFile("chart_", ".png");
			ChartUtils.saveChartAsPNG(chartPath.toFile(), lineChart, 500, 300);
			Image chartImage = Image.getInstance(chartPath.toString());
			document.add(chartImage);
			
			document.add(new Paragraph("\n"));

			// Cancels Average
			Paragraph cancelsAveragePara = new Paragraph(String.format("Cancels Average: %.2f", averageCancels), normalFont);
			cancelsAveragePara.setAlignment(Element.ALIGN_CENTER);
			document.add(cancelsAveragePara);

			// Cancels Median
			Paragraph cancelsMedianPara = new Paragraph(String.format("Cancels Median: %.2f", medianCancels), normalFont);
			cancelsMedianPara.setAlignment(Element.ALIGN_CENTER);
			document.add(cancelsMedianPara);


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			document.close();
		}

		// Now, read the content of the temporary file into a byte array
		try (FileInputStream input = new FileInputStream(tempFile)) {
			blobPdfContent = new byte[(int) tempFile.length()];
			input.read(blobPdfContent);

		} catch (Exception e) {
			e.printStackTrace();
			blobPdfContent = null; // Handle this case as appropriate
		}

	}

}

