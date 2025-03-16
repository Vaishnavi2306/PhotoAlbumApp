package com.vaishnavi.photoalbumapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.vaishnavi.photoalbumapp.database.PhotoDao;
import com.vaishnavi.photoalbumapp.network.ApiService;

public class PhotoViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final ApiService apiService;
    private final PhotoDao photoDao;

    // Constructor to initialize dependencies
    public PhotoViewModelFactory(Application application, ApiService apiService, PhotoDao photoDao) {
        this.application = application;
        this.apiService = apiService;
        this.photoDao = photoDao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        // Check if the requested ViewModel is PhotoViewModel
        if (modelClass.isAssignableFrom(PhotoViewModel.class)) {
            return (T) new PhotoViewModel(application, apiService, photoDao);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
