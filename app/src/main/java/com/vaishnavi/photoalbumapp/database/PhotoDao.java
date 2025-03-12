package com.vaishnavi.photoalbumapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.vaishnavi.photoalbumapp.model.PhotoEntity;
import java.util.List;

@Dao
public interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PhotoEntity> photos);

    @Query("SELECT * FROM photo_table")  // ✅ Ensure this matches PhotoEntity
    List<PhotoEntity> getAllPhotos();
}

