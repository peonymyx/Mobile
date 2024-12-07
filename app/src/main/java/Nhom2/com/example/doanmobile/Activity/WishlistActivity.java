package Nhom2.com.example.doanmobile.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import Nhom2.com.example.doanmobile.Adapter.WishListAdapter;
import Nhom2.com.example.doanmobile.Domain.ItemsDomain;
import Nhom2.com.example.doanmobile.Models.User;
import Nhom2.com.example.doanmobile.R;
import Nhom2.com.example.doanmobile.databinding.ActivityWishlistBinding;  // Import the generated ViewBinding class

public class WishlistActivity extends AppCompatActivity {

    private ActivityWishlistBinding binding;  // Declare the ViewBinding variable
    private WishListAdapter adapter;
    private ArrayList<ItemsDomain> wishListItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the ViewBinding
        binding = ActivityWishlistBinding.inflate(getLayoutInflater());  // Use inflate to bind the layout
        setContentView(binding.getRoot());  // Set the root view

        // Initialize RecyclerView using binding
//        binding.wishlistView.setLayoutManager(new LinearLayoutManager(this));
        binding.wishlistView.setLayoutManager(new GridLayoutManager(this, 2));
        // Set up the back button using binding
        binding.backBtn.setOnClickListener(v -> finish());  // Make sure 'backBtn' is correctly referenced

        // Fetch and display the user's wishlist
        fetchCurrentUserWishlist();
    }

    private void fetchCurrentUserWishlist() {
        // Get the current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get Firestore reference
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Fetch user data from Firestore
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get the User object from Firestore
                        User currentUser = documentSnapshot.toObject(User.class);

                        // Clear the previous wishlist items to avoid duplicates
                        wishListItems.clear();

                        // Check if the wishlist exists and add items to the local list
                        if (currentUser != null && currentUser.getWishList() != null) {
                            wishListItems.addAll(currentUser.getWishList());
                        }

                        // Set up the adapter with the wishlist data
                        adapter = new WishListAdapter(wishListItems);
                        binding.wishlistView.setAdapter(adapter);  // Use binding.wishlistView
                    } else {
                        // Handle case when user data is not found
                        Toast.makeText(WishlistActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle error when fetching data
                    Toast.makeText(WishlistActivity.this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
