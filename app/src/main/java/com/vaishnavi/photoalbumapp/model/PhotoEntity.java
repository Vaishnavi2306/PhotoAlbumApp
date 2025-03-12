package com.vaishnavi.photoalbumapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "photo_table")  // ✅ Ensure this matches DAO query
public class PhotoEntity {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private int id;
    private String author;
    private String imageUrl;  // ✅ Renamed from "url" to "imageUrl" to avoid confusion

    public PhotoEntity(int id, String author, String imageUrl) {
        this.id = id;
        this.author = author;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getImageUrl() {  // ✅ Ensure this matches Glide
        return imageUrl;
    }
}
