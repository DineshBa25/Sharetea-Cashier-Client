package com.goattechnologies.pos;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addItem(CartItem item) {
        items.add(item);
    }

    public void addItems(List<CartItem> items){
        this.items.addAll(items);
    }

    public void removeItem(CartItem item) {
        items.remove(item);
    }

    public void removeAllItemsInCart(){
        items.clear();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }

    // Add other cart-related methods as needed
}
