package com.goattechnologies.pos;

import java.util.List;

/**
 * This class represents an item in a shopping cart within a Point of Sale (POS) application.
 * It includes details about a selected drink, its add-ons, sweetness level, and ice level.
 *
 * @author Dinesh Balakrishnan
 */
public class CartItem {
    private String drinkName;
    private List<String> addOns;
    private String sweetnessLevel;
    private String iceLevel;

    /**
     * Constructs a new CartItem with the specified attributes.
     *
     * @param drinkName      The name of the selected drink.
     * @param addOns         The list of add-ons chosen for the drink.
     * @param sweetnessLevel The level of sweetness chosen for the drink.
     * @param iceLevel       The level of ice chosen for the drink.
     */
    public CartItem(String drinkName, List<String> addOns, String sweetnessLevel, String iceLevel) {
        this.drinkName = drinkName;
        this.addOns = addOns;
        this.sweetnessLevel = sweetnessLevel;
        this.iceLevel = iceLevel;
    }

    /**
     * Retrieves the name of the selected drink.
     *
     * @return The drink name.
     */
    public String getDrinkName(){
        return drinkName;
    }

    /**
     * Sets the name of the selected drink.
     *
     * @param drinkName The new drink name to set.
     */
    public void setDrinkName(String drinkName){
        this.drinkName = drinkName;
    }

    /**
     * Retrieves the list of add-ons chosen for the drink.
     *
     * @return The list of add-ons.
     */
    public List<String> getAddOns(){
        return addOns;
    }

    /**
     * Sets the list of add-ons chosen for the drink.
     *
     * @param addOns The new list of add-ons to set.
     */
    public void setAddOns(List<String> addOns){
        this.addOns = addOns;
    }

    /**
     * Retrieves the sweetness level chosen for the drink.
     *
     * @return The sweetness level.
     */
    public String getSweetnessLevel(){
        return sweetnessLevel;
    }

    /**
     * Sets the sweetness level chosen for the drink.
     *
     * @param sweetnessLevel The new sweetness level to set.
     */
    public void setSweetnessLevel(String sweetnessLevel){
        this.sweetnessLevel = sweetnessLevel;
    }

    /**
     * Retrieves the ice level chosen for the drink.
     *
     * @return The ice level.
     */
    public String getIceLevel(){
        return iceLevel;
    }

    /**
     * Sets the ice level chosen for the drink.
     *
     * @param iceLevel The new ice level to set.
     */
    public void setIceLevel(String iceLevel){
        this.iceLevel = iceLevel;
    }
}
