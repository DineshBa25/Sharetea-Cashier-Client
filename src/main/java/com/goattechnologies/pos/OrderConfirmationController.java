package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
            case "0%":
                tipPercentage = 0.0;
                break;
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
                    AlertUtil.showWarning("Invalid Tip", "Invalid tip percentage", "Please enter a valid tip percentage");
                }
                break;
        }

        List<CartItem> items = Main.cart.getItems();

        Main.dbManager.addOrder(items, tipPercentage);

        Main.cart.removeAllItemsInCart();
        // Load the order confirmation view

        // Decide which view to return to depending on manager status
        Node backToStart = (
            Employee.getInstance().isManager() ?
                FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login3-view.fxml"))) :
                FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login2-view.fxml")))
        );

        // Replace the current view with the order confirmation view
        Main.getMainController().setView(backToStart);
    }
}
