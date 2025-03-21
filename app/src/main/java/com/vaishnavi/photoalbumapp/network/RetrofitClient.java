package com.vaishnavi.photoalbumapp.network;

import android.content.Context;
import java.io.File;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getInstance(Context context) {
        if (retrofit == null) {
            File httpCacheDirectory = new File(context.getCacheDir(), "responses");
            Cache cache = new Cache(httpCacheDirectory, 50 * 1024 * 1024); // 50MB cache

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                //OkHttpClient with Interceptor
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            // Build the Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://picsum.photos/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
