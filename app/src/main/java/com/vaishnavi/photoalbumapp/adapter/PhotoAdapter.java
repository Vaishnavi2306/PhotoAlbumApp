package com.vaishnavi.photoalbumapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.vaishnavi.photoalbumapp.R;
import com.vaishnavi.photoalbumapp.model.Photo;
import com.vaishnavi.photoalbumapp.ui.FullImageActivity;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends PagingDataAdapter<Photo, PhotoAdapter.PhotoViewHolder> {
    private final List<String> imageUrls = new ArrayList<>();
    public PhotoAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Photo> DIFF_CALLBACK = new DiffUtil.ItemCallback<Photo>() {
        @Override
        public boolean areItemsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = getItem(position);
        if (photo != null) {
            holder.idTextView.setText(String.valueOf(photo.getId()));
            holder.textViewAuthor.setText(photo.getAuthor());

            Glide.with(holder.itemView.getContext())
                    .load(photo.getImageUrl())
                    .placeholder(R.drawable.placeholder_background)
                    .error(R.drawable.placeholder_foreground)
                    .into(holder.imageView);

            holder.imageView.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), FullImageActivity.class);
                intent.putExtra("position", position);
                holder.itemView.getContext().startActivity(intent);
            });


        }
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAuthor, idTextView;
        ImageView imageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.titleTextView);
            idTextView = itemView.findViewById(R.id.idTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
