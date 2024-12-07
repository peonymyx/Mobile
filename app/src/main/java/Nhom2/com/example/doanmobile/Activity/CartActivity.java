package Nhom2.com.example.doanmobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import Nhom2.com.example.doanmobile.Adapter.CartAdapter;
import Nhom2.com.example.doanmobile.Helper.ManagmentCart;
import Nhom2.com.example.doanmobile.Models.CartItem;
import Nhom2.com.example.doanmobile.databinding.ActivityCartBinding;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.checkoutBtn.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, CheckoutActivity.class)));
        managmentCart = new ManagmentCart(this);

        calculatorCart();
        setVariable();
        initCartList();

    }
    @Override
    protected void onResume() {
        super.onResume();

        // Reload cart data and update UI
        ArrayList<CartItem> cartItems = managmentCart.getListCart();
        CartAdapter cartAdapter = new CartAdapter(cartItems, this, this::calculatorCart);
        binding.cartView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
    }
    private void initCartList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, () -> calculatorCart()));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10;
        double tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100.0) / 100.0;
        double itemTotal = Math.round((managmentCart.getTotalFee() * 100.0)) / 100.0;

        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText("$" + total);
    }
}