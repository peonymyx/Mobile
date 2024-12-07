package Nhom2.com.example.doanmobile.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import Nhom2.com.example.doanmobile.Adapter.PopularAdapter;
import Nhom2.com.example.doanmobile.Domain.ItemsDomain;
import Nhom2.com.example.doanmobile.databinding.ActivityCategoryItemsBinding;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryItemsActivity extends BaseActivity {
    private ActivityCategoryItemsBinding binding;
    private String categoryName; // Danh mục nhận từ Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy tên danh mục từ Intent
        categoryName = getIntent().getStringExtra("categoryName");
        binding.nameBrand.setText(categoryName);
        // Hiển thị các sản phẩm thuộc danh mục này
        displayItemsByCategory(categoryName);
        setupBackButton();
    }
    private void setupBackButton() {
        binding.backBtn.setOnClickListener(v -> finish());
    }
    private void displayItemsByCategory(String category) {
        DatabaseReference myRef = database.getReference("Items");
        binding.progressBar.setVisibility(View.VISIBLE);

        ArrayList<ItemsDomain> filteredItems = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ItemsDomain item = issue.getValue(ItemsDomain.class);
                        if (item != null && item.getCategory() != null && item.getCategory().contains(category)) {
                            filteredItems.add(item);
                        }
                    }

                    if (!filteredItems.isEmpty()) {
                        PopularAdapter adapter = new PopularAdapter(filteredItems);
                        binding.brandlistView.setLayoutManager(new GridLayoutManager(CategoryItemsActivity.this, 2));
                        binding.brandlistView.setAdapter(adapter);
                        binding.brandlistView.setNestedScrollingEnabled(true);
                    } else {
                        binding.emptyState.setVisibility(View.VISIBLE); // Hiển thị trạng thái trống
                    }

                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(CategoryItemsActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
