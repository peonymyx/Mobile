package Nhom2.com.example.doanmobile.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Nhom2.com.example.doanmobile.Activity.OrderDetailActivity;
import Nhom2.com.example.doanmobile.Models.Order;
import Nhom2.com.example.doanmobile.R;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {
    private List<Order> orderList;
    private OnOrderClickListener listener;

    public OrderListAdapter(List<Order> orderList, OnOrderClickListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderTitle.setText(order.getOrderID());
        holder.orderStatus.setText(order.getStatus());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), OrderDetailActivity.class);
            intent.putExtra("orderID", order.getOrderID());
            v.getContext().startActivity(intent);
        });

        // Sự kiện nhấn nút Edit
        holder.editOrderBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditOrderClick(order);
            }
        });

        // Sự kiện nhấn nút Delete
        holder.deleteOrderBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteOrderClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // Interface để xử lý sự kiện nhấn vào đơn hàng
    public interface OnOrderClickListener {
        void onOrderClick(Order order);
        void onEditOrderClick(Order order); // Nhấn vào Edit
        void onDeleteOrderClick(Order order); // Nhấn vào Delete
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderTitle, orderStatus;
        Button editOrderBtn, deleteOrderBtn;

        public OrderViewHolder(View itemView) {
            super(itemView);
            orderTitle = itemView.findViewById(R.id.orderTitle);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            editOrderBtn = itemView.findViewById(R.id.editOrderBtn);
            deleteOrderBtn = itemView.findViewById(R.id.deleteOrderBtn);
        }
    }
}
