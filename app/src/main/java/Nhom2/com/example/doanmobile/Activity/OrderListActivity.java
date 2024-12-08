package Nhom2.com.example.doanmobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import Nhom2.com.example.doanmobile.Adapter.OrderListAdapter;
import Nhom2.com.example.doanmobile.Models.Order;
import Nhom2.com.example.doanmobile.Models.User;
import Nhom2.com.example.doanmobile.R;
import Nhom2.com.example.doanmobile.databinding.ActivityOrderListBinding;

public class OrderListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderListAdapter orderAdapter;
    private FirebaseFirestore db;
    private String userId;
    private ActivityOrderListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        binding = ActivityOrderListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.ordersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        fetchOrdersFromFirestore();
        setupListeners();
    }

    private void setupListeners() {
        // Back button
        binding.backBtn.setOnClickListener(v -> finish());
    }


    private void fetchOrdersFromFirestore() {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User currentUser = documentSnapshot.toObject(User.class);

                        if (currentUser != null && currentUser.getOrders() != null && !currentUser.getOrders().isEmpty()) {
                            List<Order> orders = currentUser.getOrders();

                            // Khởi tạo adapter và xử lý các sự kiện click
                            orderAdapter = new OrderListAdapter(orders, new OrderListAdapter.OnOrderClickListener() {
                                @Override
                                public void onOrderClick(Order order) {
                                    // Mở chi tiết đơn hàng
                                    Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                                    intent.putExtra("orderID", order.getOrderID());
                                    startActivity(intent);
                                }

                                @Override
                                public void onEditOrderClick(Order order) {
                                    // Xử lý sửa đơn hàng
                                    Toast.makeText(OrderListActivity.this, "Edit order: " + order.getOrderID(), Toast.LENGTH_SHORT).show();
                                    // Thực hiện hành động chỉnh sửa đơn hàng (mở màn hình edit hoặc thực hiện chỉnh sửa)
                                    Intent intent = new Intent(OrderListActivity.this, EditOrderActivity.class);
                                    intent.putExtra("orderID", order.getOrderID());
                                    startActivity(intent);
                                }

                                @Override
                                public void onDeleteOrderClick(Order order) {
                                    // Xử lý xóa đơn hàng
                                    Toast.makeText(OrderListActivity.this, "Delete order: " + order.getOrderID(), Toast.LENGTH_SHORT).show();

                                    // Xóa đơn hàng từ Firestore
                                    db.collection("users").document(userId)
                                            .update("orders", removeOrderFromList(currentUser, order))  // Truyền currentUser vào
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(OrderListActivity.this, "Order deleted", Toast.LENGTH_SHORT).show();
                                                // Cập nhật lại UI (xóa đơn hàng khỏi RecyclerView)
                                                orders.remove(order);
                                                orderAdapter.notifyDataSetChanged();  // Cập nhật lại danh sách trong adapter
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(OrderListActivity.this, "Error deleting order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                }
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

    // Phương thức để xóa đơn hàng khỏi danh sách orders
    private List<Order> removeOrderFromList(User currentUser, Order order) {
        List<Order> updatedOrders = currentUser.getOrders();
        updatedOrders.remove(order);  // Loại bỏ đơn hàng
        return updatedOrders;
    }
}
