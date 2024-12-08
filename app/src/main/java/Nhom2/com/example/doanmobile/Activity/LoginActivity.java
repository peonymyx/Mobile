package Nhom2.com.example.doanmobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.google.android.gms.common.api.ApiException;

import Nhom2.com.example.doanmobile.R;
import Nhom2.com.example.doanmobile.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private ActivityLoginBinding binding; // Binding cho layout XML
    private FirebaseAuth mAuth; // Đối tượng Firebase Authentication
    private GoogleSignInClient mGoogleSignInClient; // Đối tượng để xử lý Google Sign-In
    private ActivityResultLauncher<Intent> googleSignInLauncher; // Launcher cho kết quả Google Sign-In

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ánh xạ layout qua View Binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Nhận ID token để xác thực Firebase
                .requestEmail() // Yêu cầu email của người dùng
                .build();

        // Tạo GoogleSignInClient
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Thiết lập Launcher để xử lý kết quả Google Sign-In
        setupGoogleSignInLauncher();

        // Xử lý sự kiện khi bấm nút đăng nhập bằng Email
        binding.loginBtn.setOnClickListener(v -> loginUser());

        // Xử lý sự kiện khi bấm nút Google Sign-In
        binding.googleSignInBtn.setOnClickListener(v -> signInWithGoogle());

        // Chuyển sang màn hình đăng ký
        binding.signUp.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    // Thiết lập Google Sign-In Result Launcher
    private void setupGoogleSignInLauncher() {
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        try {
                            // Lấy thông tin tài khoản Google sau khi người dùng chọn
                            GoogleSignInAccount account =
                                    GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);

                            if (account != null) {
                                // Xác thực tài khoản Google với Firebase
                                firebaseAuthWithGoogle(account);
                            } else {
                                Log.e(TAG, "GoogleSignInAccount is null");
                                Toast.makeText(this, "Không lấy được thông tin tài khoản Google", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ApiException e) {
                            // Đăng nhập Google thất bại
                            Log.e(TAG, "Google sign in failed: " + e.getMessage(), e);
                            Toast.makeText(this, "Đăng nhập Google thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.e(TAG, "Google Sign-In bị hủy.");
                        Toast.makeText(this, "Đăng nhập Google bị hủy", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    // Bắt đầu quá trình đăng nhập bằng Google
    private void signInWithGoogle() {
        Log.d(TAG, "Bắt đầu đăng nhập Google");
        // Reset tài khoản trước đó để hiển thị hộp thoại chọn tài khoản mỗi lần đăng nhập
        mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });
    }

    // Xác thực tài khoản Google với Firebase
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle: " + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập thành công, cập nhật giao diện người dùng
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUIAfterGoogleSignIn(user);
                    } else {
                        // Đăng nhập thất bại
                        Exception exception = task.getException();
                        Log.e(TAG, "signInWithCredential:failure", exception);
                        Toast.makeText(this, "Đăng nhập Firebase thất bại: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Đăng nhập bằng email và mật khẩu
    private void loginUser() {
        String email = binding.emailEdt.getText().toString().trim();
        String password = binding.passwordEdt.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        updateUIAfterEmailSignIn(firebaseUser);
                    } else {
                        Exception exception = task.getException();
                        Log.e(TAG, "Đăng nhập email thất bại", exception);
                        Toast.makeText(this, "Đăng nhập thất bại: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Cập nhật giao diện sau khi đăng nhập bằng email
    private void updateUIAfterEmailSignIn(FirebaseUser user) {
        if (user != null) {
            Log.d(TAG, "UID người dùng: " + user.getUid());
            Log.d(TAG, "Email người dùng: " + user.getEmail());

            navigateToMainActivity();
        }
    }

    // Cập nhật giao diện sau khi đăng nhập Google
    private void updateUIAfterGoogleSignIn(FirebaseUser user) {
        if (user != null) {
            Log.d(TAG, "Google UID: " + user.getUid());
            Log.d(TAG, "Google Email: " + user.getEmail());

            navigateToMainActivity();
        }
    }

    // Điều hướng sang màn hình chính
    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
