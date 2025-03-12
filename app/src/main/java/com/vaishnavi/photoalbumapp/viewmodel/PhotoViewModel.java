package com.vaishnavi.photoalbumapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.vaishnavi.photoalbumapp.database.PhotoDao;
import com.vaishnavi.photoalbumapp.model.Photo;
import com.vaishnavi.photoalbumapp.network.ApiService;
import com.vaishnavi.photoalbumapp.network.PhotoPagingSource;

public class PhotoViewModel extends ViewModel {
    private final Application application;
    private final ApiService apiService;
    private final PhotoDao photoDao;

    public PhotoViewModel(Application application, ApiService apiService, PhotoDao photoDao) {
        this.application = application;
        this.apiService = apiService;
        this.photoDao = photoDao;
    }

    public LiveData<PagingData<Photo>> getPhotos() {
        Pager<Integer, Photo> pager = new Pager<>(
                new PagingConfig(20, 5, false),
                () -> new PhotoPagingSource(apiService, photoDao)  // Correct instantiation
        );

        return PagingLiveData.getLiveData(pager);
    }
}
