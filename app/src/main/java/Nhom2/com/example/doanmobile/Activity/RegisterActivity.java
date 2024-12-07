package Nhom2.com.example.doanmobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import Nhom2.com.example.doanmobile.Models.CartItem;
import Nhom2.com.example.doanmobile.Models.Order;
import Nhom2.com.example.doanmobile.Models.Review;
import Nhom2.com.example.doanmobile.Models.User;
import Nhom2.com.example.doanmobile.Models.WishListItem;
import Nhom2.com.example.doanmobile.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    //View Binding giúp dễ dàng truy cập các thành phần trong giao diện mà không cần dùng findViewById
    private ActivityRegisterBinding binding;

    //Firebase Authentication dùng để xác thực người dùng
    private FirebaseAuth mAuth;

    //Firebase Firestore dùng để lưu trữ thông tin người dùng trong cơ sở dữ liệu NoSQL
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Gán sự kiện cho nút đăng ký
        binding.registerBtn.setOnClickListener(v -> registerUser());

        //Gán sự kiện khi nhấn vào dòng chữ "Đăng nhập" sẽ chuyển sang màn hình LoginActivity
        binding.loginText.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        //Lấy thông tin từ giao diện
        String name = binding.nameEdt.getText().toString().trim();
        String email = binding.emailEdt.getText().toString().trim();
        String password = binding.passwordEdt.getText().toString().trim();
        String confirmPassword = binding.confirmPasswordEdt.getText().toString().trim();

        //Kiểm tra các trường nhập liệu
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        //Tạo tài khoản người dùng với Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //Khi đăng ký thành công, lấy User ID của người dùng vừa tạo
                        String userID = task.getResult().getUser().getUid();

                        //Tạo một đối tượng User để lưu thông tin người dùng
                        User newUser = new User(
                                userID,
                                name,
                                email,
                                new ArrayList<CartItem>(),
                                new ArrayList<WishListItem>(),
                                new ArrayList<Order>(),
                                new ArrayList<Review>()
                        );

                        //Lưu thông tin người dùng vào Firestore
                        db.collection("users").document(userID)
                                .set(newUser)
                                .addOnSuccessListener(aVoid -> {
                                    //Hiển thị thông báo thành công và chuyển sang màn hình LoginActivity
                                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    // Nếu lưu thông tin vào Firestore thất bại, hiển thị thông báo lỗi
                                    Toast.makeText(RegisterActivity.this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}