package Nhom2.com.example.doanmobile.Models;

public class Review {
    private String productID;
    private int rating;
    private String comment;

    public Review() {
    }

    public Review(String productID, int rating, String comment) {
        this.productID = productID;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and setters
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
