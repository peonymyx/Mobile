package Nhom2.com.example.doanmobile.Models;


public class CartItem {
    private String productID;
    private String name;
    private int quantity;
    private double price;
    private String size;
    public CartItem() {
    }

    public CartItem(String productID, String name, int quantity, double price, String size) {
        this.productID = productID;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.size = size;
    }

    // Getters and setters
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
    public void setName(String name) {
        this.name = name;
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
}
