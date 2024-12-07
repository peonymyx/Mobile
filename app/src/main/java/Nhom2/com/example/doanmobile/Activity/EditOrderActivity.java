package Nhom2.com.example.doanmobile.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import Nhom2.com.example.doanmobile.Models.Address;
import Nhom2.com.example.doanmobile.Models.Order;
import Nhom2.com.example.doanmobile.Models.User;
import Nhom2.com.example.doanmobile.R;

public class EditOrderActivity extends AppCompatActivity {

    private EditText orderIdEditText, statusEditText,
            streetEditText, cityEditText, communeEditText,
            districtEditText, phoneEditText;
    private Button saveButton;
    private FirebaseFirestore db;
    private String orderId;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        // Initialize Firestore and current user
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get order ID from Intent
        orderId = getIntent().getStringExtra("orderID");

        // Initialize UI elements
        initializeViews();

        // Fetch order details from Firestore
        fetchOrderDetails();

        // Handle save button click
        saveButton.setOnClickListener(v -> saveOrderDetails());
    }

    private void initializeViews() {
        orderIdEditText = findViewById(R.id.orderId);
        statusEditText = findViewById(R.id.status);
        streetEditText = findViewById(R.id.street);
        cityEditText = findViewById(R.id.city);
        districtEditText = findViewById(R.id.district);
        communeEditText = findViewById(R.id.commune);
        phoneEditText = findViewById(R.id.phone);
        saveButton = findViewById(R.id.saveButton);

        // Make Order ID and Status non-editable
        orderIdEditText.setEnabled(false);
        statusEditText.setEnabled(false);
    }

    private void fetchOrderDetails() {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(userDocument -> {
                    if (userDocument.exists()) {
                        // Get the specific order from user's orders list
                        Order order = findOrderById(userDocument.toObject(User.class));

                        if (order != null) {
                            // Populate fields with order details
                            orderIdEditText.setText(order.getOrderID());
                            statusEditText.setText(order.getStatus());

                            // Populate address fields
                            if (order.getAddress() != null) {
                                streetEditText.setText(order.getAddress().getDetails());
                                cityEditText.setText(order.getAddress().getCity());
                                districtEditText.setText(order.getAddress().getDistrict());
                                communeEditText.setText(order.getAddress().getCommune());
                            }

                            // Set phone number
                            phoneEditText.setText(order.getPhone());
                        } else {
                            Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching order details", Toast.LENGTH_SHORT).show();
                });
    }

    private Order findOrderById(User user) {
        if (user != null && user.getOrders() != null) {
            for (Order order : user.getOrders()) {
                if (order.getOrderID().equals(orderId)) {
                    return order;
                }
            }
        }
        return null;
    }

    private void saveOrderDetails() {
        // Validate input fields
        if (!validateInputs()) {
            return;
        }

        // Prepare updated order details
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(userDocument -> {
                    if (userDocument.exists()) {
                        User user = userDocument.toObject(User.class);
                        if (user != null && user.getOrders() != null) {
                            // Find and update the specific order in the list
                            for (Order order : user.getOrders()) {
                                if (order.getOrderID().equals(orderId)) {
                                    // Update address
                                    Address updatedAddress = new Address(
                                            streetEditText.getText().toString(),
                                            communeEditText.getText().toString(),
                                            cityEditText.getText().toString(),
                                            districtEditText.getText().toString()
                                    );
                                    order.setAddress(updatedAddress);

                                    // Update phone number
                                    order.setPhone(phoneEditText.getText().toString());
                                    break;
                                }
                            }

                            // Update the entire user document with modified orders list
                            db.collection("users").document(userId)
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(EditOrderActivity.this,
                                                "Order updated successfully",
                                                Toast.LENGTH_SHORT).show();
                                        finish(); // Close the activity after saving
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(EditOrderActivity.this,
                                                "Error updating order",
                                                Toast.LENGTH_SHORT).show();
                                        Log.e("EditOrderActivity", "Error updating order: ", e);
                                    });
                        }
                    }
                });
    }

    private boolean validateInputs() {
        // Check if any address or phone field is empty
        if (streetEditText.getText().toString().isEmpty() ||
                cityEditText.getText().toString().isEmpty() ||
                districtEditText.getText().toString().isEmpty() ||
                phoneEditText.getText().toString().isEmpty()) {

            Toast.makeText(this, "Please fill in all address and phone fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate phone number format (basic validation)
        String phoneNumber = phoneEditText.getText().toString();
        if (!phoneNumber.matches("^[0-9]{10,11}$")) {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
