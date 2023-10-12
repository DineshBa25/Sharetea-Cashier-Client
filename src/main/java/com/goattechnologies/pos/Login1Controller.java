package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import java.io.IOException;

public class Login1Controller {

    @FXML
    private TextField employeeID;

    @FXML
    private Text idPrompt;

    public void initialize() {
        Font customFont = Font.font("Arial", 20);
        idPrompt.setFont(customFont);
    }

    @FXML
    protected void onLoginClick() throws Exception {
        try {
            int username = Integer.parseInt(employeeID.getText());
            String employeeName = Main.dbManager.getEmployeeName(username);
            Employee.getInstance().setEmployeeName(employeeName);
            boolean isManager = Main.dbManager.isManager(username);
            if(isManager) {
                Node node = FXMLLoader.load(getClass().getResource("login3-view.fxml"));
                Main.getMainController().setView(node);
            }
            else {
                Node node = FXMLLoader.load(getClass().getResource("login2-view.fxml"));
                Main.getMainController().setView(node);
            }
        } catch (Exception e) {
            e.printStackTrace();
            idPrompt.setText("Incorrect Employee ID");
            idPrompt.setFill(Color.RED);
        }
    }
}
