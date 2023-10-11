package com.goattechnologies.pos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;

import java.io.IOException;

public class AddIngredientController {
    @FXML
    private TextField ingredientNameField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField costField;

    public void addIngredient(ActionEvent event) throws IOException {
        String name = ingredientNameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        double cost = Double.parseDouble(costField.getText());

        // Create an Ingredient object with the entered data
        Ingredient newIngredient = new Ingredient(0, name, quantity, cost);

        // Add the newIngredient to your list or database
        Main.dbManager.addIngredient(newIngredient);

        // redirect to inventory view
        Node node = FXMLLoader.load(getClass().getResource("inventory-view.fxml"));
        Main.getMainController().setView(node);
    }
}
