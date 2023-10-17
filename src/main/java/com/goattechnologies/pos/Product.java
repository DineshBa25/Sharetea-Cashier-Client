package com.goattechnologies.pos;
import java.util.List;

/** This class serves as the model for a product in a Point of Sale (POS) application. It stores the product id, name,
 * ingredients, price, and sale price. It also stores the ingredient names for display purposes.
 * @author Nicholas Dienstbier
 */
public class Product {
    private int productid;
    private String productName;
    private List<Integer> ingredients;
    private List<String> ingredientNames;
    private double price;
    private double salePrice;

    /** Constructor for Product
     * @param productid the product id
     * @param productName the product name
     * @param ingredients the ingredients
     * @param price the price
     * @param salePrice the sale price
     * @param ingredientNames the ingredient names
     */
    public Product(int productid, String productName, List<Integer> ingredients, double price, double salePrice, List<String> ingredientNames) {
        this.productid = productid;
        this.productName = productName;
        this.ingredients = ingredients;
        this.price = price;
        this.salePrice = salePrice;
        this.ingredientNames = ingredientNames;
    }

    /** ProductId getter for Product
     * @return the product id int
     */
    public int getProductid() {
        return productid;
    }

    /** ProductName getter for Product
     * @return the product name string
     */
    public String getProductName() {
        return productName;
    }

    /** Ingredients getter for Product
     * @return the ingredients list
     */
    public List<Integer> getIngredients() {
        return ingredients;
    }

    /** Price getter for Product
     * @return the price double
     */
    public double getPrice() {
        return price;
    }

    /** SalePrice getter for Product
     * @return the sale price double
     */
    public double getSalePrice() {
        return salePrice;
    }

    /** IngredientNames getter for Product
     * @return the ingredient names list
     */
    public List<String> getIngredientNames() {
        return ingredientNames;
    }

    /** ProductName setter for Product
     * @param name the product name String
     */
    public void setProductName(String name) { productName = name; }
    /** Product Ingredients setter for Product
     * @param newIngredients the ingredients list
     */
    public void setIngredients(List<Integer> newIngredients) { ingredients = newIngredients;}
    /** Product Price setter for Product
     * @param newIngredientNames the List of Strings of new names
     */
    public void setIngredientNames(List<String> newIngredientNames) { ingredientNames = newIngredientNames;}
    /** Product Price setter for Product
     * @param newSalePrice the new price double
     */
    public void setSalePrice(Double newSalePrice) { salePrice = newSalePrice;}
}
