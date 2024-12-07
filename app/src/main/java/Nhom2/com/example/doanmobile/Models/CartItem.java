package Nhom2.com.example.doanmobile.Models;


import java.io.Serializable;
import java.util.ArrayList;

public class CartItem implements Serializable {
    private String title;
    private int quantity;
    private double price;
    private String size;
    private String color;
    private ArrayList<String> picUrl;
    public CartItem() {
    }

    public CartItem( String name, int quantity, double price, String size, String color,ArrayList<String> picUrl ) {
        this.title = name;
        this.quantity = quantity;
        this.price = price;
        this.size = size;
        this.color = color;
        this.picUrl =picUrl;
    }

    // Getters and setters

    public String getTitle() {
        return title;
    }
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
    public void setTitle(String name) {
        this.title = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }
}
