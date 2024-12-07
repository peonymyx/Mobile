package Nhom2.com.example.doanmobile.Activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import Nhom2.com.example.doanmobile.Models.CartItem;
import Nhom2.com.example.doanmobile.Models.Order;
import Nhom2.com.example.doanmobile.Models.User;
import Nhom2.com.example.doanmobile.R;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView orderID, orderStatus, orderAddress, orderPhone, orderItems;
    private FirebaseFirestore db;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Ánh xạ các view từ layout
        orderID = findViewById(R.id.orderID);
        orderStatus = findViewById(R.id.orderStatus);
        orderAddress = findViewById(R.id.orderAddress);
        orderPhone = findViewById(R.id.orderPhone);
        orderItems = findViewById(R.id.orderItems);

        // Khởi tạo Firebase Firestore
        db = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Lấy orderID từ Intent
        String receivedOrderID = getIntent().getStringExtra("orderID");
        if (receivedOrderID != null) {
            fetchOrderDetails(receivedOrderID);
        } else {
            Toast.makeText(this, "Order ID is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchOrderDetails(String receivedOrderID) {
        db.collection("users").document(userID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User currentUser = documentSnapshot.toObject(User.class);

                        if (currentUser != null && currentUser.getOrders() != null) {
                            List<Order> orders = currentUser.getOrders();

                            // Tìm kiếm đơn hàng trong danh sách orders
                            for (Order order : orders) {
                                if (order.getOrderID().equals(receivedOrderID)) {
                                    updateUI(order);
                                    return;
                                }
                            }

                            Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUI(Order order) {
        orderID.setText("Order ID: " + order.getOrderID());
        orderStatus.setText("Status: " + order.getStatus());
        orderAddress.setText("Address: " + order.getAddress());
        orderPhone.setText("Phone: " + order.getPhone());

        // Hiển thị danh sách sản phẩm trong đơn hàng
        StringBuilder itemsBuilder = new StringBuilder();
        List<CartItem> items = order.getItems();
        for (CartItem item : items) {
            itemsBuilder.append("- ").append(item.getTitle())
                    .append(" (x").append(item.getQuantity()).append(") ")
                    .append(" - $").append(item.getPrice()).append("\n");
        }
        orderItems.setText(itemsBuilder.toString());
    }
}
