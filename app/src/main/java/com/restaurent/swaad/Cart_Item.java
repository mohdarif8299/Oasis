package com.restaurent.swaad;

public class Cart_Item {
    private String item_name,item_category,item_image;
    private int item_quantity = 1,item_price;

    public Cart_Item() {
    }

    public Cart_Item(String item_name, String item_category, int item_price, String item_image, int item_quantity) {
        this.item_name = item_name;
        this.item_category = item_category;
        this.item_price = item_price;
        this.item_image = item_image;
        this.item_quantity = item_quantity;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_category() {
        return item_category;
    }

    public int getItem_price() {
        return item_price;
    }

    public String getItem_image() {
        return item_image;
    }

    public int getItem_quantity() {
        return item_quantity;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setItem_category(String item_category) {
        this.item_category = item_category;
    }

    public void setItem_price(int item_price) {
        this.item_price = item_price;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public void setItem_quantity(int item_quantity) {
        this.item_quantity = item_quantity;
    }
}
