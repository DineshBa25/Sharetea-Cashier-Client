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

public class PairsController {
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextField startTimeTextField;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField endTimeTextField;



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
            generatePairsReport(startTime, endTime);

        } catch (ParseException e) {
            System.err.println("Invalid date/time format.");
        }
    }

    public void generatePairsReport(Timestamp startTime, Timestamp endTime) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pairs-report-view.fxml"));
            Node updateView = loader.load();

            PairsReportController pairsReportController = loader.getController();
            pairsReportController.initialize(startTime, endTime);

            Main.getMainController().setView(updateView);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("statistics-view.fxml")));
        Main.getMainController().setView(node);
    }
}
