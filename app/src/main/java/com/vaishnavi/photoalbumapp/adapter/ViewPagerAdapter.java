package com.vaishnavi.photoalbumapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.vaishnavi.photoalbumapp.R;
import com.vaishnavi.photoalbumapp.model.PhotoEntity;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    private Context context;
    private List<PhotoEntity> imageList;


    // Constructor to initialize context and image list
    public ViewPagerAdapter(Context context, List<PhotoEntity> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout for full-screen image item
        View view = LayoutInflater.from(context).inflate(R.layout.item_fullscreen_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Load image using Glide with placeholder and error handling
        Glide.with(context)
                .load(imageList.get(position).getImageUrl())  // Load the image
                .placeholder(R.drawable.placeholder_background)
                .error(R.drawable.placeholder_foreground)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    // ViewHolder to hold image reference
    public static class ViewHolder extends RecyclerView.ViewHolder {
        PhotoView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.fullImageView); // Reference to ImageView in layout
        }
    }
}
