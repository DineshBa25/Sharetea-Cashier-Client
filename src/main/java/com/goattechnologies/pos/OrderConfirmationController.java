package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class OrderConfirmationController {

    @FXML
    private ComboBox<String> tipComboBox;
    @FXML
    private TextField customTipField;
    @FXML
    private CheckBox printReceiptCheckBox;

    @FXML
    public void initialize() {
        // Handle changes in the tip ComboBox
        tipComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Custom".equals(newValue)) {
                customTipField.setVisible(true);
            } else {
                customTipField.setVisible(false);
            }
        });
    }

    @FXML
    protected void completeOrder() {
        double tipPercentage = 0.0;

        String selectedTip = tipComboBox.getSelectionModel().getSelectedItem();
        switch (selectedTip) {
            case "10%":
                tipPercentage = 0.10;
                break;
            case "15%":
                tipPercentage = 0.15;
                break;
            case "20%":
                tipPercentage = 0.20;
                break;
            case "Custom":
                try {
                    tipPercentage = Double.parseDouble(customTipField.getText()) / 100;
                } catch (NumberFormatException e) {
                    // Handle incorrect input (e.g., show an error dialog)
                }
                break;
        }

        // Apply the tip to the order (add your logic here)
        // ...

        // Check if receipt should be printed
        if (printReceiptCheckBox.isSelected()) {
            // Print the receipt (add your logic here)
            // ...
        }

        // Navigate to "Thank You" page or return to the main menu
        // ...
    }
}
