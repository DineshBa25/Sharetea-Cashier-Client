package com.goattechnologies.pos;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class UpdateProductController implements Initializable{
    public Button removeIngredientButton;
    @FXML
    private TextField productNameField;
    @FXML
    public TextField salePriceField;
    @FXML
    public ListView<String> selectedIngredientsListView;
    public String productName;
    public double productSalePrice;
    public List<Ingredient> productIngredients;
    private BooleanProperty isRowSelected = new SimpleBooleanProperty(false);
    private Product selectedProduct;


    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Bind the "Update" button's disable property to the row selection state
        removeIngredientButton.disableProperty().bind(isRowSelected.not());
        selectedIngredientsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                isRowSelected.set(newValue != null);
            }
        });
    }

    public void setSelectedProduct(Product product) {
        productIngredients = new ArrayList<>();
        System.out.println("yoyooyoy");
        this.selectedProduct = product;

        // Populate the text fields with the current data
        productNameField.setText(selectedProduct.getProductName());

        List<Ingredient> ingredientsInDatabase = Main.dbManager.getIngredients();
        List<String> selectedProductIngredientNames = product.getIngredientNames();
        for(Ingredient x : ingredientsInDatabase) {
            if(selectedProductIngredientNames.contains(x.getIngredientName())) {
                productIngredients.add(x);
                selectedIngredientsListView.getItems().add(x.getIngredientName());
            }
        }

        salePriceField.setText(String.valueOf(selectedProduct.getSalePrice()));
    }

    public void addProduct(ActionEvent event) throws IOException {
        try {
            productName = productNameField.getText();
            productSalePrice = Double.parseDouble(salePriceField.getText());

            if (productExists(Main.products, productName)) {
                AlertUtil.showWarning("Product Warning", "Unable to Add Product", "This product already exists!");
                backToProducts();
                return;
            }

            List<Integer> ingredientIds = new ArrayList<>();
            List<String> ingredientNames = new ArrayList<>();
            for(Ingredient x : productIngredients)
            {
                ingredientIds.add(x.getIngredientId());
                ingredientNames.add(x.getIngredientName());
            }


            Product newProduct = new Product(0, productName, ingredientIds, 0, productSalePrice, ingredientNames);

            Main.dbManager.addProduct(newProduct);
        } catch (Exception e) {
            AlertUtil.showWarning("Product Warning", "Unable to Add Product", "Fields may be empty!");
        }

        backToProducts();
    }

    public void backToProducts() {
        try {
            Node node = FXMLLoader.load(getClass().getResource("products-view.fxml"));
            Main.getMainController().setView(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeSelectedIngredients(ActionEvent actionEvent) {
        // Get the selected items from the ListView
        List<String> selectedItems = new ArrayList<>(selectedIngredientsListView.getSelectionModel().getSelectedItems());

        // Remove the selected items from the ListView
        selectedIngredientsListView.getItems().removeAll(selectedItems);

        // Optionally, you can also remove the corresponding items from your productIngredients list
        List<Ingredient> ingredientsToRemove = new ArrayList<>();
        for (String selectedItem : selectedItems) {
            // You need to map the selected ingredient to your productIngredients list
            Ingredient ingredientToRemove = findIngredientByName(selectedItem);
            if (ingredientToRemove != null) {
                ingredientsToRemove.add(ingredientToRemove);
            }
        }
        productIngredients.removeAll(ingredientsToRemove);
    }

    public Ingredient findIngredientByName(String name) {
        for(Ingredient x : productIngredients) {
            if (x.getIngredientName() == name) {
                return x;
            }
        }
        return null;
    }

    public void openAddIngredientDialog(ActionEvent actionEvent) {
        List<String> productIngredientNames = new ArrayList<>();
        for(Ingredient x: productIngredients) {
            productIngredientNames.add(x.getIngredientName());
        }
        List<Ingredient> ingredientsInDatabase = Main.dbManager.getIngredients();
        List<String> existingIngredients = new ArrayList<>();
        for(Ingredient x : ingredientsInDatabase) {
            if(!productIngredientNames.contains(x.getIngredientName())) {
                existingIngredients.add(x.getIngredientName());
            }
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(existingIngredients.get(0), existingIngredients);
        dialog.setTitle("Select Ingredient");
        dialog.setHeaderText("Select an existing ingredient:");

        dialog.showAndWait().ifPresent(selectedIngredient -> {
            if (!selectedIngredient.isEmpty()) {
                for(Ingredient x : ingredientsInDatabase) {
                    if(Objects.equals(x.getIngredientName(), selectedIngredient) && !productIngredientNames.contains(selectedIngredient)) {
                        productIngredients.add(x);
                        selectedIngredientsListView.getItems().add(selectedIngredient);
                    }
                }
            }
        });
    }
    public void handleBackButton(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("products-view.fxml"));
        Main.getMainController().setView(node);
    }

    public boolean productExists(List<Product> productList, String productNameToFind) {
        for (Product x : productList) {
            if (x.getProductName().equals(productNameToFind)) {
                return true;
            }
        }
        return false;
    }
}




