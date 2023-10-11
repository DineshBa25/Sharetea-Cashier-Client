package com.goattechnologies.pos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
  
    public static DatabaseManager dbManager = new DatabaseManager();
  
    private static MainController mainController;

    public static MenuController menuController;

    public static InventoryController inventoryController;

    public static Cart cart = new Cart();

    @Override
    public void start(Stage stage) throws IOException {
        dbManager.connect();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(loader.load(), 320, 240);
        scene.getStylesheets().add(getClass().getResource("/com/goattechnologies/pos/main.css").toExternalForm());

        mainController = loader.getController();

        stage.setTitle("POS Application");
        stage.setScene(scene);
        stage.show();

        // Load initial login view
        Node node = FXMLLoader.load(getClass().getResource("login1-view.fxml"));
        mainController.setView(node);
    }

    @Override
    public void stop() {
        dbManager.disconnect();
    }

    public static MainController getMainController() {
        return mainController;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
