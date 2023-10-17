package com.goattechnologies.pos;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a shopping cart in a Point of Sale (POS) application.
 * It allows adding, removing, and managing items within the cart.
 */
public class Cart {
    private List<CartItem> items;

    /**
     * Constructs a new cart with an empty list of items.
     */
    public Cart() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds a single item to the cart.
     *
     * @param item The item to add to the cart.
     */
    public void addItem(CartItem item) {
        items.add(item);
    }

    /**
     * Adds a list of items to the cart.
     *
     * @param items The list of items to add to the cart.
     */
    public void addItems(List<CartItem> items){
        this.items.addAll(items);
    }

    /**
     * Removes a specific item from the cart.
     *
     * @param item The item to remove from the cart.
     */
    public void removeItem(CartItem item) {
        items.remove(item);
    }

    /**
     * Removes all items from the cart, making it empty.
     */
    public void removeAllItemsInCart(){
        items.clear();
    }

    /**
     * Retrieves the list of items in the cart.
     *
     * @return A list of CartItems in the cart.
     */
    public List<CartItem> getItems() {
        return items;
    }

    /**
     * Clears the cart, removing all items from it.
     */
    public void clear() {
        items.clear();
    }

    /**
     * Checks if the cart is empty.
     *
     * @return True if the cart is empty; otherwise, false.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
