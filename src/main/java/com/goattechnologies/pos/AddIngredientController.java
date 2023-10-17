package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * This class serves as the controller for the add-ingredient view in a Point of Sale (POS) application.
 * It allows users to add a new ingredient to the inventory.
 *
 * @author Mohsin Khan
 */
public class AddIngredientController {
    @FXML
    private TextField ingredientNameField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField costField;
    @FXML
    private Label addIngredient;
    @FXML
    private Label name;
    @FXML
    private Label quantity;
    @FXML
    private Label cost;

    /**
     * Initializes the AddIngredientController, setting fonts and alignments for labels and fields.
     */
    public void initialize() {
        Font customFont = Font.font("Arial", 20);
        addIngredient.setFont(customFont);
        name.setFont(customFont);
        quantity.setFont(customFont);
        cost.setFont(customFont);

        Font largerFont = new Font(28);
        ingredientNameField.setFont(largerFont);
        ingredientNameField.setAlignment(Pos.CENTER);
        quantityField.setFont(largerFont);
        quantityField.setAlignment(Pos.CENTER);
        costField.setFont(largerFont);
        costField.setAlignment(Pos.CENTER);
    }

    /**
     * Handles the action of adding a new ingredient to the inventory.
     */
    public void addIngredient() {
        try {
            String name = ingredientNameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double cost = Double.parseDouble(costField.getText());

            if (ingredientExists(Main.ingredients, name)) {
                AlertUtil.showWarning("Ingredient Warning", "Unable to Add Ingredient", "This ingredient already exists!");
                backToInventory();
                return;
            }

            Ingredient newIngredient = new Ingredient(0, name, quantity, cost);

            Main.dbManager.addIngredient(newIngredient);
        } catch (Exception e) {
            AlertUtil.showWarning("Ingredient Warning", "Unable to Add Ingredient", "Fields may be empty!");
        }

        backToInventory();
    }

    /**
     * Handles the action of returning to the inventory view.
     */
    public void backToInventory() {
        try {
            Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("inventory-view.fxml")));
            Main.getMainController().setView(node);
        } catch (IOException e) {
            AlertUtil.showWarning("Inventory Warning", "Unable to Load Inventory", "Please try again!");
        }
    }

    /**
     * Handles the action of returning to the inventory view when the "Back" button is clicked.
     *
     * @throws IOException If there is an issue loading the inventory view.
     */
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("inventory-view.fxml")));
        Main.getMainController().setView(node);
    }

    /**
     * Checks if an ingredient with the given name exists in the provided ingredient list.
     *
     * @param ingredientList         The list of ingredients to search.
     * @param ingredientNameToFind   The name of the ingredient to find.
     * @return True if the ingredient with the specified name exists in the list; otherwise, false.
     */
    public boolean ingredientExists(List<Ingredient> ingredientList, String ingredientNameToFind) {
        for (Ingredient ingredient : ingredientList) {
            if (ingredient.getIngredientName().equalsIgnoreCase(ingredientNameToFind)) {
                return true;
            }
        }
        return false;
    }
}
