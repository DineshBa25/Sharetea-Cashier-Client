package com.goattechnologies.pos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import java.io.IOException;

public class Login3Controller {

    @FXML
    private Text name;

    public void initialize() {
        Font customFont = Font.font("Arial", 20);
        name.setFont(customFont);
    }

    @FXML
    protected void onLogin1Click() throws IOException {
        //empty cart before starting a new order
        Main.cart.removeAllItemsInCart();

        Node node = FXMLLoader.load(getClass().getResource("menu-view.fxml"));
        Main.getMainController().setView(node);
    }

    @FXML
    protected void onLogin2Click() throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("manager-view.fxml"));
        Main.getMainController().setView(node);
    }

    public void handleBackButton(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("login1-view.fxml"));
        Main.getMainController().setView(node);
    }

}
