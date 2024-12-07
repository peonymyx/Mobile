package Nhom2.com.example.doanmobile.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import Nhom2.com.example.doanmobile.Models.User;
import Nhom2.com.example.doanmobile.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {
    private ActivityEditProfileBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userID;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Get data from Intent
        userID = getIntent().getStringExtra("userID");
        binding.tvUserId.setText(userID);
        fetchCurrentUserData();

        setupBackButton();
        setupSaveButton();
    }

    private void setupBackButton() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void fetchCurrentUserData() {
        db.collection("users").document(userID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            currentUser = user;
                            Log.d("ProfileActivity", "User data fetched successfully.");
                        } else {
                            Log.e("ProfileActivity", "User object is null.");
                        }
                    } else {
                        Log.e("ProfileActivity", "No such document in Firestore.");
                    }
                })
                .addOnFailureListener(e -> Log.e("ProfileActivity", "Error fetching user data.", e));
    }

    private void updateUser(User updatedUser) {
        db.collection("users").document(userID)
                .set(updatedUser)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void setupSaveButton() {
        binding.btnSaveChanges.setOnClickListener(v -> {
            // Get data from edit text
            String newName = binding.etUserName.getText().toString();
            String newEmail = binding.etEmail.getText().toString().trim();

            if (newName.isBlank() || newEmail.isBlank()) {
                Toast.makeText(EditProfileActivity.this, "Please enter your new profile information.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update username
            User updatedUser = new User(
                    userID,
                    newName,
                    newEmail,
                    currentUser.getCart(),
                    currentUser.getWishList(),
                    currentUser.getOrders(),
                    currentUser.getReviews()
            );

            if (newEmail.equals(currentUser.getEmail())) {
                updateUser(updatedUser);
                return;
            }

            mAuth.getCurrentUser().updateEmail(newEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            updateUser(updatedUser);
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Update failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}