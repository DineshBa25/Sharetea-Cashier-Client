package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;

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
                customTipField.setManaged(true);
            } else {
                customTipField.setVisible(false);
                customTipField.setManaged(false);
            }
        });
    }

    @FXML
    protected void completeOrder() throws IOException {
        double tipPercentage = 0.0;

        String selectedTip = tipComboBox.getSelectionModel().getSelectedItem();
        if (selectedTip == null) {
            // Handle the case where no tip is selected, e.g., show an alert to the user
            return;
        }
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

        Main.cart.removeAllItemsInCart();
        // Load the order confirmation view
        Node backToStart = FXMLLoader.load(getClass().getResource("login2-view.fxml"));

        // Replace the current view with the order confirmation view
        Main.getMainController().setView(backToStart);
    }
}
