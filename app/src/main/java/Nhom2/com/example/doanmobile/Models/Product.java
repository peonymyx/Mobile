package Nhom2.com.example.doanmobile.Models;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;
import java.util.List;

public class Product {
    private String productID;
    private String name;
    private String description;
    private double price;
    private String categoryID;
    private List<ProductReview> reviews;

    public Product() {
    }

    public Product(String productID, String name, String description, double price, String categoryID, List<ProductReview> reviews) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryID = categoryID;
        this.reviews = reviews;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public List<ProductReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<ProductReview> reviews) {
        this.reviews = reviews;
    }
}