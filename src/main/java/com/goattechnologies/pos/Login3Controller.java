package com.goattechnologies.pos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.Objects;

/** This class serves as the controller for the third login view in a Point of Sale (POS) application. It manages the
 * display and interaction with a text field for entering an employee ID and provides functionality for logging in.
 * @Author Ilham Aryawan
 */
public class Login3Controller {

    @FXML
    private Text name;

    /**
     * Initializes the Login3Controller, setting up the text.
     */
    public void initialize() {
        Font customFont = Font.font("Arial", 20);
        name.setFont(customFont);
    }

    /** Handles the "Login" button click event, validating the employee ID and logging in if valid, goes to menu-view.
     * @throws Exception if the employee ID is invalid
     */
    @FXML
    protected void onLogin1Click() throws IOException {
        //empty cart before starting a new order
        Main.cart.removeAllItemsInCart();

        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu-view.fxml")));
        Main.getMainController().setView(node);
    }

    /** Handles the "Login" button click event, validating the employee ID and logging in if valid, goes to manager-view.
     * @throws Exception if the employee ID is invalid
     */
    @FXML
    protected void onLogin2Click() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("manager-view.fxml")));
        Main.getMainController().setView(node);
    }

    /** Handles the back button, returning to the first login view.
     * @throws IOException if the first login view cannot be loaded
     */
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login1-view.fxml")));
        Main.getMainController().setView(node);
    }

}
