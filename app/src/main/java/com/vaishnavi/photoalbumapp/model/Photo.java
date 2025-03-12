package com.vaishnavi.photoalbumapp.model;

import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Photo {
    private int id;

    @SerializedName("download_url")
    private String imageUrl;

    private String author;

    public Photo(int id, String author, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAuthor() {
        return author;
    }
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
