package com.goattechnologies.pos;


import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Array;
import java.io.BufferedReader;
import java.io.FileReader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ExcessController {

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timeTextField;

    public void generateTimeWindow() {
        try {
            String dateStr = datePicker.getValue().toString();
            String timeStr = timeTextField.getText();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateTime = dateFormat.parse(dateStr + " " + timeStr + ":00");
            Timestamp timestamp = new Timestamp(dateTime.getTime());

            generateExcessReport(timestamp);

        } catch (ParseException e) {
            System.err.println("Invalid date/time format.");
        }
    }

    public void generateExcessReport(Timestamp startTime) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("excess-report-view.fxml"));
            Node updateView = loader.load();

            ExcessReportController excessReportController = loader.getController();
            excessReportController.initialize(startTime);

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
