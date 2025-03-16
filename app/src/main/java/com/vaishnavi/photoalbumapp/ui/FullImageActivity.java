package com.vaishnavi.photoalbumapp.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.vaishnavi.photoalbumapp.R;
import com.vaishnavi.photoalbumapp.adapter.ViewPagerAdapter;
import com.vaishnavi.photoalbumapp.database.AppDatabase;
import com.vaishnavi.photoalbumapp.model.PhotoEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FullImageActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private List<PhotoEntity> imageList;
    private int startPosition;
    private ImageView btnFavorite, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        viewPager = findViewById(R.id.viewPager);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnSave = findViewById(R.id.btnSave);

        startPosition = getIntent().getIntExtra("position", 0);

        // Load images from Room Database
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            imageList = db.photoDao().getAllPhotosFromDatabase();

            runOnUiThread(() -> {
                if (imageList != null && !imageList.isEmpty()) {
                    ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageList);
                    viewPager.setAdapter(adapter);
                    viewPager.setCurrentItem(startPosition, false);
                    updateFavoriteIcon(imageList.get(startPosition).isFavorite());
                }
            });
        });

        // Handle Favorite Click
        btnFavorite.setOnClickListener(v -> toggleFavorite());

        // Handle Save Click
        btnSave.setOnClickListener(v -> saveImageToGallery());
    }

    private void toggleFavorite() {
        int position = viewPager.getCurrentItem();
        if (imageList != null && position < imageList.size()) {
            PhotoEntity photo = imageList.get(position);
            photo.setFavorite(!photo.isFavorite());

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                AppDatabase.getInstance(this).photoDao().updatePhoto(photo);
                runOnUiThread(() -> {
                    updateFavoriteIcon(photo.isFavorite());
                    Toast.makeText(this, "Updated Favorite Status", Toast.LENGTH_SHORT).show();
                });
            });
        }
    }

    private void updateFavoriteIcon(boolean isFavorite) {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24);
        }
    }

    private void saveImageToGallery() {
        int position = viewPager.getCurrentItem();
        if (imageList != null && position < imageList.size()) {
            PhotoEntity photo = imageList.get(position);
            BitmapDrawable drawable = (BitmapDrawable) btnSave.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            String savedImagePath = saveBitmapToGallery(bitmap);
            if (savedImagePath != null) {
                Toast.makeText(this, "Image Saved: " + savedImagePath, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to Save Image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String saveBitmapToGallery(Bitmap bitmap) {
        OutputStream fos;
        String imageFileName = "Photo_" + System.currentTimeMillis() + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/PhotoAlbumApp");

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File imageFile = new File(storageDir, imageFileName);
        try {
            fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, imageFile.getAbsolutePath());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            return imageFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
