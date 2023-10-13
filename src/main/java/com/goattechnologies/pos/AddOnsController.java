package com.goattechnologies.pos;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddOnsController {

    @FXML
    private ListView<String> addOnsListView;
    @FXML
    private ComboBox<String> sweetnessComboBox;
    @FXML
    private ComboBox<String> iceComboBox;
    private String selectedDrinkName;

    @FXML
    public void initialize() {
        // Load the add-ons, sweetness levels, and ice levels
        // Add-ons are fetched from db, other values are not
        List<String> addOns = new ArrayList<>();

        // Getting menu items from the database
        addOns = Main.dbManager.getAddOnNames();

        addOnsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        addOnsListView.getItems().addAll(addOns); // Sample data
        sweetnessComboBox.getItems().addAll("50%", "70%", "100%");
        iceComboBox.getItems().addAll("Less Ice", "Regular Ice", "More Ice");

        ObservableList<String> selectedAddOns = addOnsListView.getSelectionModel().getSelectedItems();

    }

    public void setSelectedDrinkName(String drinkName) {
        this.selectedDrinkName = drinkName;
    }

    @FXML
    protected void addToCart() throws IOException {
        List<String> selectedAddOns = new ArrayList<>(addOnsListView.getSelectionModel().getSelectedItems());
        //print items in selectedAddOns in one line
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
            //refresh cart
            Main.menuController.refreshCartView();
            //  navigate back to the menu view or show a confirmation message
            handleBackButton();
        }
    }

    @FXML
    protected void handleBackButton() throws IOException {
        Node menuView = FXMLLoader.load(getClass().getResource("menu-view.fxml"));
        Main.getMainController().setView(menuView);
    }

}
