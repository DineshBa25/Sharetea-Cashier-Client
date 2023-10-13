package com.goattechnologies.pos;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
public class AddProductController implements Initializable {
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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        productIngredients = new ArrayList<>();
        // Bind the "Update" button's disable property to the row selection state
        removeIngredientButton.disableProperty().bind(isRowSelected.not());
        selectedIngredientsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> isRowSelected.set(newValue != null));
    }

    public void addProduct() {
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
            Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("products-view.fxml")));
            Main.getMainController().setView(node);
        } catch (IOException e) {
            AlertUtil.showWarning("Product Warning", "Unable to Load Products", "Please try again!");
        }
    }

    public void removeSelectedIngredients() {
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
            if (Objects.equals(x.getIngredientName(), name)) {
                return x;
            }
        }
        return null;
    }

    public void openAddIngredientDialog() {
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
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("products-view.fxml")));
        Main.getMainController().setView(node);
    }

    public boolean productExists(List<Product> productList, String productNameToFind) {
        for (Product x : productList) {
            if (x.getProductName().equalsIgnoreCase(productNameToFind)) {
                return true;
            }
        }
        return false;
    }
}

