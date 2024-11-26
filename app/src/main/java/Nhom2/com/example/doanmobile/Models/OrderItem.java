package Nhom2.com.example.doanmobile.Models;

public class OrderItem {
    private String productID;
    private String name;
    private int quantity;
    private double price;

    // Constructors, getters and setters

    public OrderItem() {
        // Empty constructor for Firestore
    }

    public OrderItem(String productID, String name, int quantity, double price) {
        this.productID = productID;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "OrderItem{productID='" + productID + "', name='" + name + "', quantity=" + quantity + ", price=" + price + "}";
    }
}
