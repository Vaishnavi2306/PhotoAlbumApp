package com.vaishnavi.photoalbumapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData; // Add this import

import com.vaishnavi.photoalbumapp.model.Photo;
import com.vaishnavi.photoalbumapp.network.ApiService;
import com.vaishnavi.photoalbumapp.network.PhotoPagingSource;

public class PhotoViewModel extends ViewModel {
    private final ApiService apiService;

    public PhotoViewModel(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<PagingData<Photo>> getPhotos() {
        // Create the Pager with configuration and data source
        Pager<Integer, Photo> pager = new Pager<>(
                new PagingConfig(
                        10,  // Page size
                        5,  // Prefetch distance
                        false // Enable placeholders
                ),
                () -> new PhotoPagingSource(apiService) // Data source
        );

        // Convert Pager to LiveData<PagingData> using PagingLiveData
        return PagingLiveData.getLiveData(pager);
    }
}