package com.vaishnavi.photoalbumapp.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.vaishnavi.photoalbumapp.R;
import com.vaishnavi.photoalbumapp.adapter.ViewPagerAdapter;
import com.vaishnavi.photoalbumapp.database.AppDatabase;
import com.vaishnavi.photoalbumapp.model.PhotoEntity;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FullImageActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private List<PhotoEntity> imageList;
    private int startPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        viewPager = findViewById(R.id.viewPager);
        startPosition = getIntent().getIntExtra("position", 0);

        // Load images from Room Database on a background thread
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            imageList = db.photoDao().getAllPhotosFromDatabase();

            runOnUiThread(() -> {
                if (imageList != null && !imageList.isEmpty()) {
                    ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageList);
                    viewPager.setAdapter(adapter);
                    viewPager.setCurrentItem(startPosition, false);
                }
            });
        });
    }
}
