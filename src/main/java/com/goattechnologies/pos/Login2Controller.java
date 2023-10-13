package com.goattechnologies.pos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

public class Login2Controller {

    @FXML
    private Text name;

    public void initialize() {
        Font customFont = Font.font("Arial", 20);
        name.setFont(customFont);

        String employeeName = Employee.getInstance().getEmployeeName();
        name.setText("Welcome, " + employeeName + "!");
    }

    @FXML
    protected void onLogin1Click() throws IOException {
        //empty cart before starting a new order
        Main.cart.removeAllItemsInCart();

        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu-view.fxml")));
        Main.getMainController().setView(node);
    }


    public void handleBackButton(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login1-view.fxml")));
        Main.getMainController().setView(node);
    }

}
