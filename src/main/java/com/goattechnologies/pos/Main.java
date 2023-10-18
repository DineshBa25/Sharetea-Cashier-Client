package com.goattechnologies.pos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Main class for the POS application.
 * <p>
 * This class is responsible for initializing the database connection and loading the initial login view.
 * <p>
 * This class also contains static references to the controllers for the various views.
 * @Author Dinesh Balakrishnan
 */
public class Main extends Application {
  
    public static DatabaseManager dbManager = new DatabaseManager();
  
    private static MainController mainController;

    public static MenuController menuController;

    public static InventoryController inventoryController;

    public static ProductsController productsController;

    public static RestockController restockController;

    public static PopularityController popularityController;

    public static Cart cart = new Cart();

    // Used in supervisor tools to modify ingredients
    public static List<Ingredient> ingredients;

    public static List<Product> products;

    /**
     * Initializes the Main class, setting up the database connection and loading the initial login view.
     * @param stage the primary stage for this application
     * @throws IOException if the initial login view cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        dbManager.connect();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(loader.load(), 320, 240);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/goattechnologies/pos/main.css")).toExternalForm());

        mainController = loader.getController();

        // Set the preferred initial window size
        stage.setMinWidth(1000);
        stage.setMinHeight(800);

        stage.setTitle("POS Application");
        stage.setScene(scene);
        stage.show();

        // Load initial login view
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login1-view.fxml")));
        mainController.setView(node);
    }

    /**
     * Stops the application, closing the database connection.
     */
    @Override
    public void stop() {
        dbManager.disconnect();
    }

    /**
     * Returns the main controller for this application.
     * @return the main controller for this application
     */
    public static MainController getMainController() {
        return mainController;
    }

    /**
     * Returns the menu controller for this application.
     * @return the menu controller for this application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
