package com.goattechnologies.pos;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Objects;

/** This class serves as the controller for the products view in a Point of Sale (POS) application. It manages the display
 * and interaction with a table of products and provides functionality for updating and adding products. It also provides
 * functionality for navigating back to the manager view.
 * @author Nicholas Dienstbier
 */
public class ProductsController {
    @FXML
    private TableColumn<Ingredient, String> productNameColumn;
    @FXML
    private TableColumn<Ingredient, Double> productSalePriceColumn;
    @FXML
    private TableView<Product> productTableView;
    @FXML
    private Button updateButton;
    @FXML
    private Button addButton;
    private BooleanProperty isRowSelected = new SimpleBooleanProperty(false);

    /** Initializes the ProductsController, setting up the table view and buttons.
     */
    @FXML
    public void initialize() {
        Main.productsController = this;

        Main.products = Main.dbManager.getProductsList();
        productTableView.getItems().addAll(Main.products);
        productTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        updateButton.disableProperty().bind(isRowSelected.not());

        productTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> isRowSelected.set(newValue != null));

        centerTextInStringColumn(productNameColumn);
        centerTextInDoubleColumn(productSalePriceColumn);
    }

    /** Navigates to the edit product view for the selected product.
     */
    @FXML
    private void editSelectedProduct() {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("update-product-view.fxml"));
                Node updateView = loader.load();

                UpdateProductController updateProductController = loader.getController();
                updateProductController.setSelectedProduct(selectedProduct);
                Main.getMainController().setView(updateView);
            } catch (IOException e) {
                AlertUtil.showWarning("Error", "Error loading update product view", "Please try again later: " + e.getMessage());
            }
        }
    }

    /** Navigates to the add product view.
     */
    @FXML
    private void addProduct() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("add-product-view.fxml")));
        Main.getMainController().setView(node);
    }

    /** Navigates back to the manager view.
     */
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("manager-view.fxml")));
        Main.getMainController().setView(node);
    }

    /** Centers the text in a string column.
     * @param column the column to center
     */
    private void centerTextInStringColumn(TableColumn<Ingredient, String> column) {
        column.setCellFactory(tc -> {
            TableCell<Ingredient, String> cell = new TableCell<>() {
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

    /** Centers the text in a double column.
     * @param column the column to center
     */
    private void centerTextInDoubleColumn(TableColumn<Ingredient, Double> column) {
        column.setCellFactory(tc -> {
            TableCell<Ingredient, Double> cell = new TableCell<>() {
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



