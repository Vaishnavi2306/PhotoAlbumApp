package com.vaishnavi.photoalbumapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.paging.PagingData;
import androidx.paging.PagingDataAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.vaishnavi.photoalbumapp.adapter.PhotoAdapter;
import com.vaishnavi.photoalbumapp.database.AppDatabase;
import com.vaishnavi.photoalbumapp.network.ApiService;
import com.vaishnavi.photoalbumapp.network.RetrofitClient;
import com.vaishnavi.photoalbumapp.viewmodel.PhotoViewModel;
import com.vaishnavi.photoalbumapp.viewmodel.PhotoViewModelFactory;

public class MainActivity extends AppCompatActivity {
    private PhotoViewModel photoViewModel;
    private PhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PhotoAdapter();
        recyclerView.setAdapter(adapter);

        ApiService apiService = RetrofitClient.getInstance(this).create(ApiService.class);
        AppDatabase database = AppDatabase.getInstance(this);
        photoViewModel = new ViewModelProvider(this,
                new PhotoViewModelFactory(getApplication(), apiService, database.photoDao()))
                .get(PhotoViewModel.class);


        photoViewModel.getPhotos().observe(this, pagingData -> {
            if (pagingData == null || pagingData.equals(PagingData.empty())) {
                Snackbar.make(findViewById(R.id.recyclerView), "No internet. Loading cached data.", Snackbar.LENGTH_LONG).show();
            } else {
                adapter.submitData(getLifecycle(), pagingData);
            }
        });
    }
}
