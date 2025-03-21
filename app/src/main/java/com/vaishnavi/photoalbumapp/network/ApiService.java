package com.vaishnavi.photoalbumapp.network;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.vaishnavi.photoalbumapp.model.Photo;

public interface ApiService {
    @GET("v2/list")// API endpoint to fetch the photo list
    Call<List<Photo>> getPhotos(@Query("page") int page, @Query("limit") int limit);
}
