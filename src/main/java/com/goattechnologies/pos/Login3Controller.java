package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.Objects;

/**
 * The `Login3Controller` class is responsible for handling user interactions and logic
 * on the third screen of the login process, which is specifically for managers. It provides
 * options to start a new order, access the manager's view, or go back to the previous screen.
 *
 * @author Dinesh Balakrishnan, Ilham Aryawan
 */
public class Login3Controller {

    @FXML
    private Text name;

    /**
     * Initializes the Login3Controller. Sets the font for the text elements.
     */
    public void initialize() {
        Font customFont = Font.font("Arial", 20);
        name.setFont(customFont);
    }

    /**
     * Handles the event when the user clicks the "Start New Order" button.
     * Clears the cart and navigates the user to the menu screen to start a new order.
     *
     * @throws IOException if there is an error while navigating to the menu screen.
     */
    @FXML
    protected void onLogin1Click() throws IOException {
        //empty cart before starting a new order
        Main.cart.removeAllItemsInCart();

        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu-view.fxml")));
        Main.getMainController().setView(node);
    }

    /**
     * Handles the event when the user clicks the "Manager's View" button.
     * Navigates the user to the manager's view.
     *
     * @throws IOException if there is an error while navigating to the manager's view.
     */
    @FXML
    protected void onLogin2Click() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("manager-view.fxml")));
        Main.getMainController().setView(node);
    }

    /**
     * Handles the event when the user clicks the "Back" button.
     * Navigates the user back to the previous login screen.
     *
     * @throws IOException if there is an error while navigating back to the previous screen.
     */
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login1-view.fxml")));
        Main.getMainController().setView(node);
    }

}
