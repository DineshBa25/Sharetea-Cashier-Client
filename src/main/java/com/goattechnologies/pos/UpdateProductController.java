package com.goattechnologies.pos;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
/** This class serves as the controller for the "Add Product" view in a Point of Sale (POS) application.
 * It allows users to add a new product to the inventory, specifying its name, ingredients, and sale price.
 * @Author Nicholas Dienstbier, Ilham Aryawan
 */
public class UpdateProductController implements Initializable{
    @FXML
    private Label editProduct;
    @FXML
    private Label name;
    @FXML
    private Label ingredients;
    @FXML
    private Label price;
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

    /**
     * Initializes the AddProductController, setting fonts and alignments for labels and fields.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Font customFont = Font.font("Arial", 20);
        editProduct.setFont(customFont);
        name.setFont(customFont);
        ingredients.setFont(customFont);
        price.setFont(customFont);

        Font largerFont = new Font(28);
        productNameField.setFont(largerFont);
        productNameField.setAlignment(Pos.CENTER);
        salePriceField.setFont(largerFont);
        salePriceField.setAlignment(Pos.CENTER);
        selectedIngredientsListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    setText(item);
                    setFont(Font.font(20));
                    setAlignment(Pos.CENTER);
                } else {
                    setText(null);
                }
            }
        });

        // Bind the "Update" button's disable property to the row selection state
        removeIngredientButton.disableProperty().bind(isRowSelected.not());
        selectedIngredientsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> isRowSelected.set(newValue != null));
    }

    /**
     * Sets the selected product to edit.
     * @param product the product to edit
     */
    public void setSelectedProduct(Product product) {
        productIngredients = new ArrayList<>();
        this.selectedProduct = product;

        // Populate the text fields with the current data
        productName = product.getProductName();
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

    /**
     * Updates the selected product with the new data.
     */
    public void updateProduct() {
        try {
            String newName = productNameField.getText();

            double newSalePrice = Double.parseDouble(salePriceField.getText());

            if (!productName.equals(newName) && productExists(Main.products, newName)) {
                AlertUtil.showWarning("Ingredient Warning", "Unable to Update Ingredient", "This ingredient name already exists!");
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

            selectedProduct.setProductName(newName);
            selectedProduct.setIngredients(ingredientIds);
            selectedProduct.setIngredientNames(ingredientNames);
            selectedProduct.setSalePrice(newSalePrice);

            Main.dbManager.updateProduct(selectedProduct);

            backToProducts();
        } catch (Exception e) {
            AlertUtil.showWarning("Product Warning", "Unable to Edit Product", "Fields may be empty!");
        }
    }

    /**
     * Handles the action of deleting a selected product from the database.
     */
    public void deleteProduct() {
        Main.dbManager.deleteProduct(selectedProduct.getProductid());
        backToProducts();
    }

    /**
     * Navigates back to the products view.
     */
    public void backToProducts() {
        try {
            Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("products-view.fxml")));
            Main.getMainController().setView(node);
        } catch (IOException e) {
            AlertUtil.showWarning("Error", "Error loading products view", "Please try again later: " + e.getMessage());
        }
    }

    /**
     * Handles the action of removing selected ingredients from the product's ingredient list.
     */
    public void removeSelectedIngredients() {
        List<String> selectedItems = new ArrayList<>(selectedIngredientsListView.getSelectionModel().getSelectedItems());

        selectedIngredientsListView.getItems().removeAll(selectedItems);

        List<Ingredient> ingredientsToRemove = new ArrayList<>();
        for (String selectedItem : selectedItems) {
            Ingredient ingredientToRemove = findIngredientByName(selectedItem);
            if (ingredientToRemove != null) {
                ingredientsToRemove.add(ingredientToRemove);
            }
        }
        productIngredients.removeAll(ingredientsToRemove);
    }

    /**
     * Finds an ingredient in the product's ingredient list by its name.
     * @param name the name of the ingredient to find
     * @return the ingredient with the specified name, or null if not found
     */
    public Ingredient findIngredientByName(String name) {
        for(Ingredient x : productIngredients) {
            if (Objects.equals(x.getIngredientName(), name)) {
                return x;
            }
        }
        return null;
    }

    /**
     * Opens a dialog for selecting an ingredient to add to the product.
     */
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
    /** Navigates back to the products view.
     */
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("products-view.fxml")));
        Main.getMainController().setView(node);
    }

    /** Checks whether a product already exists in the database
     * @param productList the list of products to check from
     * @param productNameToFind the name of the product to check existence of
     * @return returns a boolean if a product exists already within the database
     */
    public boolean productExists(List<Product> productList, String productNameToFind) {
        for (Product x : productList) {
            if (x.getProductName().equalsIgnoreCase(productNameToFind)) {
                return true;
            }
        }
        return false;
    }
}




