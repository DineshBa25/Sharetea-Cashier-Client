package com.goattechnologies.pos;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;

public class ProductsController {
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

        productTableView.setColumnResizePolicy(productTableView.CONSTRAINED_RESIZE_POLICY);

        Main.products = Main.dbManager.getProductsList();
        productTableView.getItems().addAll(Main.products);

        // Bind the "Update" button's disable property to the row selection state
        updateButton.disableProperty().bind(isRowSelected.not());

        // Add a listener to update the row selection state
        productTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
                isRowSelected.set(newValue != null);
            }
        });
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
}
