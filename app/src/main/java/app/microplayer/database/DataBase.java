package app.microplayer.database;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import app.microplayer.model.Video;

@Database(entities = {Video.class},exportSchema = false, version = 1)
public abstract class DataBase extends RoomDatabase {
    private static DataBase dataBase;

    public static synchronized DataBase getInstance(Application application){
        if (dataBase==null){
            dataBase= Room.databaseBuilder(application,DataBase.class,application.getPackageName())
                    .fallbackToDestructiveMigration()
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .build();
        }
        return dataBase;
    }

    public abstract VideoDAO videoDAO();

    private static final Migration migration1to2=new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
}
