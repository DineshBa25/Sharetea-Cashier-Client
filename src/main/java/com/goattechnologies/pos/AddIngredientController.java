package com.goattechnologies.pos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.List;

public class AddIngredientController {
    @FXML
    private TextField ingredientNameField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField costField;

    public void addIngredient(ActionEvent event) throws IOException {
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

    public void backToInventory() {
        try {
            Node node = FXMLLoader.load(getClass().getResource("inventory-view.fxml"));
            Main.getMainController().setView(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleBackButton(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("inventory-view.fxml"));
        Main.getMainController().setView(node);
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
