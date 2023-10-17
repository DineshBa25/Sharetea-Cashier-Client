package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.Objects;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;

/**
 * This class serves as the controller for the inventory view in a Point of Sale (POS) application.
 * It manages the display and interaction with a table of ingredients and provides functionality for
 * updating, adding, and navigating within the inventory.
 *
 * @author Mohsin Khan
 */
public class InventoryController {
    @FXML
    private TableView<Ingredient> ingredientTableView;
    @FXML
    private TableColumn<Ingredient, String> ingredientNameColumn;
    @FXML
    private TableColumn<Ingredient, Integer> quantityColumn;
    @FXML
    private TableColumn<Ingredient, Double> costColumn;
    @FXML
    private Button updateButton;

    private BooleanProperty isRowSelected = new SimpleBooleanProperty(false);

    /**
     * Initializes the InventoryController, setting up the table, binding the "Update" button's
     * disable property, and centering text in the columns.
     */
    @FXML
    public void initialize() {
        Main.inventoryController = this;

        Main.ingredients = Main.dbManager.getIngredients();
        ingredientTableView.getItems().addAll(Main.ingredients);
        ingredientTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Bind the "Update" button's disable property to the row selection state
        updateButton.disableProperty().bind(isRowSelected.not());

        // Add a listener to update the row selection state
        ingredientTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> isRowSelected.set(newValue != null));

        centerTextInStringColumn(ingredientNameColumn);
        centerTextInIntegerColumn(quantityColumn);
        centerTextInDoubleColumn(costColumn);
    }

    /**
     * Centers text in a TableColumn containing String data.
     *
     * @param column The FXML column for strings taken from an ingredient, to be centered.
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

    /**
     * Centers text in a TableColumn containing Integer data.
     *
     * @param column The FXML column for integers taken from an ingredient, to be centered.
     */
    private void centerTextInIntegerColumn(TableColumn<Ingredient, Integer> column) {
        column.setCellFactory(tc -> {
            TableCell<Ingredient, Integer> cell = new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
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

    /**
     * Centers text in a TableColumn containing Double data.
     *
     * @param column The FXML column for doubles taken from an ingredient, to be centered.
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

    /**
     * Handles the action of editing the selected ingredient. Opens a new view for editing the
     * selected ingredient's details.
     */
    @FXML
    private void editSelectedIngredient() {
        Ingredient selectedIngredient = ingredientTableView.getSelectionModel().getSelectedItem();

        if (selectedIngredient != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("update-ingredient-view.fxml"));
                Node updateView = loader.load();

                UpdateIngredientController updateIngredientController = loader.getController();
                updateIngredientController.setSelectedIngredient(selectedIngredient);
                Main.getMainController().setView(updateView);
            } catch (IOException e) {
                AlertUtil.showWarning("Inventory Warning", "Unable to Load Update Ingredient View", "Please try again!");
            }
        }
    }

    /**
     * Handles the action of adding a new ingredient. Opens a new view for entering details of the
     * new ingredient.
     *
     * @throws IOException If there is an issue loading the "add-ingredient-view.fxml" file.
     */
    @FXML
    private void addIngredient() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("add-ingredient-view.fxml")));
        Main.getMainController().setView(node);
    }

    /**
     * Handles the action of returning to the main manager view.
     *
     * @throws IOException If there is an issue loading the "manager-view.fxml" file.
     */
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("manager-view.fxml")));
        Main.getMainController().setView(node);
    }
}
