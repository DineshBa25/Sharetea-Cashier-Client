package com.goattechnologies.pos;

/**
 * The Ingredient class represents an ingredient used in the Point of Sale system.
 * It contains information such as the ingredient ID, name, quantity, and cost.
 */
public class Ingredient {
    private int ingredientId;
    private String ingredientName;
    private int quantity;
    private double cost;

    /**
     * Constructs an Ingredient object with the given attributes.
     *
     * @param ingredientId    The unique identifier for the ingredient.
     * @param ingredientName  The name of the ingredient.
     * @param quantity        The quantity of the ingredient in stock.
     * @param cost            The cost of the ingredient.
     */
    public Ingredient(int ingredientId, String ingredientName, int quantity, double cost) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.cost = cost;
    }

    /**
     * Get the unique identifier of the ingredient.
     *
     * @return The ingredient's ID.
     */
    public int getIngredientId() {
        return ingredientId;
    }

    /**
     * Get the name of the ingredient.
     *
     * @return The ingredient's name.
     */
    public String getIngredientName() {
        return ingredientName;
    }

    /**
     * Get the current quantity of the ingredient in stock.
     *
     * @return The quantity of the ingredient.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Get the cost of the ingredient.
     *
     * @return The cost of the ingredient.
     */
    public double getCost() {
        return cost;
    }

    /**
     * Set the name of the ingredient.
     *
     * @param ingredientName The new name for the ingredient.
     */
    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    /**
     * Set the quantity of the ingredient in stock.
     *
     * @param quantity The new quantity of the ingredient.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Set the cost of the ingredient.
     *
     * @param cost The new cost of the ingredient.
     */
    public void setCost(double cost) {
        this.cost = cost;
    }
}
