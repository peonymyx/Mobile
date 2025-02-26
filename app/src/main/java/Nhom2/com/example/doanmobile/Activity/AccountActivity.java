package Nhom2.com.example.doanmobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import Nhom2.com.example.doanmobile.Models.User;
import Nhom2.com.example.doanmobile.databinding.ActivityAccountBinding;

public class AccountActivity extends AppCompatActivity {
    private ActivityAccountBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Get current user
        String userID = getCurrentUserID();
        if (userID != null) {
            fetchUserData(userID);
        } else {
            Log.e("AccountActivity", "No logged-in user found.");
        }

        setupListeners();
    }

    private String getCurrentUserID() {
        if (mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser().getUid();
        }
        return null;
    }

    private void fetchUserData(String userID) {
        db.collection("users").document(userID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            Log.d("AccountActivity", "User data fetched successfully.");
                            updateUI(user);
                        } else {
                            Log.e("AccountActivity", "User object is null.");
                        }
                    } else {
                        Log.e("AccountActivity", "No such document in Firestore.");
                    }
                })
                .addOnFailureListener(e -> Log.e("AccountActivity", "Error fetching user data.", e));
    }

    private void updateUI(User user) {
        binding.nameUser.setText(user.getName() != null ? user.getName() : "N/A");
    }

    private void setupListeners() {
        // Back button
        binding.backBtn.setOnClickListener(v -> finish());

        // Profile button
        binding.profileBtn.setOnClickListener(v -> {
            String userID = getCurrentUserID();
            if (userID != null) {
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            } else {
                Log.e("AccountActivity", "UserID is null, cannot navigate to Profile.");
            }
        });

        // Orders button - Navigate to OrderListActivity
        binding.ordersBtn.setOnClickListener(v -> {
            // Mở OrderListActivity để hiển thị các đơn hàng
            Intent intent = new Intent(AccountActivity.this, OrderListActivity.class);
            startActivity(intent);
        });

        // Sự kiện click cho Store System
        binding.storeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, StoreSystemActivity.class);
            startActivity(intent);
        });

        // Sự kiện click cho FAQ
        binding.faqBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, FaqActivity.class);
            startActivity(intent);
        });

        // Sự kiện click cho Shipping Policy
        binding.shippingBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShippingPolicyActivity.class);
            startActivity(intent);
        });

        // Sự kiện click cho Terms of User
        binding.termsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, TermsOfUserActivity.class);
            startActivity(intent);
        });

        // Sự kiện click cho Privacy Policy
        binding.privacyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, PrivacyPolicyActivity.class);
            startActivity(intent);
        });
    }
}