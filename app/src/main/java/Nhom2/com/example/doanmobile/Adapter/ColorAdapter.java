package Nhom2.com.example.doanmobile.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import Nhom2.com.example.doanmobile.R;
import Nhom2.com.example.doanmobile.databinding.ViewholderColorBinding;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.Viewholder> {
    ArrayList<String> items;
    Context context;
    int selectPosition = -1;
    int lastSelectedPosition = -1;

    // Listener to notify item click
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Interface to notify the color selected
    public interface OnItemClickListener {
        void onItemClick(String color);  // Notify with selected color
    }

    // Constructor to accept color items
    public ColorAdapter(ArrayList<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ColorAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderColorBinding binding = ViewholderColorBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorAdapter.Viewholder holder, int position) {
        String color = items.get(position);

        // Set the color text or view properties
        holder.binding.colorLayout.setBackgroundColor(Color.parseColor(color));

        // Set click listener to notify item selection
        holder.itemView.setOnClickListener(v -> {
            lastSelectedPosition = selectPosition;
            selectPosition = holder.getAdapterPosition();
            notifyItemChanged(lastSelectedPosition);
            notifyItemChanged(selectPosition);

            // Notify listener with the selected color
            if (listener != null) {
                listener.onItemClick(color);
            }
        });

        // Update selection UI
        if (selectPosition == holder.getAdapterPosition()) {
            // Apply a specific style for selected color
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.color_selected);
            Glide.with(context)
                    .load(unwrappedDrawable)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.binding.colorLayout);
        } else {
            // Normal color item
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.color_selected);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(color));

            Glide.with(context)
                    .load(unwrappedDrawable)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.binding.colorLayout);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderColorBinding binding;

        public Viewholder(ViewholderColorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
