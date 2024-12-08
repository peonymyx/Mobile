package Nhom2.com.example.doanmobile.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Nhom2.com.example.doanmobile.Adapter.PopularAdapter;
import Nhom2.com.example.doanmobile.Domain.ItemsDomain;
import Nhom2.com.example.doanmobile.R;
import Nhom2.com.example.doanmobile.databinding.ActivitySearchBinding;

public class SearchActivity extends BaseActivity {
    private ActivitySearchBinding binding;
    private ArrayList<ItemsDomain> allItems;
    private PopularAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        allItems = new ArrayList<>();
        adapter = new PopularAdapter(allItems);

        binding.searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.searchResultsRecyclerView.setAdapter(adapter);

        // Xử lý nút Back
        binding.backButton.setOnClickListener(v -> {
            finish(); // Đóng SearchActivity và quay về MainActivity
        });

        // Lấy từ khóa từ Intent
        String keyword = getIntent().getStringExtra("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            binding.searchEditText.setText(keyword); // Hiển thị từ khóa lên EditText
            searchItems(keyword); // Tự động tìm kiếm
        }

        // Nút tìm kiếm
        binding.searchButton.setOnClickListener(v -> searchItems(binding.searchEditText.getText().toString()));
    }

    private void searchItems(String keyword) {
        if (keyword.isEmpty()) {
            Toast.makeText(this, "Please enter a keyword", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Items");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allItems.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ItemsDomain item = dataSnapshot.getValue(ItemsDomain.class);
                    if (item != null && item.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                        allItems.add(item);
                    }
                }

                if (allItems.isEmpty()) {
                    binding.emptyTextView.setVisibility(View.VISIBLE); // Hiển thị thông báo
                    binding.searchResultsRecyclerView.setVisibility(View.GONE); // Ẩn RecyclerView
                } else {
                    binding.emptyTextView.setVisibility(View.GONE); // Ẩn thông báo
                    binding.searchResultsRecyclerView.setVisibility(View.VISIBLE); // Hiển thị RecyclerView
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this, "Search failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}