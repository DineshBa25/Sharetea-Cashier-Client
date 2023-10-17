package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class serves as the controller for the add-ons view in a Point of Sale (POS) application.
 * It allows users to select add-ons, sweetness levels, and ice levels for a selected drink
 * and add the customized item to the shopping cart.
 */
public class AddOnsController {

    @FXML
    private ListView<String> addOnsListView;
    @FXML
    private ComboBox<String> sweetnessComboBox;
    @FXML
    private ComboBox<String> iceComboBox;
    private String selectedDrinkName;

    /**
     * Initializes the AddOnsController, populating the add-ons list and combo boxes with options.
     */
    @FXML
    public void initialize() {
        // Add-ons are fetched from db, other values are not
        List<String> addOns;

        // Getting menu items from the database
        addOns = Main.dbManager.getAddOnNames();

        addOnsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        addOnsListView.getItems().addAll(addOns); // Sample data
        sweetnessComboBox.getItems().addAll("50%", "70%", "100%");
        iceComboBox.getItems().addAll("Less Ice", "Regular Ice", "More Ice");
    }

    /**
     * Sets the name of the selected drink for which add-ons are being customized.
     *
     * @param drinkName The name of the selected drink.
     */
    public void setSelectedDrinkName(String drinkName) {
        this.selectedDrinkName = drinkName;
    }

    /**
     * Handles the action of adding the customized item to the shopping cart.
     *
     * @throws IOException If there is an issue loading another view.
     */
    @FXML
    protected void addToCart() throws IOException {
        List<String> selectedAddOns = new ArrayList<>(addOnsListView.getSelectionModel().getSelectedItems());
        String selectedSweetness = sweetnessComboBox.getValue();
        String selectedIceLevel = iceComboBox.getValue();

        if(selectedSweetness == null && selectedIceLevel == null) {
            AlertUtil.showWarning("Item Warning","Missing sweetness and ice level","Please select a sweetness and ice level");
        } else if(selectedIceLevel == null) {
            AlertUtil.showWarning("Item Warning","Missing ice level","Please select an ice level");
        } else
        if(selectedSweetness == null) {
            AlertUtil.showWarning("Item Warning","Missing sweetness level","Please select a sweetness level");
        }
        else //only if sweetness and ice level are selected, we can proceed to add to cart
        {
            CartItem item = new CartItem(selectedDrinkName, selectedAddOns, selectedSweetness, selectedIceLevel);
            Main.cart.addItem(item);
            Main.menuController.refreshCartView();
            handleBackButton();
        }
    }

    /**
     * Handles the action of returning to the main menu view.
     *
     * @throws IOException If there is an issue loading the main menu view.
     */
    @FXML
    protected void handleBackButton() throws IOException {
        Node menuView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu-view.fxml")));
        Main.getMainController().setView(menuView);
    }

}
