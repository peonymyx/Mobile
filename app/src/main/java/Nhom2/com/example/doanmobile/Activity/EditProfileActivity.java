package Nhom2.com.example.doanmobile.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import Nhom2.com.example.doanmobile.databinding.ActivityEditProfileBinding;

import Nhom2.com.example.doanmobile.R;
import Nhom2.com.example.doanmobile.databinding.ActivityProfileBinding;

public class EditProfileActivity extends AppCompatActivity {
    private ActivityEditProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBackButton();
    }
    private void setupBackButton() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}