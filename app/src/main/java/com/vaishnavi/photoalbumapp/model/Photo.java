package com.vaishnavi.photoalbumapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Photo {
    private int id;

    @SerializedName("download_url") // Maps JSON field `download_url` to `imageUrl`
    private String imageUrl;

    private String author;

    @ColumnInfo(name = "isFavorite") // Used for local favorite tracking (not in API response)
    private boolean isFavorite;

    // Constructor
    public Photo(int id, String author, String imageUrl, boolean isFavorite) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.author = author;
        this.isFavorite = isFavorite;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    // Setter for favorite status
    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    // Equals method for comparing objects
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Photo photo = (Photo) obj;
        return id == photo.id &&
                Objects.equals(imageUrl, photo.imageUrl) &&
                Objects.equals(author, photo.author);
    }

}
