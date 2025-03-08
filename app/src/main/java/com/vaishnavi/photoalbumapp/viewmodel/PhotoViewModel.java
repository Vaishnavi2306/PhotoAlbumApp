package com.vaishnavi.photoalbumapp.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.vaishnavi.photoalbumapp.model.Photo;
import com.vaishnavi.photoalbumapp.network.ApiService;
import com.vaishnavi.photoalbumapp.network.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoViewModel extends ViewModel {
    private MutableLiveData<List<Photo>> photosLiveData = new MutableLiveData<>();

    public LiveData<List<Photo>> getPhotos() {
        return photosLiveData;
    }

    public void fetchPhotos() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<Photo>> call = apiService.getPhotos(1, 30);

        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    photosLiveData.setValue(response.body());
                    Log.d("API SUCCESS", "Fetched " + response.body().size() + " photos.");
                } else {
                    Log.e("API ERROR", "Response failed");
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Log.e("API ERROR", "Failure: " + t.getMessage());
            }
        });
    }
}
