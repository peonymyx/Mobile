package Nhom2.com.example.doanmobile.Activity;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import Nhom2.com.example.doanmobile.Models.CartItem;
import Nhom2.com.example.doanmobile.Models.Order;
import Nhom2.com.example.doanmobile.Models.Review;
import Nhom2.com.example.doanmobile.Models.User;
import Nhom2.com.example.doanmobile.Models.WishListItem;
import Nhom2.com.example.doanmobile.R;

public class MainActivity extends AppCompatActivity {

    // Firestore instance
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Firestore instance
        db = FirebaseFirestore.getInstance();
        getUsers();
    }
    private void getUsers() {
        db.collection("users") // Truy vấn đến collection "users"
                .get() // Lấy dữ liệu từ Firestore
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            // Chuyển dữ liệu từ Firestore thành đối tượng User
                            User user = document.toObject(User.class);
                            if (user != null) {
                                // Log thông tin của User
                                Log.d("MainActivity", "User ID: " + user.getUserID());
                                Log.d("MainActivity", "User Name: " + user.getName());
                                Log.d("MainActivity", "User Email: " + user.getEmail());

                                // Log thông tin về Cart
                                if (user.getCart() != null) {
                                    for (CartItem cartItem : user.getCart()) {
                                        Log.d("MainActivity", "Cart Item: " + cartItem.getTitle() +
                                                ", Quantity: " + cartItem.getQuantity() +
                                                ", Price: " + cartItem.getPrice());
                                    }
                                }

                                // Log thông tin về WishList
                                if (user.getWishList() != null) {
                                    for (WishListItem wishListItem : user.getWishList()) {
                                        Log.d("MainActivity", "WishList Item: " + wishListItem.getName() +
                                                ", Price: " + wishListItem.getPrice() +
                                                ", Description: " + wishListItem.getDescription());

                                        // Log thông tin các Review trong WishListItem
                                        if (wishListItem.getReviews() != null) {
                                            for (Review review : wishListItem.getReviews()) {
                                                Log.d("MainActivity", "Review for Product " + review.getProductID() +
                                                        ": Rating - " + review.getRating() +
                                                        ", Comment: " + review.getComment());
                                            }
                                        }
                                    }
                                }

                                // Log thông tin về Orders
                                if (user.getOrders() != null) {
                                    for (Order order : user.getOrders()) {
                                        Log.d("MainActivity", "Order ID: " + order.getOrderID() +
                                                ", Status: " + order.getStatus() +
                                                ", Address: " + order.getAddress());

                                        // Log thông tin các CartItem trong Order
                                        if (order.getItems() != null) {
                                            for (CartItem cartItem : order.getItems()) {
                                                Log.d("MainActivity", "Order CartItem: " + cartItem.getTitle() +
                                                        ", Quantity: " + cartItem.getQuantity() +
                                                        ", Price: " + cartItem.getPrice());
                                            }
                                        }
                                    }
                                }

                                // Log thông tin về Reviews
                                if (user.getReviews() != null) {
                                    for (Review review : user.getReviews()) {
                                        Log.d("MainActivity", "User Review: " + review.getProductID() +
                                                ", Rating: " + review.getRating() +
                                                ", Comment: " + review.getComment());
                                    }
                                }
                            }
                        }
                    } else {
                        Log.e("MainActivity", "Error getting users: ", task.getException());
                    }
                });
    }

}

