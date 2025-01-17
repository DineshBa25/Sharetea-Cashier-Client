package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * This class manages
 * the display and interaction with the update ingredient view, which allows the user to update an ingredient in the
 * database.
 * @Author Mohsin Khan
 */
public class UpdateIngredientController {
    @FXML
    private TextField ingredientNameField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField costField;
    @FXML
    private Label updateIngredient;
    @FXML
    private Label name;
    @FXML
    private Label quantity;
    @FXML
    private Label cost;

    private Ingredient selectedIngredient;
    private String oldName;

    /**
     * Initializes the UpdateIngredientController, setting up the text fields.
     */
    public void initialize() {
        Font customFont = Font.font("Arial", 20);
        updateIngredient.setFont(customFont);
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
     * Sets the selected ingredient to be updated.
     * @param ingredient The ingredient to be updated.
     */
    public void setSelectedIngredient(Ingredient ingredient) {
        this.selectedIngredient = ingredient;
        this.oldName = ingredient.getIngredientName();

        // Populate the text fields with the current data
        ingredientNameField.setText(selectedIngredient.getIngredientName());
        quantityField.setText(String.valueOf(selectedIngredient.getQuantity()));
        costField.setText(String.valueOf(selectedIngredient.getCost()));
    }

    /**
     * Updates the ingredient in the database.
     */
    public void updateIngredient() {
        String newName = ingredientNameField.getText();
        int newQuantity = Integer.parseInt(quantityField.getText());
        double newCost = Double.parseDouble(costField.getText());

        if (!oldName.equals(newName) && ingredientExists(Main.ingredients, newName)) {
            AlertUtil.showWarning("Ingredient Warning", "Unable to Update Ingredient", "This ingredient name already exists!");
            backToInventory();
            return;
        }

        selectedIngredient.setIngredientName(newName);
        selectedIngredient.setQuantity(newQuantity);
        selectedIngredient.setCost(newCost);

        Main.dbManager.updateIngredient(selectedIngredient);

        backToInventory();
    }

    /**
     * Deletes the ingredient from the database.
     */
    public void deleteIngredient() {
        Main.dbManager.deleteIngredient(selectedIngredient.getIngredientId());
        backToInventory();
    }

    /**
     * Navigates back to the inventory view.
     */
    public void backToInventory() {
        try {
            Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("inventory-view.fxml")));
            Main.getMainController().setView(node);
        } catch (IOException e) {
            AlertUtil.showWarning("Error", "Error loading inventory view", "Please try again later: " + e.getMessage());
        }
    }

    /**
     * Checks if an ingredient exists in a list of ingredients.
     * @param ingredientList The list of ingredients to check.
     * @param ingredientNameToFind The ingredient name to find.
     * @return True if the ingredient exists, false otherwise.
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