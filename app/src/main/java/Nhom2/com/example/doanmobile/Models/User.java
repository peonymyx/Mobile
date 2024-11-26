package Nhom2.com.example.doanmobile.Models;
import java.util.List;
public class User {
    private String userID;
    private String name;
    private String email;
    private List<CartItem> cart;
    private List<WishListItem> wishList;
    private List<Order> orders;
    private List<Review> reviews;

    public User() {
        // Required for Firestore
    }

    public User(String userID, String name, String email, List<CartItem> cart, List<WishListItem> wishList, List<Order> orders, List<Review> reviews) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.cart = cart;
        this.wishList = wishList;
        this.orders = orders;
        this.reviews = reviews;
    }

    // Getters and setters
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<CartItem> getCart() {
        return cart;
    }

    public void setCart(List<CartItem> cart) {
        this.cart = cart;
    }

    public List<WishListItem> getWishList() {
        return wishList;
    }

    public void setWishList(List<WishListItem> wishList) {
        this.wishList = wishList;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}