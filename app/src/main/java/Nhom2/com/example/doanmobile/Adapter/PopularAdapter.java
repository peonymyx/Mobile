package Nhom2.com.example.doanmobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import Nhom2.com.example.doanmobile.Activity.DetailActivity;
import Nhom2.com.example.doanmobile.Domain.ItemsDomain;
import Nhom2.com.example.doanmobile.databinding.ViewholderPopularBinding;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Viewholder> {
    private ArrayList<ItemsDomain> items;
    private Context context;

    public PopularAdapter(ArrayList<ItemsDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public PopularAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderPopularBinding binding = ViewholderPopularBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.Viewholder holder, int position) {
        ItemsDomain item = items.get(position);

        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.reviewTxt.setText(String.valueOf(item.getReview()));
        holder.binding.priceTxt.setText("$" + item.getPrice());
        holder.binding.ratingTxt.setText("(" + item.getRating() + ")");
        holder.binding.oldPriceTxt.setText("$" + item.getOldPrice());
        holder.binding.oldPriceTxt.setPaintFlags(holder.binding.oldPriceTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.binding.ratingBar.setRating((float) item.getRating());

        // Sử dụng Glide để tải ảnh
        RequestOptions options = new RequestOptions();
        options = options.transform(new CenterCrop());
        Glide.with(context)
                .load(item.getPicUrl().get(0)) // Giả sử mỗi sản phẩm có ít nhất 1 ảnh
                .apply(options)
                .into(holder.binding.pic);

        // Sự kiện click vào sản phẩm
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", item); // Truyền đối tượng sản phẩm sang DetailActivity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Phương thức để cập nhật danh sách sản phẩm
    public void updateItems(ArrayList<ItemsDomain> newItems) {
        this.items = newItems;
        notifyDataSetChanged(); // Cập nhật giao diện
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderPopularBinding binding;

        public Viewholder(ViewholderPopularBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
