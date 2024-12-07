package Nhom2.com.example.doanmobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import Nhom2.com.example.doanmobile.Activity.CategoryItemsActivity;  // Thêm import cho Activity
import Nhom2.com.example.doanmobile.Domain.CategoryDomain;
import Nhom2.com.example.doanmobile.databinding.ViewholderCategoryBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<CategoryDomain> items;
    private Context context;

    public CategoryAdapter(ArrayList<CategoryDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderCategoryBinding binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryDomain category = items.get(position);

        holder.binding.title.setText(category.getTitle());
        Glide.with(context).load(category.getPicUrl()).into(holder.binding.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CategoryItemsActivity.class);
            intent.putExtra("categoryName", category.getTitle()); // Truyền tên danh mục qua Intent
            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderCategoryBinding binding;

        public ViewHolder(ViewholderCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
