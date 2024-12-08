package Nhom2.com.example.doanmobile.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.UUID;

import Nhom2.com.example.doanmobile.Adapter.CartAdapter;
import Nhom2.com.example.doanmobile.Helper.ManagmentCart;
import Nhom2.com.example.doanmobile.Models.Address;
import Nhom2.com.example.doanmobile.Models.CartItem;
import Nhom2.com.example.doanmobile.Models.Order;
import Nhom2.com.example.doanmobile.Models.User;
import Nhom2.com.example.doanmobile.databinding.ActivityCheckoutBinding;

public class CheckoutActivity extends BaseActivity {
    private ActivityCheckoutBinding binding;
    private ManagmentCart managmentCart;
    private double tax;
    private Address shippingAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        // Hiển thị danh sách sản phẩm trong giỏ hàng
        initCartList();
        // Cập nhật thông tin giỏ hàng (chi phí, thuế, tổng cộng)
        calculatorCart();
        // Xử lý sự kiện nút quay lại
        setVariable();

        // Cập nhật khi người dùng nhập địa chỉ
        binding.checkoutBtn.setOnClickListener(v -> onCheckoutClicked());
    }

    private void initCartList() {

        binding.productListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.productListView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, () -> calculatorCart()));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100.0) / 100.0;
        double itemTotal = Math.round((managmentCart.getTotalFee() * 100.0)) / 100.0;

        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText("$" + total);
    }

    private void onCheckoutClicked() {
        // Lấy thông tin địa chỉ
        String city = binding.cityEditText.getText().toString();
        String district = binding.districtEditText.getText().toString();
        String commune = binding.communeEditText.getText().toString();
        String details = binding.detailsEditText.getText().toString();
        String phone = binding.phone.getText().toString();

        // Kiểm tra xem các trường nhập có đầy đủ không
        if (city.isEmpty() || district.isEmpty() || commune.isEmpty() || details.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng Address từ thông tin nhập vào
        shippingAddress = new Address(city, district, commune, details);

        // Tạo Order từ giỏ hàng và thông tin nhập
        String orderID = UUID.randomUUID().toString(); // Tạo ID đơn hàng ngẫu nhiên
        ArrayList<CartItem> cartItems = managmentCart.getListCart();

        // Tạo đối tượng Order
        Order order = new Order(orderID, "Pending", shippingAddress, phone, cartItems);

        // Lưu thông tin đơn hàng vào Firestore
        saveOrderToFirebase(order);

        // Hiển thị thông báo
        Toast.makeText(this, "Order created: " + order.getOrderID(), Toast.LENGTH_LONG).show();

        // Xóa giỏ hàng
        managmentCart.clearCart();

        // Cập nhật giao diện về trạng thái ban đầu
        resetCartUI();

        // Đóng màn hình
        finish(); // Quay lại trang trước hoặc chuyển đến trang khác
    }

    private void resetCartUI() {
        // Cập nhật các giá trị về 0
        binding.totalFeeTxt.setText("$0.00");
        binding.taxTxt.setText("$0.00");
        binding.deliveryTxt.setText("$0.00");
        binding.totalTxt.setText("$0.00");

        // Làm trống danh sách sản phẩm trong giỏ hàng
        binding.productListView.setAdapter(new CartAdapter(new ArrayList<>(), this, () -> {}));
    }

    // Lưu đơn hàng vào Firebase Firestore
    private void saveOrderToFirebase(Order order) {
        // Lấy ID người dùng hiện tại
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Lấy tham chiếu tới collection "users"
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy tài liệu người dùng từ Firestore
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Lấy đối tượng User từ Firestore
                        User currentUser = documentSnapshot.toObject(User.class);

                        if (currentUser != null) {
                            // Kiểm tra xem người dùng đã có danh sách đơn hàng chưa
                            if (currentUser.getOrders() == null) {
                                currentUser.setOrders(new ArrayList<>());  // Nếu không có thì tạo mới danh sách
                            }

                            // Thêm đơn hàng vào danh sách đơn hàng của người dùng
                            currentUser.getOrders().add(order);

                            // Cập nhật lại tài liệu người dùng trong Firestore
                            db.collection("users").document(userId).set(currentUser)
                                    .addOnSuccessListener(aVoid -> {
                                        // Thành công
                                        Toast.makeText(CheckoutActivity.this, "Order saved to user profile", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Lỗi
                                        Toast.makeText(CheckoutActivity.this, "Failed to save order to user profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // Xử lý trường hợp không tìm thấy người dùng
                        Toast.makeText(CheckoutActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Lỗi khi lấy dữ liệu người dùng
                    Toast.makeText(CheckoutActivity.this, "Failed to get user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
