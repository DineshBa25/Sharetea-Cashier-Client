package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * The `Login1Controller` class is responsible for handling user interactions and logic
 * on the first screen of the login process. It allows users to input their employee ID,
 * performs validation, and directs the user to the appropriate next screen based on their role.
 *
 * @author Ilham Aryawan, Dinesh Balakrishnan, Cole Broberg
 */
public class Login1Controller {

    @FXML
    private TextField employeeID;

    @FXML
    private Text idPrompt;

    /**
     * Initializes the Login1Controller. Sets fonts and alignment for the user interface elements.
     */
    public void initialize() {
        Font customFont = Font.font("Arial", 20);
        idPrompt.setFont(customFont);
        Font largerFont = new Font(28);
        employeeID.setFont(largerFont);
        employeeID.setAlignment(Pos.CENTER);
    }

    /**
     * Handles the event when the user clicks the login button.
     * Validates the entered employee ID, retrieves the employee's name and role,
     * and navigates the user to the appropriate next screen.
     *
     * @throws Exception if there is an error during the login process.
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
