package Nhom2.com.example.doanmobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import Nhom2.com.example.doanmobile.Models.User;
import Nhom2.com.example.doanmobile.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Get userID from Intent
        userID = getIntent().getStringExtra("userID");

        if (userID != null) {
            fetchUserData(userID);
        } else {
            Log.e("ProfileActivity", "UserID is null.");
        }

        setupListeners();
    }

    private void fetchUserData(String userID) {
        db.collection("users").document(userID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            Log.d("ProfileActivity", "User data fetched successfully.");
                            updateUI(user);
                        } else {
                            Log.e("ProfileActivity", "User object is null.");
                        }
                    } else {
                        Log.e("ProfileActivity", "No such document in Firestore.");
                    }
                })
                .addOnFailureListener(e -> Log.e("ProfileActivity", "Error fetching user data.", e));
    }

    private void updateUI(User user) {
        binding.nameUser.setText(user.getName() != null ? user.getName() : "N/A");
        binding.tvUserId.setText(user.getUserID() != null ? user.getUserID() : "N/A");
        binding.tvEmail.setText(user.getEmail() != null ? user.getEmail() : "N/A");
    }

    private void setupListeners() {
        // Back button
        binding.backBtn.setOnClickListener(v -> finish());

        // Change password button
        binding.changePassBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });

        // Edit button
        binding.editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });

        // Logout button
        binding.logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
