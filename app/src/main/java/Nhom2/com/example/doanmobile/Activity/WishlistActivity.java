package Nhom2.com.example.doanmobile.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import Nhom2.com.example.doanmobile.Adapter.WishListAdapter;
import Nhom2.com.example.doanmobile.Domain.ItemsDomain;
import Nhom2.com.example.doanmobile.Models.User;
import Nhom2.com.example.doanmobile.R;

public class WishlistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WishListAdapter adapter;
    private ArrayList<ItemsDomain> wishListItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerView_wishlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy danh sách yêu thích của người dùng hiện tại
        fetchCurrentUserWishlist();
    }

    private void fetchCurrentUserWishlist() {
        // Lấy ID người dùng hiện tại
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Lấy tham chiếu đến Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy dữ liệu người dùng từ Firestore
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Lấy đối tượng User từ Firestore
                        User currentUser = documentSnapshot.toObject(User.class);

                        // Kiểm tra xem danh sách yêu thích có sẵn không và gán nó vào danh sách địa phương
                        if (currentUser != null && currentUser.getWishList() != null) {
                            wishListItems.addAll(currentUser.getWishList());
                        }

                        // Thiết lập adapter với dữ liệu danh sách yêu thích đã lấy
                        adapter = new WishListAdapter(wishListItems);
                        recyclerView.setAdapter(adapter);
                    } else {
                        // Xử lý trường hợp không tìm thấy dữ liệu người dùng
                        Toast.makeText(WishlistActivity.this, "Dữ liệu người dùng không tìm thấy", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                    Toast.makeText(WishlistActivity.this, "Lỗi khi lấy dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
