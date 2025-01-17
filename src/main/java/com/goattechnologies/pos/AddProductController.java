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
import java.util.*;

/**
 * This class serves as the controller for the "Add Product" view in a Point of Sale (POS) application.
 * It allows users to add a new product to the inventory, specifying its name, ingredients, and sale price.
 *
 * @author Nicholas Dienstbier, Ilham Aryawan
 */
public class AddProductController implements Initializable {
    @FXML
    private Label addProduct;
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

    /**
     * Initializes the AddProductController, setting fonts and alignments for labels and fields.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Font customFont = Font.font("Arial", 20);
        addProduct.setFont(customFont);
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

        productIngredients = new ArrayList<>();
        // Bind the "Update" button's disable property to the row selection state
        removeIngredientButton.disableProperty().bind(isRowSelected.not());
        selectedIngredientsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> isRowSelected.set(newValue != null));
    }

    /**
     * Handles the action of adding a new product to the database.
     */
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
        Main.dbManager.refreshProductCosts();
        backToProducts();
    }

    /**
     * Handles the action of returning to the products view.
     */
    public void backToProducts() {
        try {
            Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("products-view.fxml")));
            Main.getMainController().setView(node);
        } catch (IOException e) {
            AlertUtil.showWarning("Product Warning", "Unable to Load Products", "Please try again!");
        }
    }

    /**
     * Handles the action of removing selected ingredients from the product's ingredient list.
     */
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

    /**
     * Finds an ingredient in the product's ingredient list by its name.
     *
     * @param name The name of the ingredient to find.
     * @return The ingredient object if found, or null if not found.
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

    /**
     * Handles the action of returning to the products view when the "Back" button is clicked.
     *
     * @throws IOException If there is an issue loading the products view.
     */
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("products-view.fxml")));
        Main.getMainController().setView(node);
    }

    /**
     * Checks if a product with the given name exists in the provided product list.
     *
     * @param productList The list of products to search.
     * @param productNameToFind The name of the product to find.
     * @return True if the product with the specified name exists in the list
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

