package com.vaishnavi.photoalbumapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vaishnavi.photoalbumapp.network.ApiService;
import com.vaishnavi.photoalbumapp.database.PhotoDao;

@SuppressWarnings("unchecked")
public class PhotoViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final ApiService apiService;
    private final PhotoDao photoDao;

    public PhotoViewModelFactory(Application application, ApiService apiService, PhotoDao photoDao) {
        this.application = application;
        this.apiService = apiService;
        this.photoDao = photoDao;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PhotoViewModel.class)) {
            return (T) new PhotoViewModel(application, apiService, photoDao);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
