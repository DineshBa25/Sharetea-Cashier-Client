package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class UpdateIngredientController {
    @FXML
    private TextField ingredientNameField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField costField;

    private Ingredient selectedIngredient;

    public void setSelectedIngredient(Ingredient ingredient) {
        this.selectedIngredient = ingredient;

        // Populate the text fields with the current data
        ingredientNameField.setText(selectedIngredient.getIngredientName());
        quantityField.setText(String.valueOf(selectedIngredient.getQuantity()));
        costField.setText(String.valueOf(selectedIngredient.getCost()));
    }

    public void updateIngredient() {
        String newName = ingredientNameField.getText();
        int newQuantity = Integer.parseInt(quantityField.getText());
        double newCost = Double.parseDouble(costField.getText());

        selectedIngredient.setIngredientName(newName);
        selectedIngredient.setQuantity(newQuantity);
        selectedIngredient.setCost(newCost);

        // Save the updated ingredient to database
        Main.dbManager.updateIngredient(selectedIngredient);

        backToInventory();
    }

    public void deleteIngredient() {
        Main.dbManager.deleteIngredient(selectedIngredient.getIngredientId());
        backToInventory();
    }

    public void backToInventory() {
        try {
            Node node = FXMLLoader.load(getClass().getResource("inventory-view.fxml"));
            Main.getMainController().setView(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}