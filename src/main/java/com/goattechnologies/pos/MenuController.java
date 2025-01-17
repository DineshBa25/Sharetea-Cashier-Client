package com.goattechnologies.pos;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * This class serves as the controller for the menu view in a Point of Sale (POS) application. It manages the display
 * and interaction with the screen a employee would see after logging in and starting an order.
 * @Author Dinesh Balakrishnan
 */
public class MenuController {

    @FXML
    private ListView<String> menuListView;

    @FXML
    private ListView<CartItem> cartListView;

    /**
     * Initializes the MenuController, setting up the menu list view.
     */
    @FXML
    public void initialize() {
        Main.menuController = this;
        // Load the menu items (you should fetch these from the database)
        List<String> products;

        // Getting menu items from the database
        products = Main.dbManager.getProductNames();


        menuListView.getItems().addAll(products); // Sample data

        menuListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                showAddOnsMenu();
            }
        });

        cartListView.setItems(FXCollections.observableArrayList(Main.cart.getItems()));
        cartListView.setPrefHeight(250);

        cartListView.setCellFactory(listView -> new ListCell<>() {
            private final HBox content = new HBox();
            private final Label deleteLabel = new Label("Delete");

            {
                deleteLabel.setStyle("-fx-text-fill: red; -fx-cursor: hand;"); // Make the label red and use hand cursor
                deleteLabel.setOnMouseClicked(event -> {
                    CartItem itemToRemove = getItem();
                    Main.cart.removeItem(itemToRemove);
                    refreshCartView();
                });
                content.setSpacing(10);
                content.getChildren().addAll(new Label(), deleteLabel);
            }
            /**
             * Updates the cell with the specified item and empty status.
             * @param item the item to display in the cell
             * @param empty whether or not this cell represents data from the list. If it is empty, then it does not
             *              represent any domain data, but is a cell being used to render an "empty" row.
             */
            @Override
            protected void updateItem(CartItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ((Label) content.getChildren().get(0)).setText(item.getDrinkName() + " - Toppings: "
                            + String.join(", ", item.getAddOns()) + " - Sweetness: " + item.getSweetnessLevel()
                            + " - " + item.getIceLevel());
                    setGraphic(content);
                    setText(null);
                }
            }
        });
    }

    /**
     * Shows the add-ons menu for the selected drink.
     */
    private void showAddOnsMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-ons-view.fxml"));
            Node addOnsView = loader.load();

            AddOnsController addOnsController = loader.getController();
            addOnsController.setSelectedDrinkName(menuListView.getSelectionModel().getSelectedItem());

            Main.getMainController().setView(addOnsView);
        } catch(IOException e) {
            AlertUtil.showWarning("Error", "Error loading add-ons view", "Please try again later: "+ e.getMessage());
        }
    }

    /**
     * Handles the "Proceed to Checkout" button click event, going to order-confirmation-view.
     * @throws IOException if the order-confirmation-view cannot be loaded
     */
    @FXML
    protected void proceedToCheckout() throws IOException {
        if(Main.cart.isEmpty()) {
            AlertUtil.showWarning("Cart Warning", "Cart is empty", "Please add items to the cart before proceeding to checkout");
            return;
        }

        Node orderConfirmationView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("order-confirmation-view.fxml")));
        Main.getMainController().setView(orderConfirmationView);
    }

    /**
     * Handles the "Back" button click event, returning to the respective employee/manager login view.
     * @throws IOException if the third login view cannot be loaded
     */
    @FXML
    protected void handleBackButton() throws IOException {
        Node node;
        if(Employee.getInstance().isManager()) {
            node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login3-view.fxml")));
        }
        else {
            node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login2-view.fxml")));
        }
        Main.getMainController().setView(node);
    }

    /**
     * Refreshes the cart view.
     */
    public void refreshCartView() {
        cartListView.setItems(FXCollections.observableArrayList(Main.cart.getItems()));
    }
}
