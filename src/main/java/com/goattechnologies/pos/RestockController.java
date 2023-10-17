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

/** This class serves as the controller for the restock view in a Point of Sale (POS) application. It manages the display
 * of a table of ingredients that are low on stock and provides functionality for navigating back to the statistics view.
 * @author Nicholas Dienstbier
 */
public class RestockController {
    @FXML
    private TableView<Ingredient> ingredientTableView;
    @FXML
    private TableColumn<Ingredient, String> ingredientNameColumn;
    @FXML
    private TableColumn<Ingredient, Integer> quantityColumn;

    /** Initializes the RestockController, setting up the table view.
     */
    @FXML
    public void initialize() {
        Main.restockController = this;

        Main.ingredients = Main.dbManager.getIngredients();
        for(Ingredient ingredient: Main.ingredients) {
            if(ingredient.getQuantity() < 11) {
                ingredientTableView.getItems().add(ingredient);
            }
        }
        ingredientTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        centerTextInStringColumn(ingredientNameColumn);
        centerTextInIntegerColumn(quantityColumn);
    }
    /** Centers the text in a String column
     * @param column the column to center the text in
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
    /** Centers the text in an Integer column
     * @param column the column to center the text in
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
    /** Navigates back to the statistics view.
     */
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("statistics-view.fxml")));
        Main.getMainController().setView(node);
    }
}
