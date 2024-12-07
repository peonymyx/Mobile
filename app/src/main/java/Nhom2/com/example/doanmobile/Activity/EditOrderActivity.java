package Nhom2.com.example.doanmobile.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import Nhom2.com.example.doanmobile.Models.Order;
import Nhom2.com.example.doanmobile.R;

public class EditOrderActivity extends AppCompatActivity {

    private EditText orderIdEditText, statusEditText;
    private Button saveButton;
    private FirebaseFirestore db;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get order ID from Intent
        String orderId = getIntent().getStringExtra("orderID");

        // Initialize UI elements
        orderIdEditText = findViewById(R.id.orderId);
        statusEditText = findViewById(R.id.status);
        saveButton = findViewById(R.id.saveButton);

        // Fetch order details from Firestore
        fetchOrderDetails(orderId);

        // Handle save button click
        saveButton.setOnClickListener(v -> {
            saveOrderDetails(orderId);
        });
    }

    private void fetchOrderDetails(String orderId) {
        db.collection("orders")
                .document(orderId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        order = documentSnapshot.toObject(Order.class);
                        if (order != null) {
                            orderIdEditText.setText(order.getOrderID());
                            statusEditText.setText(order.getStatus());
                        }
                    } else {
                        Toast.makeText(EditOrderActivity.this, "Order not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditOrderActivity.this, "Error fetching order", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveOrderDetails(String orderId) {
        String updatedStatus = statusEditText.getText().toString();

        if (updatedStatus.isEmpty()) {
            Toast.makeText(this, "Please enter a status", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update order status
        db.collection("orders")
                .document(orderId)
                .update("status", updatedStatus)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditOrderActivity.this, "Order updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after saving
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditOrderActivity.this, "Error updating order", Toast.LENGTH_SHORT).show();
                    Log.e("EditOrderActivity", "Error updating order: ", e);
                });
    }
}
