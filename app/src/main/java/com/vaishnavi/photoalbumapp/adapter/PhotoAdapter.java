package com.vaishnavi.photoalbumapp.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vaishnavi.photoalbumapp.R;
import com.vaishnavi.photoalbumapp.model.Photo;

import com.vaishnavi.photoalbumapp.ui.FullImageActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
        Photo photo = getItem(position); //  Get the photo item at position
        if (photo != null) {
            holder.idTextView.setText(String.valueOf(photo.getId()));
            holder.textViewAuthor.setText(photo.getAuthor());

            //  Load image using Glide with placeholder and error handling

            Glide.with(holder.itemView.getContext())
                    .load(photo.getImageUrl())
                    .placeholder(R.drawable.placeholder_background)
                    .error(R.drawable.placeholder_foreground)
                    .into(holder.imageView);

            // Handle image click event to open full image view

            holder.imageView.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), FullImageActivity.class);
                intent.putExtra("position", position);
                holder.itemView.getContext().startActivity(intent);
            });

            // **Favorite Button Logic**

            updateFavoriteIcon(holder.favoriteButton, photo.isFavorite());

            holder.favoriteButton.setOnClickListener(v -> {
                boolean isFavorite = !photo.isFavorite();
                photo.setFavorite(isFavorite);
                updateFavoriteIcon(holder.favoriteButton, isFavorite);
            });

            updateFavoriteIcon(holder.favoriteButton, photo.isFavorite());

            holder.saveButton.setOnClickListener(v -> {
                saveImageToGallery(photo.getImageUrl(), holder.itemView.getContext());
            });
        }
    }

    //  Update Favorite Button UI based on state

    private void updateFavoriteIcon(ImageView favoriteButton, boolean isFavorite) {
        if (isFavorite) {
            favoriteButton.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            favoriteButton.setImageResource(R.drawable.baseline_favorite_border_24);
        }
    }

    @Override
    public int getItemCount() {
        return getItemCountInternal();
    }

    private int getItemCountInternal() {
        return snapshot().size();
    }

    // ViewHolder for holding UI components of each item

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAuthor, idTextView;
        ImageView imageView,favoriteButton, saveButton;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.titleTextView);
            idTextView = itemView.findViewById(R.id.idTextView);
            imageView = itemView.findViewById(R.id.imageView);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
            saveButton = itemView.findViewById(R.id.btnSaveImage);
        }
    }

    //  Save Image to Gallery

    private void saveImageToGallery(String imageUrl, Context context) {
        Glide.with(context)
                .asBitmap()
                .load(imageUrl) // Load the full image from URL, not ImageView
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        saveBitmapToGallery(bitmap, context); // Save full bitmap
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });


    }

    //  Save Bitmap to Device Gallery

    private void saveBitmapToGallery(Bitmap bitmap, Context context) {
        OutputStream fos = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, "photo_" + System.currentTimeMillis() + ".jpg");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/PhotoAlbumApp");

                Uri imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if (imageUri != null) {
                    fos = context.getContentResolver().openOutputStream(imageUri);
                }
            } else {
                File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/PhotoAlbumApp/");
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                File file = new File(directory, "photo_" + System.currentTimeMillis() + ".jpg");
                fos = new FileOutputStream(file);
            }

            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
                Toast.makeText(context, "Image Saved to Gallery", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error Saving Image", Toast.LENGTH_SHORT).show();
        }
    }


}
