package com.goattechnologies.pos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

/**
 * The `Login2Controller` class is responsible for handling user interactions and logic
 * on the second screen of the login process. It welcomes the user and provides options
 * to start a new order or go back to the previous screen.
 *
 * @author Dinesh Balakrishnan, Ilham Aryawan, Moshin Khan
 */
public class Login2Controller {

    @FXML
    private Text name;

    /**
     * Initializes the Login2Controller. Sets the font and displays a welcome message with the user's name.
     */
    public void initialize() {
        Font customFont = Font.font("Arial", 20);
        name.setFont(customFont);

        String employeeName = Employee.getInstance().getEmployeeName();
        name.setText("Welcome, " + employeeName + "!");
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
     * Handles the event when the user clicks the "Back" button.
     * Navigates the user back to the previous login screen.
     *
     * @param event The event that triggered the action.
     * @throws IOException if there is an error while navigating back to the previous screen.
     */
    public void handleBackButton(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login1-view.fxml")));
        Main.getMainController().setView(node);
    }

}
