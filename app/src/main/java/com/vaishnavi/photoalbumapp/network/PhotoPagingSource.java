package com.vaishnavi.photoalbumapp.network;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.paging.PagingSource;
import androidx.paging.PagingState;
import com.vaishnavi.photoalbumapp.database.PhotoDao;
import com.vaishnavi.photoalbumapp.model.Photo;
import com.vaishnavi.photoalbumapp.model.PhotoEntity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import kotlin.coroutines.Continuation;

public class PhotoPagingSource extends PagingSource<Integer, Photo> {
    private final ApiService apiService;
    private final ExecutorService executor;
    private final PhotoDao photoDao;

    public PhotoPagingSource(ApiService apiService, PhotoDao photoDao) {
        this.apiService = apiService;
        this.photoDao = photoDao;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @NonNull
    @Override
    public Object load(
            @NonNull LoadParams<Integer> params,
            @NonNull Continuation<? super LoadResult<Integer, Photo>> continuation
    ) {
        int page = params.getKey() != null ? params.getKey() : 1;

        try {
            Future<LoadResult<Integer, Photo>> future = executor.submit(() -> {
                List<Photo> photos;
                try {
                    photos = apiService.getPhotos(page, params.getLoadSize()).execute().body();
                } catch (IOException e) {
                    Log.e("PhotoPagingSource", "API fetch failed, loading from cache...");
                    return loadFromCache();
                }

                if (photos == null || photos.isEmpty()) {
                    return loadFromCache();
                }

                List<PhotoEntity> photoEntities = new ArrayList<>();
                for (Photo photo : photos) {
                    photoEntities.add(new PhotoEntity(photo.getId(), photo.getAuthor(), photo.getImageUrl()));
                }
                photoDao.insertAll(photoEntities);

                int nextPage = page + 1;
                return new LoadResult.Page<>(photos, null, nextPage);
            });

            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("PhotoPagingSource", "Fetching from API failed. Loading from cache...");
            return loadFromCache();
        }
    }

    private LoadResult<Integer, Photo> loadFromCache() {
        List<PhotoEntity> cachedPhotos = photoDao.getAllPhotos();
        if (cachedPhotos.isEmpty()) {
            return new LoadResult.Error<>(new IOException("No internet and no cached data"));
        }

        List<Photo> photos = new ArrayList<>();
        for (PhotoEntity entity : cachedPhotos) {
            photos.add(new Photo(entity.getId(), entity.getAuthor(), entity.getImageUrl()));
        }

        return new LoadResult.Page<>(photos, null, null);
    }

    @NonNull
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Photo> state) {
        Integer anchorPosition = state.getAnchorPosition();
        if (anchorPosition == null) {
            return 1;
        }
        LoadResult.Page<Integer, Photo> closestPage = state.closestPageToPosition(anchorPosition);
        if (closestPage == null || closestPage.getNextKey() == null) {
            return 1;
        }

        return closestPage.getNextKey();
    }
}
