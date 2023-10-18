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
import java.util.Date;
import java.util.Objects;

/**
 * The `PopularityController` class is responsible for handling user interactions and logic
 * on the popularity report screen. It provides options to generate a popularity report or go back
 * to the previous screen.
 * @Author Mohsin Khan
 */
public class PopularityController {
    @FXML
    private TextField numberOfItems;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextField startTimeTextField;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField endTimeTextField;

    /**
     * Generates a time window based on the user's input and passes it to the popularity report view.
     */
    public void generateTimeWindow() {
        Main.popularityController = this;
        try {
            Integer numberItems = Integer.parseInt(numberOfItems.getText());
            String startDateStr = startDatePicker.getValue().toString();
            String startTimeStr = startTimeTextField.getText();
            String endDateStr = endDatePicker.getValue().toString();
            String endTimeStr = endTimeTextField.getText();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDateTime = dateFormat.parse(startDateStr + " " + startTimeStr + ":00");
            Date endDateTime = dateFormat.parse(endDateStr + " " + endTimeStr + ":00");

            Timestamp startTime = new Timestamp(startDateTime.getTime());
            Timestamp endTime = new Timestamp(endDateTime.getTime());
            generatePopularityReport(startTime, endTime, numberItems);
        } catch (ParseException e) {
            AlertUtil.showWarning("Invalid Input", "Date-Time or Number Issue", "Make sure times are in HH:MM format");
        }
    }

    /**
     * Generates a popularity report view based on the given time window.
     *
     * @param startTime The start time of the time window.
     * @param endTime   The end time of the time window.
     * @param numItems  The number of items to display
     */
    public void generatePopularityReport(Timestamp startTime, Timestamp endTime, Integer numItems) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("popularity-table-view.fxml"));
            Node updateView = loader.load();
            PopularityTableController popularityTableController = loader.getController();

            popularityTableController.setPopularityMap(Main.dbManager.getPopularity(startTime, endTime, numItems));
            popularityTableController.loadData();
            Main.getMainController().setView(updateView);
        } catch (IOException e) {
            AlertUtil.showWarning("Invalid Input", "Date-Time or Number Issue", "Make sure times are in HH:MM format" );
        } catch (NullPointerException e) {
            AlertUtil.showWarning("Unable to generate report", "Failed to load FXML file", "Please try again later.");
        }
    }

    /**
     * Handles the "Back" button click event, returning to the statistics view.
     * @throws IOException if the statistics view cannot be loaded
     */
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("statistics-view.fxml")));
        Main.getMainController().setView(node);
    }
}
