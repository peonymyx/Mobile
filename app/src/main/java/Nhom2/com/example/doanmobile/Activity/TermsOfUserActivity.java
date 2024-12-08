package Nhom2.com.example.doanmobile.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import Nhom2.com.example.doanmobile.databinding.ActivityTermsOfUserBinding;

public class TermsOfUserActivity extends AppCompatActivity {
    private ActivityTermsOfUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTermsOfUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Thêm nút quay lại
        binding.backBtn.setOnClickListener(v -> finish());
    }
}
