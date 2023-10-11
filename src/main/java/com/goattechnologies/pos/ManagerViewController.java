package com.goattechnologies.pos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;

public class ManagerViewController {
    @FXML
    protected void onInventoryClick() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("inventory-view.fxml")));
        Main.getMainController().setView(node);
    }

    @FXML
    protected void onProductsClick() throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("products-view.fxml"));
        Main.getMainController().setView(node);
    }

    public void handleBackButton(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("login2-view.fxml"));
        Main.getMainController().setView(node);
    }

}
