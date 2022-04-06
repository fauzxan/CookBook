package com.example.cookbook2;

public class Product {
    private String product_name;
    private String datetime;
    private String quantity;

    Product(){

    }
    Product(String name, String date, String quantity){
        this.product_name = name;
        this.datetime = date;
        this.quantity = quantity;
    }


    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
