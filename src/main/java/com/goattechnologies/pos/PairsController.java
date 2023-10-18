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

/**
 * The `PairsController` class is responsible for handling user interactions and logic
 * on the pairs report screen. It provides options to generate a pairs report or go back
 * to the previous screen.
 * @author Cole Broberg
 */
public class PairsController {
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextField startTimeTextField;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField endTimeTextField;


    /**
     * Generates a time window based on the user's input and passes it to the pairs report view.
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
            generatePairsReport(startTime, endTime);

        } catch (ParseException e) {
            System.err.println("Invalid date/time format.");
        }
    }

    /**
     * Generates a pairs report view based on the given time window.
     *
     * @param startTime The start time of the time window.
     * @param endTime   The end time of the time window.
     */
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

    /**
     * Handles the event when the user clicks the "Back" button.
     * Navigates the user back to the statistics screen.
     *
     * @throws IOException if there is an error while navigating back to the previous screen.
     */
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("statistics-view.fxml")));
        Main.getMainController().setView(node);
    }
}
