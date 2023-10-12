package com.goattechnologies.pos;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
import java.lang.reflect.Array;

public class ProductsController {
    @FXML
    private TableColumn<Ingredient, String> productNameColumn;
    @FXML
    private TableColumn<Ingredient, Array> productIngredientsColumn;
    @FXML
    private TableColumn<Ingredient, Double> productSalePriceColumn;
    @FXML
    private TableView<Product> productTableView;
    @FXML
    private Button updateButton;
    @FXML
    private Button addButton;

    private BooleanProperty isRowSelected = new SimpleBooleanProperty(false);

    @FXML
    public void initialize() {
        Main.productsController = this;

        Main.products = Main.dbManager.getProductsList();
        productTableView.getItems().addAll(Main.products);
        productTableView.setColumnResizePolicy(productTableView.CONSTRAINED_RESIZE_POLICY);

        // Bind the "Update" button's disable property to the row selection state
        updateButton.disableProperty().bind(isRowSelected.not());

        // Add a listener to update the row selection state
        productTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
                isRowSelected.set(newValue != null);
            }
        });

        centerTextInStringColumn(productNameColumn);
        centerTextInDoubleColumn(productSalePriceColumn);
    }

    @FXML
    private void updateProduct(ActionEvent event) throws IOException{
        // Handle the "Update" button click here
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            // TODO
            System.out.println("We are gonna update selected product");
            Node node = FXMLLoader.load(getClass().getResource("update-product-view.fxml"));
            Main.getMainController().setView(node);
        }
    }

    @FXML
    private void editSelectedProduct(ActionEvent event) throws IOException {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("update-product-view.fxml"));
                Node updateView = loader.load();

                UpdateProductController updateProductController = loader.getController();
                updateProductController.setSelectedProduct(selectedProduct);
                Main.getMainController().setView(updateView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addProduct(ActionEvent event) throws IOException {
        // TODO
        System.out.println("We gonna go add a product");
        Node node = FXMLLoader.load(getClass().getResource("add-product-view.fxml"));
        Main.getMainController().setView(node);
    }

    public void handleBackButton(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("manager-view.fxml"));
        Main.getMainController().setView(node);
    }

    private void centerTextInStringColumn(TableColumn<Ingredient, String> column) {
        column.setCellFactory(tc -> {
            TableCell<Ingredient, String> cell = new TableCell<Ingredient, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };
            cell.setAlignment(Pos.CENTER);

            return cell;
        });
    }
    private void centerTextInDoubleColumn(TableColumn<Ingredient, Double> column) {
        column.setCellFactory(tc -> {
            TableCell<Ingredient, Double> cell = new TableCell<Ingredient, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
            cell.setAlignment(Pos.CENTER);

            return cell;
        });
    }
}



