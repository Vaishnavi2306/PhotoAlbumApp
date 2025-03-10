package com.vaishnavi.photoalbumapp.model;

import com.google.gson.annotations.SerializedName;

public class Photo {
    @SerializedName("id")
    private String id;

    @SerializedName("author")
    private String author;

    @SerializedName("download_url")
    private String imageUrl;

    // Constructors
    public Photo(String id, String author, String imageUrl) {
        this.id = id;
        this.author = author;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
