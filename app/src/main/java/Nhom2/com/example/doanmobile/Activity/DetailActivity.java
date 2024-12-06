package Nhom2.com.example.doanmobile.Activity;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import Nhom2.com.example.doanmobile.Adapter.ColorAdapter;
import Nhom2.com.example.doanmobile.Adapter.SizeAdapter;
import Nhom2.com.example.doanmobile.Adapter.SliderAdapter;
import Nhom2.com.example.doanmobile.Domain.ItemsDomain;
import Nhom2.com.example.doanmobile.Domain.SliderItems;
import Nhom2.com.example.doanmobile.Helper.ManagmentCart;
import Nhom2.com.example.doanmobile.Models.CartItem;
import Nhom2.com.example.doanmobile.Models.User;
import Nhom2.com.example.doanmobile.databinding.ActivityDetailBinding;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private ItemsDomain object;
    private int numberOrder = 1;
    private ManagmentCart managmentCart;

    String selectedColor = "";
    String selectedSize = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        getBundles();
        initBanners();
        initSize();
        initColor();
    }

    private void handleSelectColor(String color) {
        selectedColor = color;  // Store the selected color
    }
    private void initColor() {
        ArrayList<String> list = new ArrayList<>();
        list.add("#006fc4");
        list.add("#daa048");
        list.add("#398d41");
        list.add("#0c3c72");
        list.add("#829db5");
        ColorAdapter colorAdapter = new ColorAdapter(list);
        binding.recyclerColor.setAdapter(colorAdapter);
        binding.recyclerColor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Listen for color selection
        colorAdapter.setOnItemClickListener((color) -> {
            handleSelectColor(color);
        });
    }

    private void handleSelectedSize(String size){
        selectedSize = size; // Lưu kích thước được chọn
    }
    private void initSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("S");
        list.add("M");
        list.add("L");
        list.add("XL");
        list.add("XXL");
        SizeAdapter sizeAdapter = new SizeAdapter(list);
        binding.recyclerSize.setAdapter(sizeAdapter);
        binding.recyclerSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Lắng nghe sự kiện chọn kích thước
        sizeAdapter.setOnItemClickListener((size) -> {
            handleSelectedSize(size);
        });
    }

    private void initBanners() {
        ArrayList<SliderItems> sliderItems = new ArrayList<>();
        for (int i = 0; i < object.getPicUrl().size(); i++) {
            sliderItems.add(new SliderItems(object.getPicUrl().get(i)));
        }
        binding.viewpagerSlider.setAdapter(new SliderAdapter(sliderItems, binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setClipChildren(false);
        binding.viewpagerSlider.setOffscreenPageLimit(3);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    private void getBundles() {
        object = (ItemsDomain) getIntent().getSerializableExtra("object");
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("$" + object.getPrice());
        binding.ratingBar.setRating((float) object.getRating());
        binding.ratingTxt.setText(object.getRating() + " Rating");
        binding.descriptionTxt.setText(object.getDescription());

        binding.AddtoCartBtn.setOnClickListener(v -> {
            // Create a new CartItem from the selected item (ItemsDomain)
            CartItem cartItem = new CartItem();
            cartItem.setTitle(object.getTitle());
            cartItem.setPrice(object.getPrice());
            cartItem.setQuantity(numberOrder);
            cartItem.setPicUrl(object.getPicUrl());
            cartItem.setSize(selectedSize);  // Assuming selectedSize is set somewhere
            cartItem.setColor(selectedColor);  // Assuming selectedColor is set somewhere

            Log.d("CartItem: ", selectedColor);
            // Insert the item into the cart
            managmentCart.insertItem(cartItem);
        });
        binding.wishListBtn.setOnClickListener(v -> {
            addWishList(object);
        });

        binding.backBtn.setOnClickListener(v -> finish());
    }
    private void addWishList(ItemsDomain item) {
        // Lấy ID người dùng hiện tại
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Lấy tham chiếu đến Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy tài liệu người dùng từ Firestore
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Lấy đối tượng User từ Firestore
                        User currentUser = documentSnapshot.toObject(User.class);

                        if (currentUser != null) {
                            // Kiểm tra xem danh sách wishList có sẵn không
                            if (currentUser.getWishList() == null) {
                                currentUser.setWishList(new ArrayList<>());  // Nếu không có thì tạo mới danh sách
                            }

                            // Thêm món đồ vào danh sách yêu thích
                            currentUser.getWishList().add(item);

                            // Cập nhật lại danh sách yêu thích của người dùng trong Firestore
                            db.collection("users").document(userId).set(currentUser)
                                    .addOnSuccessListener(aVoid -> {
                                        // Hiển thị thông báo khi đã thêm vào danh sách yêu thích
                                        Toast.makeText(DetailActivity.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Xử lý lỗi khi không thể cập nhật Firestore
                                        Toast.makeText(DetailActivity.this, "Lỗi khi lưu vào danh sách yêu thích: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // Xử lý trường hợp không tìm thấy người dùng
                        Toast.makeText(DetailActivity.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi không thể lấy tài liệu người dùng
                    Toast.makeText(DetailActivity.this, "Lỗi khi lấy dữ liệu người dùng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        }
}