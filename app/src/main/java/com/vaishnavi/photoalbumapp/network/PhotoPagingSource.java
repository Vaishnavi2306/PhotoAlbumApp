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
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import kotlin.coroutines.Continuation;

public class PhotoPagingSource extends PagingSource<Integer, Photo> {
    private final ApiService apiService;
    private final PhotoDao photoDao;
    private final String searchQuery;  // Search Query field

    // **Updated constructor** to allow `searchQuery` as optional
    public PhotoPagingSource(ApiService apiService, PhotoDao photoDao, String searchQuery) {
        this.apiService = apiService;
        this.photoDao = photoDao;
        this.searchQuery = searchQuery != null ? searchQuery.toLowerCase() : null; // Case insensitive
    }

    @NonNull
    @Override
    public LoadResult<Integer, Photo> load(
            @NonNull LoadParams<Integer> params,
            @NonNull Continuation<? super LoadResult<Integer, Photo>> continuation
    ) {
        int page = params.getKey() != null ? params.getKey() : 1;
        int pageSize = 20;

        try {
            Future<LoadResult<Integer, Photo>> future = Executors.newSingleThreadExecutor().submit(() -> {
                //  **If there's a search query, fetch from the database**
                if (searchQuery != null && !searchQuery.isEmpty()) {
                    List<Photo> filteredPhotos = new ArrayList<>();
                    List<PhotoEntity> photoEntities;
                    try {
                        photoEntities = photoDao.searchPhotos(searchQuery); // Safe DB query
                    } catch (Exception e) {
                        Log.e("PhotoPagingSource", "Database search failed: " + e.getMessage(), e);
                        return new LoadResult.Error<>(e);
                    }
                    for (PhotoEntity entity : photoEntities) {
                        filteredPhotos.add(new Photo(entity.getId(), entity.getAuthor(), entity.getImageUrl(), entity.isFavorite()));
                    }
                    return new LoadResult.Page<>(filteredPhotos, null, null); // No pagination for search
                }

                // Fetch photos from API
                List<Photo> photos;
                try {
                    photos = apiService.getPhotos(page, pageSize).execute().body();
                } catch (IOException e) {
                    Log.e("PhotoPagingSource", "API fetch failed, loading from cache...");
                    return loadFromCache(page, pageSize);
                }

                if (photos == null || photos.isEmpty()) {
                    return loadFromCache(page, pageSize);
                }

                 // Save API results to the database
                List<PhotoEntity> photoEntities = new ArrayList<>();
                for (Photo photo : photos) {
                    photoEntities.add(new PhotoEntity(photo.getId(), photo.getAuthor(), photo.getImageUrl(),photo.isFavorite()));
                }
                photoDao.insertAll(photoEntities);

                int nextPage = page + 1;
                return new LoadResult.Page<>(photos, null, nextPage);
            });

            return future.get();
        } catch (Exception e) {
            Log.e("PhotoPagingSource", "Fetching from API failed. Loading from cache...");
            return loadFromCache(page, pageSize);
        }
    }


    // Load paginated data from the database if the API call fails
    private LoadResult<Integer, Photo> loadFromCache(int page, int pageSize) {
        try {
            int offset = (page - 1) * pageSize;
            List<PhotoEntity> cachedPhotos = photoDao.getPagedPhotos(pageSize, offset);


            if (cachedPhotos == null || cachedPhotos.isEmpty()) {
                return new LoadResult.Error<>(new IOException("No internet and no cached data"));
            }

            List<Photo> photos = new ArrayList<>();
            for (PhotoEntity entity : cachedPhotos) {
                photos.add(new Photo(entity.getId(), entity.getAuthor(), entity.getImageUrl(),entity.isFavorite()));
            }

            // **Filter cached photos if search query exists**
            if (searchQuery != null) {
                List<Photo> filteredPhotos = new ArrayList<>();
                for (Photo photo : photos) {
                    if (photo.getAuthor().toLowerCase().contains(searchQuery) ||
                            String.valueOf(photo.getId()).equals(searchQuery)) {
                        filteredPhotos.add(photo);
                    }
                }
                if (filteredPhotos.isEmpty()) {
                    return new LoadResult.Error<>(new IOException("No matching results found in cache"));
                }
                photos = filteredPhotos;
            }

            return new LoadResult.Page<>(photos, null, null);
        } catch (Exception e) {
            Log.e("PhotoPagingSource", "Loading from cache Failed...");
            throw e;
        }
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
