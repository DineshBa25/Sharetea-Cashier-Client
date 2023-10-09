package com.goattechnologies.pos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Login2Controller {
    @FXML
    protected void onLogin1Click() {
        // TODO: Implement
    }

    @FXML
    protected void onLogin2Click() {
        // TODO: Implement
    }

    public void handleBackButton(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("login1-view.fxml"));
        Main.getMainController().setView(node);
    }

}
