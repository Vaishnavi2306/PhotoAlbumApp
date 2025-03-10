package com.vaishnavi.photoalbumapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.paging.PagingData;
import androidx.paging.PagingDataAdapter;
import com.vaishnavi.photoalbumapp.adapter.PhotoAdapter;
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

        ApiService apiService = RetrofitClient.getInstance();
        photoViewModel = new ViewModelProvider(this, new PhotoViewModelFactory(apiService))
                .get(PhotoViewModel.class);

        photoViewModel.getPhotos().observe(this, pagingData -> adapter.submitData(getLifecycle(), pagingData));
    }
}
