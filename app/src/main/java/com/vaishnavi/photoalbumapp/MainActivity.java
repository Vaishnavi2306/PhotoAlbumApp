package com.vaishnavi.photoalbumapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vaishnavi.photoalbumapp.adapter.PhotoAdapter;
import com.vaishnavi.photoalbumapp.model.Photo;
import com.vaishnavi.photoalbumapp.network.ApiService;
import com.vaishnavi.photoalbumapp.network.RetrofitClient;
import com.vaishnavi.photoalbumapp.viewmodel.PhotoViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;
    private ProgressBar progressBar;
    private PhotoViewModel photoViewModel;

    @Override
    //Initializes the UI, sets up RecyclerView & calls fetchPhotos() to load images.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        photoAdapter = new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);

        // Fetch Data
        //Calls the API using Retrofit and updates the RecyclerView with fetched images.
        fetchPhotos();
    }

    private void fetchPhotos() {
        progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<Photo>> call = apiService.getPhotos(1, 30);

        call.enqueue(new Callback<List<Photo>>() {
            @Override
            //Receives API response, updates UI & logs success/failure.
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    photoAdapter.setPhotos(response.body());
                    Log.d("API RESPONSE", "Fetched " + response.body().size() + " photos.");
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load images", Toast.LENGTH_SHORT).show();
                    Log.e("API ERROR", "Response failed");
                }
            }

            //Handles API failures & shows an error message to the user.
            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API ERROR", "Failure: " + t.getMessage());
            }
        });
    }
}
