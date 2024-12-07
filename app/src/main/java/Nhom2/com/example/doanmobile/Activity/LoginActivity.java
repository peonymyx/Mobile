package Nhom2.com.example.doanmobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Nhom2.com.example.doanmobile.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    //truy cập các thành phần trong giao diện
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //kết nối layout activity_login.xml với LoginActivity thông qua View Binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        //hiển thị giao diện
        setContentView(binding.getRoot());

        //khởi tạo Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        //gọi hàm loginUser() để xử lý đăng nhập
        binding.loginBtn.setOnClickListener(v -> loginUser());

        //chuyển sang màn hình RegisterActivity để người dùng đăng ký tài khoản
        binding.signUp.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void loginUser() {
        //lấy thông tin từ giao diện
        String email = binding.emailEdt.getText().toString().trim();
        String password = binding.passwordEdt.getText().toString().trim();

        //Kiểm tra dữ liệu rỗng
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        //gửi thông tin đến Firebase để kiểm tra tính hợp lệ
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //lấy thông tin người dùng từ Firebase sau khi xác thực thành công
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            //lấy thông tin từ Firebase Auth
                            String uid = firebaseUser.getUid();
                            String email_db = firebaseUser.getEmail();

                            Log.d("FirebaseAuth", "User UID: " + uid);
                            Log.d("FirebaseAuth", "User Email: " + email_db);

                            //chuyển đến màn hình chính
                            Intent intent = new Intent(this, MainActivity.class);
                            //xóa mọi Activity trước đó để không quay lại màn hình đăng nhập
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        //hiển thị thông báo lỗi nếu email hoặc mật khẩu không chính xác
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}