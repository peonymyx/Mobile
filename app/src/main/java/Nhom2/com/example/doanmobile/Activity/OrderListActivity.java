package Nhom2.com.example.doanmobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import Nhom2.com.example.doanmobile.Adapter.OrderAdapter;
import Nhom2.com.example.doanmobile.Adapter.OrderListAdapter;
import Nhom2.com.example.doanmobile.Models.Order;
import Nhom2.com.example.doanmobile.Models.User;
import Nhom2.com.example.doanmobile.R;

public class OrderListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderListAdapter orderAdapter;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        recyclerView = findViewById(R.id.ordersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        fetchOrdersFromFirestore();
    }

    private void fetchOrdersFromFirestore() {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User currentUser = documentSnapshot.toObject(User.class);

                        // Kiểm tra xem người dùng có đơn hàng không
                        if (currentUser != null && currentUser.getOrders() != null && !currentUser.getOrders().isEmpty()) {
                            List<Order> orders = currentUser.getOrders();

                            // Cập nhật adapter với danh sách đơn hàng
                            orderAdapter = new OrderListAdapter(orders, order -> {
                                // Khi nhấn vào đơn hàng, mở chi tiết đơn hàng
                                Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                                intent.putExtra("orderID", order.getOrderID()); // Truyền orderID
                                startActivity(intent);
                            });
                            recyclerView.setAdapter(orderAdapter);
                        } else {
                            Toast.makeText(OrderListActivity.this, "No orders found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(OrderListActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrderListActivity.this, "Error fetching orders: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
