package app.microplayer.android;

import android.app.Application;

import app.microplayer.android.database.AppDatabase;
import app.microplayer.android.database.repository.VideoRepository;

/**
 * Base Application. Access singletons here.
 */
public class BaseApplication extends Application {

    private AppExecutors appExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        appExecutors=new AppExecutors();
    }

    public AppExecutors getAppExecutors() {
        return appExecutors;
    }

    public AppDatabase getDatabase(){
        return AppDatabase.getInstance(this,appExecutors);
    }

    public VideoRepository getVideoRepository(){
        return VideoRepository.getInstance(getDatabase());
    }
}
