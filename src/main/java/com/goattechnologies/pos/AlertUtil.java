package com.goattechnologies.pos;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

/**
 * A utility class for displaying warning alerts in a Point of Sale (POS) application.
 *
 * @author Mohsin Khan
 */
public class AlertUtil {

    /**
     * Displays a warning alert with the specified title, header, and content.
     *
     * @param title   The title of the warning alert.
     * @param header  The header text of the alert.
     * @param content The main content of the alert.
     */
    public static void showWarning(String title, String header, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.initStyle(StageStyle.UTILITY);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.getButtonTypes().setAll(ButtonType.OK);

        alert.showAndWait();
    }
}

