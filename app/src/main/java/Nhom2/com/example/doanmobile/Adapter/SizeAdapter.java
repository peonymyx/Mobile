package Nhom2.com.example.doanmobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import Nhom2.com.example.doanmobile.R;
import Nhom2.com.example.doanmobile.databinding.ViewholderSizeBinding;

import java.util.ArrayList;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.Viewholder> {
    ArrayList<String> items;
    Context context;
    int selectPosition = -1;
    int lastSelectedPosition = -1;
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String color);
    }
    public interface OnSizeSelectedListener {
        void onSizeSelected(String size);  // Truyền màu khi người dùng chọn
    }
    private OnSizeSelectedListener sizeSelectedListener;

    public SizeAdapter(ArrayList<String> items, OnSizeSelectedListener sizeSelectedListener) {
        this.items = items;
        this.sizeSelectedListener = sizeSelectedListener;
    }
    public SizeAdapter(ArrayList<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public SizeAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderSizeBinding binding = ViewholderSizeBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeAdapter.Viewholder holder, int position) {
        String size = items.get(position);
        holder.binding.sizeTxt.setText(size);

        // Thiết lập sự kiện click cho mục kích thước
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(size);  // Gọi phương thức trong listener với kích thước được chọn
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderSizeBinding binding;

        public Viewholder(ViewholderSizeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
