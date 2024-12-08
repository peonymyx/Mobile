package Nhom2.com.example.doanmobile.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import Nhom2.com.example.doanmobile.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText emailEditText;
    private Button resetPasswordButton;
    private TextView loginTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.emailEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        loginTextView = findViewById(R.id.loginTextView);

        mAuth = FirebaseAuth.getInstance();

        // Xử lý nút "Reset Password"
        resetPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else {
                resetPassword(email);
            }
        });

        // Xử lý nút "Back to Login"
        loginTextView.setOnClickListener(v -> {
            finish(); // Quay lại trang đăng nhập
        });
    }

    // Gửi yêu cầu đặt lại mật khẩu
    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                        finish(); // Quay lại trang đăng nhập sau khi gửi email
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}