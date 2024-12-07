package Nhom2.com.example.doanmobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import Nhom2.com.example.doanmobile.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Login button click listener
        binding.loginBtn.setOnClickListener(v -> loginUser());

        // Register text click listener
        binding.registerText.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void loginUser() {
        String email = binding.emailEdt.getText().toString().trim();
        String password = binding.passwordEdt.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Lấy thông tin từ Firebase Auth
                            String uid = firebaseUser.getUid();
                            String email_db = firebaseUser.getEmail();
                            String displayName = firebaseUser.getDisplayName();

                            Log.d("FirebaseAuth", "User UID: " + uid);
                            Log.d("FirebaseAuth", "User Email: " + email_db);
                            Log.d("FirebaseAuth", "User Display Name: " + displayName);

                            // Chuyển đến màn hình chính
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra("userID", uid);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}