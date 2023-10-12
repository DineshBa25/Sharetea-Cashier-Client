package com.goattechnologies.pos;
import java.sql.ResultSet;
import java.util.List;

public class Product {
    private int productid;
    private String productName;
    private List<Integer> ingredients;
    private List<String> ingredientNames;
    private double price;
    private double salePrice;

    public Product(int productid, String productName, List<Integer> ingredients, double price, double salePrice, List<String> ingredientNames) {
        this.productid = productid;
        this.productName = productName;
        this.ingredients = ingredients;
        this.price = price;
        this.salePrice = salePrice;
        this.ingredientNames = ingredientNames;
    }

    public int getProductid() {
        return productid;
    }

    public String getProductName() {
        return productName;
    }

    public List<Integer> getIngredients() {
        return ingredients;
    }

    public double getPrice() {
        return price;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public List<String> getIngredientNames() {
        return ingredientNames;
    }

    public void setProductName(String name) { productName = name; }
    public void setIngredients(List<Integer> newIngredients) { ingredients = newIngredients;}
    public void setIngredientNames(List<String> newIngredientNames) { ingredientNames = newIngredientNames;}
    public void setSalePrice(Double newSalePrice) { salePrice = newSalePrice;}
}
