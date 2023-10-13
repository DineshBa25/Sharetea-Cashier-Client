package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.Objects;

public class MainController {
    @FXML
    private StackPane mainContainer;
    @FXML
    private ImageView logoImageView;

    public void initialize() {
        Image logoImage = new Image(Objects.requireNonNull(getClass().getResource("/com/goattechnologies/pos/sharetea_logo.png")).toExternalForm());
        logoImageView.setImage(logoImage);
    }
    public void setView(Node node) {
        mainContainer.getChildren().setAll(node);
    }
}
