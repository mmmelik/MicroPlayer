package app.microplayer.android.database.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import app.microplayer.android.database.AppDatabase;
import app.microplayer.android.database.entity.VideoEntity;

public class VideoRepository {
    private final AppDatabase appDatabase;
    private static VideoRepository instance;
    private final MediatorLiveData<List<VideoEntity>> observableVideoList;


    public VideoRepository(final AppDatabase appDatabase) {
        this.appDatabase=appDatabase;
        observableVideoList=new MediatorLiveData<>();
        observableVideoList.addSource(appDatabase.videoDAO().getAll(), new Observer<List<VideoEntity>>() {
            @Override
            public void onChanged(List<VideoEntity> videoEntities) {
                observableVideoList.postValue(videoEntities);
            }
        });
    }

    public static VideoRepository getInstance(final AppDatabase appDatabase) {
        if (instance==null){
            synchronized (VideoRepository.class){
                if (instance==null){
                    instance=new VideoRepository(appDatabase);
                }
            }
        }
        return instance;
    }

    public LiveData<List<VideoEntity>> getAll(){
        return observableVideoList;
    }

    public LiveData<VideoEntity> getVideoById(String id){
        return appDatabase.videoDAO().getVideoById(id);
    }

    public void insertAll(List<VideoEntity> videoEntities){
        appDatabase.videoDAO().insertAll(videoEntities);
    }
}
