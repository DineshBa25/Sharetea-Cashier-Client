package com.goattechnologies.pos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
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

        Ingredient newIngredient = new Ingredient(0, name, quantity, cost);

        Main.dbManager.addIngredient(newIngredient);

        Node node = FXMLLoader.load(getClass().getResource("inventory-view.fxml"));
        Main.getMainController().setView(node);
    }
}
