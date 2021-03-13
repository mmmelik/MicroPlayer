package app.microplayer.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import app.microplayer.model.Video;

@Dao
public interface VideoDAO {

    @Query("SELECT * FROM Video WHERE id=:id")
    LiveData<Video> getVideoById(long id);

    @Query("SELECT * FROM Video")
    LiveData<List<Video>> getAll();

    @Insert
    void addVideo(Video video);

    @Insert
    void addVideoBatch(List<Video> video);

    @Update
    void updateVideo(Video video);

    @Delete
    void deleteVideo(Video video);
}
