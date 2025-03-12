package com.vaishnavi.photoalbumapp.repository;

import android.content.Context;
import com.vaishnavi.photoalbumapp.database.AppDatabase;
import com.vaishnavi.photoalbumapp.database.PhotoDao;
import com.vaishnavi.photoalbumapp.network.ApiService;
import com.vaishnavi.photoalbumapp.network.RetrofitClient;

public class PhotoRepository {
    private final ApiService apiService;
    private final PhotoDao photoDao;

    public PhotoRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).create(ApiService.class);
        this.photoDao = AppDatabase.getInstance(context).photoDao();
    }

    public ApiService getApiService() {
        return apiService;
    }

    public PhotoDao getPhotoDao() {
        return photoDao;
    }
}
