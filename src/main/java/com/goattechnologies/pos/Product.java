package com.goattechnologies.pos;

public class Product {
    private int productid;
    private String productName;
    private Ingredient[] ingredients;
    private double price;

    private double salePrice;

    public Product(int productid, String productName, Ingredient[] ingredients, double price, double salePrice) {
        this.productid = productid;
        this.productName = productName;
        this.ingredients = ingredients;
        this.price = price;
        this.salePrice = salePrice;
    }

    public int getProductid() {
        return productid;
    }

    public String getProductName() {
        return productName;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public double getPrice() {
        return price;
    }

    public double getSalePrice() { return salePrice;}
}
