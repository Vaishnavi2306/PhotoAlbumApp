package com.vaishnavi.photoalbumapp.database;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.vaishnavi.photoalbumapp.model.PhotoEntity;
import java.util.List;

@Dao
public interface PhotoDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PhotoEntity> photos);

    // PagingSource for pagination
    @Query("SELECT * FROM photo_table ORDER BY id ASC")
    PagingSource<Integer, PhotoEntity> getAllPhotosPagingSource();

    @Query("SELECT * FROM photo_table WHERE isFavorite = 1 ORDER BY id ASC")
    LiveData<List<PhotoEntity>> getFavoritePhotos();

    @Update
    void updatePhoto(PhotoEntity photo);

    @Query("UPDATE photo_table SET isFavorite = :isFavorite WHERE id = :photoId")
    void updateFavoriteStatus(int photoId, boolean isFavorite);

    // Standard list for fallback caching
    @Query("SELECT * FROM photo_table ORDER BY id ASC")
    List<PhotoEntity> getAllPhotosFromDatabase();

    // Search by author name (case-insensitive) or by ID prefix match
    @Query("SELECT * FROM photo_table WHERE LOWER(author) LIKE '%' || LOWER(:searchQuery) || '%' OR CAST(id AS TEXT) LIKE :searchQuery || '%' ORDER BY id ASC")
    PagingSource<Integer, PhotoEntity> searchPhotosPagingSource(String searchQuery);

    @Query("SELECT * FROM photo_table WHERE LOWER(author) LIKE '%' || LOWER(:searchQuery) || '%' OR CAST(id AS TEXT) LIKE :searchQuery || '%' ORDER BY id ASC")
    List<PhotoEntity> searchPhotos(String searchQuery);

    @Query("SELECT * FROM photo_table ORDER BY id ASC LIMIT :pageSize OFFSET :offset")
    List<PhotoEntity> getPagedPhotos(int pageSize, int offset);
}
