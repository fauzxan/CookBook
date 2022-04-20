package com.example.cookbook2;

public class Cart {
    private String item_name;
    private String quantity;



    Cart(){

    }

    Cart(String name){
        this.item_name = name;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
