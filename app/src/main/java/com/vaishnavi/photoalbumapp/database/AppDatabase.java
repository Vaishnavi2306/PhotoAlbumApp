package com.vaishnavi.photoalbumapp.database;

import android.content.Context;
import android.util.Log;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.vaishnavi.photoalbumapp.model.PhotoEntity;

@Database(entities = {PhotoEntity.class}, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;

    public abstract PhotoDao photoDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "photo_database")
                            .fallbackToDestructiveMigration()
                            .build();
                    Log.d("AppDatabase", "Database instance created");
                }
            }
        }
        return instance;
    }
}
