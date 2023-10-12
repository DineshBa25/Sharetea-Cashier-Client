package com.goattechnologies.pos;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AddProductController {
    public TextField productNameField;
    public TextField ingredientListField;
    public TextField salePriceField;
    public ListView selectedIngredientsListView;

    public void addProduct(ActionEvent event) throws IOException {
        String name = productNameField.getText();
//        int quantity = Integer.parseInt(quantityField.getText());
        double salePrice = Double.parseDouble(salePriceField.getText());

//        Ingredient newIngredient = new Ingredient(0, name, quantity, cost);

//        Main.dbManager.addIngredient(newIngredient);

        Node node = FXMLLoader.load(getClass().getResource("products-view.fxml"));
        Main.getMainController().setView(node);
    }

    public void removeSelectedIngredients(ActionEvent actionEvent) {
    }

    public void openAddIngredientDialog(ActionEvent actionEvent) {
    }
}
