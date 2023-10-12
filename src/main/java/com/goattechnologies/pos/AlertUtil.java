package com.goattechnologies.pos;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

public class AlertUtil {

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

