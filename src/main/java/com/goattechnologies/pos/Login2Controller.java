package com.goattechnologies.pos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

/** This class serves as the controller for the second login view in a Point of Sale (POS) application. It manages the
 * display and interaction with a text field for entering an employee ID and provides functionality for logging in.
 * @Author Dinesh Balakrishnan
 */
public class Login2Controller {

    @FXML
    private Text name;

    /**
     * Initializes the Login2Controller, setting up the text.
     */
    public void initialize() {
        Font customFont = Font.font("Arial", 20);
        name.setFont(customFont);

        String employeeName = Employee.getInstance().getEmployeeName();
        name.setText("Welcome, " + employeeName + "!");
    }

    /**
     * Handles the "Login" button click event, validating the employee ID and logging in if valid.
     * @throws Exception if the employee ID is invalid
     */
    @FXML
    protected void onLogin1Click() throws IOException {
        //empty cart before starting a new order
        Main.cart.removeAllItemsInCart();

        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu-view.fxml")));
        Main.getMainController().setView(node);
    }


    /** Handles the back button, returning to the first login view.
    // * @throws IOException if the first login view cannot be loaded
    // */
    public void handleBackButton(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login1-view.fxml")));
        Main.getMainController().setView(node);
    }

}
