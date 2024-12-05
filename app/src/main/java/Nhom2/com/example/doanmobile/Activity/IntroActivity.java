package Nhom2.com.example.doanmobile.Activity;

import android.content.Intent;
import android.os.Bundle;

import Nhom2.com.example.doanmobile.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
    private ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Navigate to Login Activity
        binding.textView4.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        // Start app main flow
        binding.startBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

    }
}