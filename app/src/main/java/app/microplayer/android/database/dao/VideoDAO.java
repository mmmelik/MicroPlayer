package app.microplayer.android.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import app.microplayer.android.database.entity.VideoEntity;

@Dao
public interface VideoDAO {

    @Query("SELECT * FROM VideoEntity")
    LiveData<List<VideoEntity>> getAll();

    @Query("SELECT * FROM VideoEntity WHERE id=:id")
    LiveData<VideoEntity> getVideoById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<VideoEntity> videoEntities);

}
