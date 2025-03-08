package com.vaishnavi.photoalbumapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vaishnavi.photoalbumapp.R;
import com.vaishnavi.photoalbumapp.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private List<Photo> photoList = new ArrayList<>();

    public void setPhotos(List<Photo> photos) {
        this.photoList = photos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = photoList.get(position);
        holder.authorTextView.setText(photo.getAuthor());

        Glide.with(holder.itemView.getContext())
                .load(photo.getImageUrl())
                .placeholder(R.drawable.placeholder_background)
                .into(holder.photoImageView);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;
        TextView authorTextView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
        }
    }
}
