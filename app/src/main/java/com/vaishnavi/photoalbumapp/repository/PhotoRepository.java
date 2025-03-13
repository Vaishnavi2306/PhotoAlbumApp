package com.vaishnavi.photoalbumapp.repository;

import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingLiveData;
import androidx.lifecycle.LiveData;
import androidx.paging.PagingData;
import com.vaishnavi.photoalbumapp.database.PhotoDao;
import com.vaishnavi.photoalbumapp.model.PhotoEntity;

public class PhotoRepository {
    private final PhotoDao photoDao;

    public PhotoRepository(PhotoDao photoDao) {
        this.photoDao = photoDao;
    }

    public LiveData<PagingData<PhotoEntity>> getPhotos() {
        Pager<Integer, PhotoEntity> pager = new Pager<>(
                new PagingConfig(20, 5, false),
                () -> photoDao.getAllPhotosPagingSource()
        );
        return PagingLiveData.getLiveData(pager);
    }

    public LiveData<PagingData<PhotoEntity>> searchPhotos(String query) {
        Pager<Integer, PhotoEntity> pager = new Pager<>(
                new PagingConfig(20),
                () -> photoDao.searchPhotosPagingSource(query)
        );
        return PagingLiveData.getLiveData(pager);
    }
}
