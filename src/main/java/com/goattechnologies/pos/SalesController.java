package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SalesController {
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextField startTimeTextField;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField endTimeTextField;



    /**
     * This method is called when the user clicks the "Generate Report" button.
     * It will parse the date and time fields and call generateSalesReport().
     */
    public void generateTimeWindow() {
        try {
            String startDateStr = startDatePicker.getValue().toString();
            String startTimeStr = startTimeTextField.getText();
            String endDateStr = endDatePicker.getValue().toString();
            String endTimeStr = endTimeTextField.getText();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDateTime = dateFormat.parse(startDateStr + " " + startTimeStr + ":00");
            Date endDateTime = dateFormat.parse(endDateStr + " " + endTimeStr + ":00");

            Timestamp startTime = new Timestamp(startDateTime.getTime());
            Timestamp endTime = new Timestamp(endDateTime.getTime());

            // Use startTime and endTime for your time window calculations or other processing
            System.out.println("Start Time: " + startTime);
            System.out.println("End Time: " + endTime);
            generateSalesReport(startTime, endTime);

        } catch (ParseException e) {
            System.err.println("Invalid date/time format.");
        }
    }

    /**
     * This method is called by generateTimeWindow() to generate the sales report.
     * @param startTime The start time of the time window.
     * @param endTime The end time of the time window.
     */
    public void generateSalesReport(Timestamp startTime, Timestamp endTime) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sales-report-view.fxml"));
            Node updateView = loader.load();

            SalesReportController salesReportController = loader.getController();
            salesReportController.initialize(startTime, endTime);

            Main.getMainController().setView(updateView);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method takes the user back to the statistics view.
     */
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("statistics-view.fxml")));
        Main.getMainController().setView(node);
    }
}
