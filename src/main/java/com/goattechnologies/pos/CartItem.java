package com.goattechnologies.pos;

import java.util.List;

public class CartItem {
    private String drinkName;
    private List<String> addOns;
    private String sweetnessLevel;
    private String iceLevel;
    // Add other attributes as needed

    public CartItem(String drinkName, List<String> addOns, String sweetnessLevel, String iceLevel) {
        this.drinkName = drinkName;
        this.addOns = addOns;
        this.sweetnessLevel = sweetnessLevel;
        this.iceLevel = iceLevel;
    }

    public String getDrinkName(){
        return drinkName;
    }

    public void setDrinkName(String drinkName){
        this.drinkName = drinkName;
    }

    public List<String> getAddOns(){
        return addOns;
    }

    public void setAddOns(List<String> addOns){
        this.addOns = addOns;
    }

    public String getSweetnessLevel(){
        return sweetnessLevel;
    }

    public void setSweetnessLevel(String sweetnessLevel){
        this.sweetnessLevel = sweetnessLevel;
    }

    public String getIceLevel(){
        return iceLevel;
    }

    public void setIceLevel(String iceLevel){
        this.iceLevel = iceLevel;
    }
}
