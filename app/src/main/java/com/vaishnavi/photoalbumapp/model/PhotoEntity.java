package com.vaishnavi.photoalbumapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "photo_table")// Defines a Room database table
public class PhotoEntity {
    @PrimaryKey(autoGenerate = false) //  Photo ID should be unique
    @NonNull
    private int id;
    private String author;
    private String imageUrl;
    private boolean isFavorite;//  Stores favorite status in the database

    // Constructor
    public PhotoEntity(int id, String author, String imageUrl,boolean isFavorite) {
        this.id = id;
        this.author = author;
        this.imageUrl = imageUrl;
        this.isFavorite = isFavorite;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isFavorite() { return isFavorite; }

    // Setter for favorite status
    public void setFavorite(boolean favorite) { this.isFavorite = favorite; }

}
