package com.goattechnologies.pos;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import java.util.List;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;


public class InventoryController {
    @FXML
    private TableView<Ingredient> ingredientTableView;
    @FXML
    private Button updateButton;
    @FXML
    private Button addButton;

    private BooleanProperty isRowSelected = new SimpleBooleanProperty(false);

    @FXML
    public void initialize() {
        Main.inventoryController = this;

        Main.ingredients = Main.dbManager.getIngredients();
        ingredientTableView.getItems().addAll(Main.ingredients);

        // Bind the "Update" button's disable property to the row selection state
        updateButton.disableProperty().bind(isRowSelected.not());

        // Add a listener to update the row selection state
        ingredientTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ingredient>() {
            @Override
            public void changed(ObservableValue<? extends Ingredient> observable, Ingredient oldValue, Ingredient newValue) {
                isRowSelected.set(newValue != null);
            }
        });

    }

    @FXML
    private void updateIngredient(ActionEvent event) throws IOException{
        // Handle the "Update" button click here
        Ingredient selectedIngredient = ingredientTableView.getSelectionModel().getSelectedItem();
        if (selectedIngredient != null) {
            // TODO
            System.out.println("We are gonna update selected ingredient");
            Node node = FXMLLoader.load(getClass().getResource("update-ingredient-view.fxml"));
            Main.getMainController().setView(node);
        }
    }

    @FXML
    private void addIngredient(ActionEvent event) throws IOException {
    // TODO
        System.out.println("We gonna go add an ingredient");
        Node node = FXMLLoader.load(getClass().getResource("add-ingredient-view.fxml"));
        Main.getMainController().setView(node);
    }

    public void handleBackButton(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("manager-view.fxml"));
        Main.getMainController().setView(node);
    }
}
