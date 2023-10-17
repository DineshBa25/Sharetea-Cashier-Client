package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Objects;

/**
 * This class serves as the controller for the first login view in a Point of Sale (POS) application. It manages the
 * display and interaction with a text field for entering an employee ID and provides functionality for logging in.
 * @author Dinesh Balakrishnan
 */
public class Login1Controller {

    @FXML
    private TextField employeeID;

    @FXML
    private Text idPrompt;

    /**
     * Initializes the Login1Controller, setting up the text field and text.
     */
    public void initialize() {
        Font customFont = Font.font("Arial", 20);
        idPrompt.setFont(customFont);
        Font largerFont = new Font(28);
        employeeID.setFont(largerFont);
        employeeID.setAlignment(Pos.CENTER);
    }

    /**
     * Handles the "Login" button click event, validating the employee ID and logging in if valid.
     * @throws Exception if the employee ID is invalid
     */
    @FXML
    protected void onLoginClick() throws Exception {
        try {
            int username = Integer.parseInt(employeeID.getText());
            String employeeName = Main.dbManager.getEmployeeName(username);
            Employee.getInstance().setEmployeeName(employeeName);
            boolean isManager = Main.dbManager.isManager(username);
            Employee.getInstance().setManager(isManager);
            Node node;
            if(isManager) {
                node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login3-view.fxml")));
            }
            else {
                node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login2-view.fxml")));
            }
            Main.getMainController().setView(node);
        } catch (Exception e) {
            AlertUtil.showWarning("Login Warning", "Unable to Login", "Please enter a valid employee ID");
            idPrompt.setText("Incorrect Employee ID");
            idPrompt.setFill(Color.RED);
        }
    }
}
