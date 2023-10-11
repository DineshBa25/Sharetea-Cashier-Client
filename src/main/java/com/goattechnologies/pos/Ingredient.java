package com.goattechnologies.pos;

public class Ingredient {
    private int ingredientId;
    private String ingredientName;
    private int quantity;
    private double cost;

    public Ingredient(int ingredientId, String ingredientName, int quantity, double cost) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.cost = cost;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getCost() {
        return cost;
    }
}
