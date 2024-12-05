package Nhom2.com.example.doanmobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity2 extends BaseActivity {
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

    private void setupLogout() {
        // Add logout functionality
        binding.logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }


    private void bottomNavigation() {
        binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity2.this, CartActivity.class)));
    }

    private void initPopular() {
        DatabaseReference myref = database.getReference("Items");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items = new ArrayList<>();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));
                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(MainActivity2.this, 2));
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
        DatabaseReference myref = database.getReference("Category");
        binding.progressBarOfficial.setVisibility(View.VISIBLE);

        ArrayList<CategoryDomain> items = new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(CategoryDomain.class));
                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewOffical.setLayoutManager(new LinearLayoutManager(
                                MainActivity2.this, LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewOffical.setAdapter(new CategoryAdapter(items));
                        binding.recyclerViewOffical.setNestedScrollingEnabled(true);
                    }
                    binding.progressBarOfficial.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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