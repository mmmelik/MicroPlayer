package app.microplayer.android.database;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import app.microplayer.android.AppExecutors;
import app.microplayer.android.database.dao.VideoDAO;
import app.microplayer.android.database.entity.VideoEntity;

@Database(entities = {VideoEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static String DATABASE_NAME = "microplayer";
    private static AppDatabase instance;

    public abstract VideoDAO videoDAO();

    public static AppDatabase getInstance(final Context context, final  AppExecutors executors) {
        if (instance==null){
            synchronized (AppDatabase.class){
                if (instance==null){
                    Log.d("here","here");
                    instance=buildDatabase(context,executors);
                }
            }
        }
        return instance;
    }

    private static AppDatabase buildDatabase(final Context appContext, final AppExecutors executors){
        return Room.databaseBuilder(appContext,AppDatabase.class,DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(() -> {
                            Log.d("Oncreate","executed");
                        });
                    }
                })
                .build();
    }

}
