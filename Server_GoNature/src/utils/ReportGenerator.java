package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import logic.AmountDivisionReport;
import logic.CancellationsReport;
import logic.ParkDailySummary;
import logic.VisitsReport;

public class ReportGenerator {

	public static byte[] generateCancellationsReportAsPdfBlob(CancellationsReport report) {

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
			document.add(new Paragraph("Park: " + report.getRequestedPark().name(), boldFont));
			document.add(new Paragraph("Year: " + report.getYear(), boldFont));
			document.add(new Paragraph("Month: " + report.getMonth(), boldFont));

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
			for (Integer day : report.getReportData().keySet()) {
				ParkDailySummary summary = report.getReportData().get(day);
				table.addCell(new PdfPCell(new Paragraph(day.toString(), normalFont)));
				table.addCell(new PdfPCell(new Paragraph(String.valueOf(summary.getCancelsOrders()), normalFont)));
				table.addCell(new PdfPCell(new Paragraph(String.valueOf(summary.getTimePassedOrders()), normalFont)));
				table.addCell(new PdfPCell(new Paragraph(String.valueOf(summary.getTotalOrders()), normalFont)));
			}

			document.add(table);

			// Line Chart
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			for (Integer day : report.getReportData().keySet()) {
				ParkDailySummary summary = report.getReportData().get(day);
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
			Paragraph cancelsAveragePara = new Paragraph(String.format("Cancels Average: %.2f", report.getAverageCancels()), normalFont);
			cancelsAveragePara.setAlignment(Element.ALIGN_CENTER);
			document.add(cancelsAveragePara);

			// Cancels Median
			Paragraph cancelsMedianPara = new Paragraph(String.format("Cancels Median: %.2f", report.getMedianCancels()), normalFont);
			cancelsMedianPara.setAlignment(Element.ALIGN_CENTER);
			document.add(cancelsMedianPara);


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			document.close();
		}

		// Now, read the content of the temporary file into a byte array
		try (FileInputStream input = new FileInputStream(tempFile)) {
			byte[] fileAsBytes = new byte[(int) tempFile.length()];
			input.read(fileAsBytes);
			return fileAsBytes;

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

	}
	
	public static byte[] generateVisitsReportAsPdf(VisitsReport report) {
		Document document = new Document();
		// Create a temporary file
		Path tempFilePath = null;
		File tempFile = null;
		try {
			tempFilePath = Files.createTempFile("visits_report", ".pdf");
			tempFile = tempFilePath.toFile();
			// Initialize PdfWriter to write to the temporary file
			PdfWriter.getInstance(document, new FileOutputStream(tempFile));
			document.open();

			// Header Font
			Font headerFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
			Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
			Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

			// Header
			Paragraph header = new Paragraph("Visits Report", headerFont);
			header.setAlignment(Element.ALIGN_CENTER);
			document.add(header);

			// Adding some space
			document.add(new Paragraph("\n"));

			document.add(new Paragraph("This report contains total amount of visitors distributed by visitors type. ", boldFont));
			document.add(new Paragraph("In addition, this report includes the total idle time inside the park. ", boldFont));
			
			// Park, Year, Month Info
			document.add(new Paragraph("Park: " + report.getRequestedPark().name(), boldFont));
			document.add(new Paragraph("Year: " + report.getYear(), boldFont));
			document.add(new Paragraph("Month: " + report.getMonth(), boldFont));

			// Adding some space before the table
			document.add(new Paragraph("\n"));

			// Table with 4 columns
			PdfPTable table = new PdfPTable(4); 
			table.setWidthPercentage(100); // Set table width to 100% of the page
			table.setSpacingBefore(10f); // Set spacing before the table

			// Define the headers
			String[] headers = new String[] {"Hour", "Solo Visits", "Family Visits", "Group Visits"};

			// Define column widths to match the layout in the image
			float[] columnWidths = new float[] {2f, 2f, 2f, 2f};
			table.setWidths(columnWidths);

			// Add the header row
			for (String head : headers) {
			    PdfPCell cell = new PdfPCell(new Phrase(head));
			    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			    cell.setPadding(5); // Set padding for a bit of space, adjust as needed
			    table.addCell(cell);
			}

			// Add the data rows
			String[] hours = new String[] {
			    "08:00-08:59",
			    "09:00-09:59",
			    "10:00-10:59",
			    "11:00-11:59",
			    "12:00-12:59",
			    "13:00-13:59",
			    "14:00-14:59",
			    "15:00-16:00",
			};

			ArrayList<Integer> solo = report.getTotalVisitsByEnterTime().get(0);
			ArrayList<Integer> soloPreorder = report.getTotalVisitsByEnterTime().get(1);
//			ArrayList<Integer> family = report.getTotalVisitsByEnterTime().get(2);
			ArrayList<Integer> familyPreorder = report.getTotalVisitsByEnterTime().get(3);
			ArrayList<Integer> group = report.getTotalVisitsByEnterTime().get(4);
			ArrayList<Integer> groupPreorder = report.getTotalVisitsByEnterTime().get(5);
			int i=0;
			// For each hour slot, add a row to the table
			for (String hour : hours) {
			    
			    // Add hour cell
			    PdfPCell cellHour = new PdfPCell(new Phrase(hour));
			    cellHour.setHorizontalAlignment(Element.ALIGN_CENTER);
			    table.addCell(cellHour);
			    
			    Integer totalSolo = solo.get(i)+soloPreorder.get(i);
			    Integer totalFamily = familyPreorder.get(i);
			    Integer totalGroup = group.get(i)+groupPreorder.get(i);
			    
		        PdfPCell cellCount = new PdfPCell(new Phrase(String.valueOf(totalSolo)));
		        cellCount.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(cellCount);
		        
		        cellCount = new PdfPCell(new Phrase(String.valueOf(totalFamily)));
		        cellCount.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(cellCount);
		        
		        cellCount = new PdfPCell(new Phrase(String.valueOf(totalGroup)));
		        cellCount.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(cellCount);
		        i++;
			}

			// Add table to document
			document.add(table);
			
			document.add(new Paragraph("\n\n"));
			
			table = new PdfPTable(4);
			table.setWidthPercentage(100); // Set table width to 100% of the page
			table.setSpacingBefore(10f); // Set spacing before the table

			// Define the headers
			headers = new String[] {"Idle Time", "Solo Visits", "Family Visits", "Group Visits"};

			// Define column widths to match the layout in the image
			columnWidths = new float[] {2f, 2f, 2f, 2f};
			table.setWidths(columnWidths);

			// Add the header row
			for (String head : headers) {
			    PdfPCell cell = new PdfPCell(new Phrase(head));
			    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			    cell.setPadding(5); // Set padding for a bit of space, adjust as needed
			    table.addCell(cell);
			}

			// Add the data rows
			String[] idleTime = new String[] {
			    "0 up to 1",
			    "1 up to 2",
			    "2 up to 3",
			    "3 up to 4",
			    "4+",
			};

			int time=0;
			// For each hour slot, add a row to the table
			for (String idle : idleTime) {
			    
			    // Add hour cell
			    PdfPCell cellHour = new PdfPCell(new Phrase(idle));
			    cellHour.setHorizontalAlignment(Element.ALIGN_CENTER);
			    table.addCell(cellHour);
			    
			    ArrayList<Integer> currentIdle = report.getIdleTimeData(time++);
			    // Add visits count cells
			    for (int count : currentIdle) {
			        PdfPCell cellCount = new PdfPCell(new Phrase(String.valueOf(count)));
			        cellCount.setHorizontalAlignment(Element.ALIGN_CENTER);
			        table.addCell(cellCount);
			    }
			}

			// Add table to document
			document.add(table);
			

			// Line Chart
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			for (int j=0;j<5;j++) {
				ArrayList<Integer> data = report.getIdleTimeData(j);
				String column = String.format("%s up to %s",j,j+1);
				if(j==4)
					column = "4+";
				dataset.addValue(data.get(0), "Solo",column);
				dataset.addValue(data.get(1),"Family",column);
				dataset.addValue(data.get(2),"Group",column);
			}

			JFreeChart chart = createBarChart(dataset,"Total Visits Distributed By Idle Time","Idle Time","Visits");
			Path chartPath = Files.createTempFile("chart_", ".png");
			ChartUtils.saveChartAsPNG(chartPath.toFile(), chart, 450, 400);
			
			// Add the grouped bar chart image to the PDF
			Image chartImage = Image.getInstance(chartPath.toString());
			document.add(chartImage);

			// Add the second grouped bar chart
			dataset = new DefaultCategoryDataset();
			for (int j=0;j<8;j++) {
				ArrayList<Integer> data = report.getEnterTimeData(j);
				String column = String.format("%s:00-%s:59",8+j,8+j+1);
			    Integer totalSolo = solo.get(j)+soloPreorder.get(j);
			    Integer totalFamily = familyPreorder.get(j);
			    Integer totalGroup = group.get(j)+groupPreorder.get(j);
				if(j==7)
					column="15:00-16:00";
				dataset.addValue(totalSolo, "Solo",column);
				dataset.addValue(totalFamily,"Family",column);
				dataset.addValue(totalGroup,"Group",column);
			}

			chart = createBarChart(dataset,"Total Visits Distributed By Enter Time","Enter Time","Visits");
			chartPath = Files.createTempFile("chart_2", ".png");
			ChartUtils.saveChartAsPNG(chartPath.toFile(), chart, 450, 400);
			
			// Add the grouped bar chart image to the PDF
			chartImage = Image.getInstance(chartPath.toString());
			document.add(chartImage);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			document.close();
		}

		// Now, read the content of the temporary file into a byte array
		try (FileInputStream input = new FileInputStream(tempFile)) {
			byte[] fileAsBytes = new byte[(int) tempFile.length()];
			input.read(fileAsBytes);
			return fileAsBytes;

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
	}
	
	public static byte[] generateTotalVisitorsAmountReportAsPdf(AmountDivisionReport report) {
		Document document = new Document();
		// Create a temporary file
		Path tempFilePath = null;
		File tempFile = null;
		try {
			tempFilePath = Files.createTempFile("TotalAmount_report", ".pdf");
			tempFile = tempFilePath.toFile();
			// Initialize PdfWriter to write to the temporary file
			PdfWriter.getInstance(document, new FileOutputStream(tempFile));
			document.open();

			// Header Font
			Font headerFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
			Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
			// Header
			Paragraph header = new Paragraph("Total amount of travlers report", headerFont);
			header.setAlignment(Element.ALIGN_CENTER);
			document.add(header);

			// Adding some space
			document.add(new Paragraph("\n"));

			// Park, Year, Month Info
			document.add(new Paragraph("Park: " + report.getRequestedPark().name(), boldFont));
			document.add(new Paragraph("Year: " + report.getYear(), boldFont));
			document.add(new Paragraph("Month: " + report.getMonth(), boldFont));

			// Adding some space before the table
			document.add(new Paragraph("\n"));

			// pie Chart
			DefaultPieDataset dataset = new DefaultPieDataset();
			dataset.setValue("Solo", report.getReportData().getAmountSolo());
	        dataset.setValue("Family", report.getReportData().getAmountFamily());
	        dataset.setValue("Group", report.getReportData().getAmountGroup());

	        JFreeChart pieChart = ChartFactory.createPieChart(
	                "Order Distribution", // chart title
	                dataset, // dataset
	                true, // include legend
	                true,
	                false);


			// Save chart as image and add to document
			Path chartPath = Files.createTempFile("chart_", ".png");
			ChartUtils.saveChartAsPNG(chartPath.toFile(), pieChart, 500, 300);
			Image chartImage = Image.getInstance(chartPath.toString());
			document.add(chartImage);
			document.add(new Paragraph("\n"));

		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			document.close();
		}

		// Now, read the content of the temporary file into a byte array
		try (FileInputStream input = new FileInputStream(tempFile)) {
			byte[] pdfAsBytes = new byte[(int) tempFile.length()];
			input.read(pdfAsBytes);
			
			return pdfAsBytes;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}


	}
	
	public static byte[] generateUsageReportAsPdf() {
		return null;
	}
	
	private static JFreeChart createBarChart(final CategoryDataset dataset,String charTitle, String xAxis,
			String yAxis) {
	    // Create the chart
	    JFreeChart chart = ChartFactory.createBarChart(
	    		charTitle,      // Chart title
	    		xAxis,                 // Domain axis label
	    		yAxis,                    // Range axis label
	            dataset,                     // Data
	            PlotOrientation.VERTICAL,
	            true,                        // Include legend
	            true,
	            false);

	    // Customizations for the chart, such as color, etc.
	    CategoryPlot plot = (CategoryPlot) chart.getPlot();
	    CategoryAxis domainAxis = plot.getDomainAxis();

	    // Rotate labels for better fit if needed
	    domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
	    BarRenderer renderer = (BarRenderer) plot.getRenderer();

	    // Custom renderer to display the grouped bars
	    renderer.setItemMargin(0.1); // Gap between groups

	    return chart;
	}
	
}
