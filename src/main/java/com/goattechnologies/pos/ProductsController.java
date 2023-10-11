package com.goattechnologies.pos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

import java.io.IOException;

public class ProductsController {
    @FXML
    private ListView<String> menuListView;

    @FXML
    private ListView<CartItem> cartListView;

    @FXML
    public void initialize() {

    }

    public void handleBackButton(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("manager-view.fxml"));
        Main.getMainController().setView(node);
    }
}
