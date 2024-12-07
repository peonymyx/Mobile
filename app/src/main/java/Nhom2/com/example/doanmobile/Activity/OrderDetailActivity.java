package Nhom2.com.example.doanmobile.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Nhom2.com.example.doanmobile.R;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;

import Nhom2.com.example.doanmobile.Models.Order;
import Nhom2.com.example.doanmobile.R;

public class OrderDetailActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView orderTitle, orderStatus, orderDetails, orderAddress, orderPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);  // Layout cho chi tiết đơn hàng

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Ánh xạ các TextViews
        orderTitle = findViewById(R.id.orderTitle);
        orderStatus = findViewById(R.id.orderStatus);
        orderDetails = findViewById(R.id.orderDetails);
        orderAddress = findViewById(R.id.orderAddress);
        orderPhone = findViewById(R.id.orderPhone);

        // Lấy Order ID từ Intent
        String orderId = getIntent().getStringExtra("orderID");

        // Nếu orderId hợp lệ, tiến hành truy vấn dữ liệu từ Firestore
        if (orderId != null) {
            fetchOrderDetails(orderId);
        } else {
            Toast.makeText(this, "Order ID is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchOrderDetails(String orderId) {
        // Lấy đơn hàng từ Firestore dựa trên orderID
        db.collection("Orders")  // Giả sử Orders là tên collection trong Firestore
                .document(orderId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Lấy dữ liệu đơn hàng từ Firestore
                        Order order = documentSnapshot.toObject(Order.class);

                        if (order != null) {
                            // Cập nhật UI với dữ liệu đơn hàng
                            orderTitle.setText("Order ID: " + order.getOrderID());
                            orderStatus.setText("Status: " + order.getStatus());
                            orderDetails.setText("Items: " + order.getItems().toString());
                            orderAddress.setText("Shipping Address: " + order.getAddress().toString());
                            orderPhone.setText("Phone: " + order.getPhone());
                        }
                    } else {
                        Toast.makeText(OrderDetailActivity.this, "Order not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi nếu không lấy được dữ liệu từ Firestore
                    Toast.makeText(OrderDetailActivity.this, "Error fetching order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}