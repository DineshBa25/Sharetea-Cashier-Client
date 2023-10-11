package com.goattechnologies.pos;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;



import java.io.IOException;

public class InventoryController {
    @FXML
    private TableView<Ingredient> ingredientTable;

    @FXML
    private TableColumn<Ingredient, Integer> idColumn;

    @FXML
    private TableColumn<Ingredient, String> nameColumn;

    @FXML
    private TableColumn<Ingredient, Integer> quantityColumn;

    @FXML
    private TableColumn<Ingredient, Double> costColumn;

    @FXML
    public void initialize() {
        Main.inventoryController = this;

        List<Ingredient> ingredients = Main.dbManager.getIngredients();

//        // Create PropertyValueFactory instances for each column
//        PropertyValueFactory<Ingredient, Integer> idCellValueFactory = new PropertyValueFactory<>("ingredientID");
//        PropertyValueFactory<Ingredient, String> nameCellValueFactory = new PropertyValueFactory<>("ingredientName");
//        PropertyValueFactory<Ingredient, Integer> quantityCellValueFactory = new PropertyValueFactory<>("quantity");
//        PropertyValueFactory<Ingredient, Double> costCellValueFactory = new PropertyValueFactory<>("cost");
//
//        // Set the cell value factories for each column
//        idColumn.setCellValueFactory(idCellValueFactory);
//        nameColumn.setCellValueFactory(nameCellValueFactory);
//        quantityColumn.setCellValueFactory(quantityCellValueFactory);
//        costColumn.setCellValueFactory(costCellValueFactory);
//
//        // Convert the list to an ObservableList and set it as the table's data
//        ObservableList<Ingredient> data = FXCollections.observableArrayList(ingredients);
//        ingredientTable.setItems(data);
    }

    public void addIngredient(ActionEvent event) throws IOException {
    // TODO
    }

    public void updateIngredient(ActionEvent event) throws IOException {
    // TODO
    }

    public void handleBackButton(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("manager-view.fxml"));
        Main.getMainController().setView(node);
    }
}
