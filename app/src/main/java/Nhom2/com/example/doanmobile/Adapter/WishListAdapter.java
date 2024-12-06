package Nhom2.com.example.doanmobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import Nhom2.com.example.doanmobile.Activity.DetailActivity;
import Nhom2.com.example.doanmobile.Domain.ItemsDomain;
import Nhom2.com.example.doanmobile.databinding.ViewholderPopularBinding;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.Viewholder> {
    private ArrayList<ItemsDomain> items;
    private Context context;

    public WishListAdapter(ArrayList<ItemsDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        // Inflate the layout and return the Viewholder
        ViewholderPopularBinding binding = ViewholderPopularBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        // Set data to the views
        holder.binding.titleTxt.setText(items.get(position).getTitle());
        holder.binding.reviewTxt.setText(String.valueOf(items.get(position).getReview()));
        holder.binding.priceTxt.setText("$" + items.get(position).getPrice());
        holder.binding.ratingTxt.setText("(" + items.get(position).getRating() + ")");
        holder.binding.oldPriceTxt.setText("$" + items.get(position).getOldPrice());
        holder.binding.oldPriceTxt.setPaintFlags(holder.binding.oldPriceTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.binding.ratingBar.setRating((float) items.get(position).getRating());

        // Set the product image using Glide
        RequestOptions options = new RequestOptions();
        options = options.transform(new com.bumptech.glide.load.resource.bitmap.CenterCrop());

        Glide.with(context)
                .load(items.get(position).getPicUrl().get(0))
                .apply(options)
                .into(holder.binding.pic);

        // Set item click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", items.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        ViewholderPopularBinding binding;

        public Viewholder(ViewholderPopularBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
