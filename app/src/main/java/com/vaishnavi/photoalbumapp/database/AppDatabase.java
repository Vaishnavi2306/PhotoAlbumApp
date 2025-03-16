package com.vaishnavi.photoalbumapp.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.vaishnavi.photoalbumapp.model.PhotoEntity;

@Database(entities = {PhotoEntity.class}, version = 8, exportSchema = false) // Define the database schema
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;

    // Abstract method for DAO
    public abstract PhotoDao photoDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE photo_table ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            
        }
    };

    // Singleton pattern to get a single instance of the database
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "photo_database")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                            .fallbackToDestructiveMigration()
                            .build();
                    Log.d("AppDatabase", "Database instance created");
                }
            }
        }
        return instance;
    }





}
