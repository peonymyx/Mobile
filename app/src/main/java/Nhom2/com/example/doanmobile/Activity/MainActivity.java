package Nhom2.com.example.doanmobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import Nhom2.com.example.doanmobile.Adapter.CategoryAdapter;
import Nhom2.com.example.doanmobile.Adapter.PopularAdapter;
import Nhom2.com.example.doanmobile.Adapter.SliderAdapter;
import Nhom2.com.example.doanmobile.Domain.CategoryDomain;
import Nhom2.com.example.doanmobile.Domain.ItemsDomain;
import Nhom2.com.example.doanmobile.Domain.SliderItems;
import Nhom2.com.example.doanmobile.databinding.ActivityMainBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        initBanner();
        initCategory();
        initPopular();
        bottomNavigation();
    }

    private void bottomNavigation() {
        binding.WishList.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WishlistActivity.class)));

        binding.searchBtn.setOnClickListener(v -> {
            String keyword = binding.editTextText.getText().toString(); // Lấy từ khóa từ EditText (nếu có)
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra("keyword", keyword); // Truyền từ khóa qua Intent
            startActivity(intent);
        });

        binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
        binding.accountBtn.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String userID = user.getUid();
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                intent.putExtra("userID", userID);

                startActivity(intent);
            }
        });
    }

    private void initPopular() {
        DatabaseReference myRef = database.getReference("Items");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));
                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                        binding.recyclerViewPopular.setAdapter(new PopularAdapter(items));
                        binding.recyclerViewPopular.setNestedScrollingEnabled(true);
                    }
                    binding.progressBarPopular.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarOfficial.setVisibility(View.VISIBLE);

        ArrayList<CategoryDomain> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        CategoryDomain category = issue.getValue(CategoryDomain.class);
                        if (category != null) {
                            items.add(category);
                        }
                    }

                    if (!items.isEmpty()) {
                        binding.recyclerViewOffical.setLayoutManager(new LinearLayoutManager(
                                MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewOffical.setAdapter(new CategoryAdapter(items));
                        binding.recyclerViewOffical.setNestedScrollingEnabled(true);
                    } else {
//                        binding.emptyStateCategory.setVisibility(View.VISIBLE); // Trạng thái không có danh mục
                    }

                    binding.progressBarOfficial.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarOfficial.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Lỗi tải danh mục!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void banners(ArrayList<SliderItems> items) {
        binding.viewPagerSlider.setAdapter(new SliderAdapter(items, binding.viewPagerSlider));
        binding.viewPagerSlider.setClipToPadding(false);
        binding.viewPagerSlider.setClipChildren(false);
        binding.viewPagerSlider.setOffscreenPageLimit(3);
        binding.viewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewPagerSlider.setPageTransformer(compositePageTransformer);
    }

}
