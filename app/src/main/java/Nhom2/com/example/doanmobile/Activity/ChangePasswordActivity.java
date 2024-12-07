package Nhom2.com.example.doanmobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Nhom2.com.example.doanmobile.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        // Get data from Intent
        String userID = getIntent().getStringExtra("userID");
        binding.tvUserId.setText(userID);

        setupBackButton();
        setupSetPasswordButton();
    }

    private void setupBackButton() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void setupSetPasswordButton() {
        binding.btnSetPassword.setOnClickListener(v -> {
            String currentPassword = binding.etOldPassword.getText().toString();
            String newPassword = binding.etNewPassword.getText().toString();
            String confirmPassword = binding.etConfirmPassword.getText().toString();

            if (newPassword.isBlank()) {
                Toast.makeText(ChangePasswordActivity.this, "Please enter your new password.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (confirmPassword.isBlank()) {
                Toast.makeText(ChangePasswordActivity.this, "Please confirm your new password.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(ChangePasswordActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser user = mAuth.getCurrentUser();
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    user.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(ChangePasswordActivity.this, "Password changed.", Toast.LENGTH_SHORT).show();
                            // Logout and return to intro for re-authentication
                            mAuth.signOut();
                            Intent intent = new Intent(this, IntroActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Cannot change your password.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}