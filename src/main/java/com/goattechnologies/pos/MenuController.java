package com.goattechnologies.pos;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;

public class MenuController {

    @FXML
    private ListView<String> menuListView;

    @FXML
    private ListView<CartItem> cartListView;

    @FXML
    public void initialize() {
        Main.menuController = this;
        // Load the menu items (you should fetch these from the database)
        menuListView.getItems().addAll("Drink1", "Drink2", "Drink3"); // Sample data

        menuListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                showAddOnsMenu();
            }
        });

        cartListView.setItems(FXCollections.observableArrayList(Main.cart.getItems()));

        // Customize the display of cart items
        cartListView.setCellFactory(listView -> new ListCell<CartItem>() {
            @Override
            protected void updateItem(CartItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Customize the display of cart items
                    setText(item.getDrinkName() + " - Toppings: " + String.join(", ", item.getAddOns()) + " - Sweetness: " + item.getSweetnessLevel() + " - " + item.getIceLevel());
                }
            }
        });
    }

    private void showAddOnsMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-ons-view.fxml"));
            Node addOnsView = loader.load();

            AddOnsController addOnsController = loader.getController();
            addOnsController.setSelectedDrinkName(menuListView.getSelectionModel().getSelectedItem());

            Main.getMainController().setView(addOnsView);
        } catch(IOException e) {
            e.printStackTrace();
            // Handle the error appropriately (e.g., show an error dialog)
        }
    }

    @FXML
    protected void proceedToCheckout() throws IOException {
        // Load the order confirmation view
        Node orderConfirmationView = FXMLLoader.load(getClass().getResource("order-confirmation-view.fxml"));

        // Replace the current view with the order confirmation view
        Main.getMainController().setView(orderConfirmationView);
    }

    public void refreshCartView() {
        cartListView.setItems(FXCollections.observableArrayList(Main.cart.getItems()));
    }
}
