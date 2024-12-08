package Nhom2.com.example.doanmobile.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import Nhom2.com.example.doanmobile.databinding.ActivityPrivacyPolicyBinding;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private ActivityPrivacyPolicyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Thêm nút quay lại
        binding.backBtn.setOnClickListener(v -> finish());
    }
}
