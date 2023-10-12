package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.List;

public class UpdateIngredientController {
    @FXML
    private TextField ingredientNameField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField costField;

    private Ingredient selectedIngredient;
    private String oldName;

    public void setSelectedIngredient(Ingredient ingredient) {
        this.selectedIngredient = ingredient;
        this.oldName = ingredient.getIngredientName();

        // Populate the text fields with the current data
        ingredientNameField.setText(selectedIngredient.getIngredientName());
        quantityField.setText(String.valueOf(selectedIngredient.getQuantity()));
        costField.setText(String.valueOf(selectedIngredient.getCost()));
    }

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

    public boolean ingredientExists(List<Ingredient> ingredientList, String ingredientNameToFind) {
        for (Ingredient ingredient : ingredientList) {
            if (ingredient.getIngredientName().equalsIgnoreCase(ingredientNameToFind)) {
                return true;
            }
        }
        return false;
    }
}