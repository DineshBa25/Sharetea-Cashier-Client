package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class Login1Controller {
    @FXML
    protected void onLoginClick() throws Exception {
        Node node = FXMLLoader.load(getClass().getResource("login2-view.fxml"));
        Main.getMainController().setView(node);
    }
}
