package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.Objects;

/**
 * This class serves as the controller for the main view in a Point of Sale (POS) application. It manages the display
 * and interaction with the main view, which contains a logo and a container for the various views.
 * @Author Dinesh Balakrishnan
 */
public class MainController {
    @FXML
    private StackPane mainContainer;
    @FXML
    private ImageView logoImageView;

    /**
     * Initializes the MainController, setting up the logo image.
     */
    public void initialize() {
        Image logoImage = new Image(Objects.requireNonNull(getClass().getResource("/com/goattechnologies/pos/sharetea_logo.png")).toExternalForm());
        logoImageView.setImage(logoImage);
    }
    /** Sets the view in the main container to the specified node.
     * @param node the node to set as the view in the main container
     */
    public void setView(Node node) {
        mainContainer.getChildren().setAll(node);
    }
}
