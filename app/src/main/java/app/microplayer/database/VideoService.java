package app.microplayer.database;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;

import app.microplayer.model.Video;

public class VideoService {

    private DataBase dataBase;
    private VideoDAO videoDAO;

    public VideoService(Application application) {
        dataBase=DataBase.getInstance(application);
        videoDAO=dataBase.videoDAO();
    }

    public LiveData<List<Video>> getAllVideos(){
        return videoDAO.getAll();
    }

    public LiveData<Video> getVideoById(long id){
        return videoDAO.getVideoById(id);
    }

    public LiveData<List<Video>> getAllOf(String path){
        return videoDAO.getAllOf(path);
    }

    public void addVideo(Video video,JobListener jobListener){
        new Thread(){
            @Override
            public void run() {
                try {
                    videoDAO.addVideo(video);
                    jobListener.onFinish();
                }catch (Exception e){
                    jobListener.onFail(e);
                }
            }
        }.start();
    }

    public void addVideoBatch(List<Video> videos,JobListener jobListener){
        new Thread(){
            @Override
            public void run() {
                try {
                    videoDAO.addVideoBatch(videos);
                    jobListener.onFinish();
                }catch (Exception e){
                    jobListener.onFail(e);
                }
            }
        }.start();
    }

    public void updateVideo(Video video,JobListener jobListener){
        new Thread(){
            @Override
            public void run() {
                try {
                    videoDAO.updateVideo(video);
                    jobListener.onFinish();
                }catch (Exception e){
                    jobListener.onFail(e);
                }
            }
        }.start();
    }

    public void hideVideo(Video video,JobListener jobListener){
        new Thread(){
            @Override
            public void run() {
                try {
                    video.setHidden(true);
                    videoDAO.updateVideo(video);
                    jobListener.onFinish();
                }catch (Exception e){
                    jobListener.onFail(e);
                }
            }
        }.start();
    }

    public void showVideo(Video video,JobListener jobListener){
        new Thread(){
            @Override
            public void run() {
                try {
                    video.setHidden(false);
                    videoDAO.updateVideo(video);
                    jobListener.onFinish();
                }catch (Exception e){
                    jobListener.onFail(e);
                }
            }
        }.start();
    }

    public interface JobListener{
        void onFinish();
        void onFail(Exception e);
    }

}
