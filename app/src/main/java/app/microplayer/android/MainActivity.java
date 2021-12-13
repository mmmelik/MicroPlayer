package app.microplayer.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import app.microplayer.android.database.AppDatabase;
import app.microplayer.android.database.entity.VideoEntity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertVideos();
            }
        });
        findViewById(R.id.click2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Video Count:", String.valueOf(((BaseApplication)getApplication()).getVideoRepository().getAll().getValue().size()));
            }
        });
    }
    private void insertVideos(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
        }else {
            ((BaseApplication)getApplication()).getAppExecutors().diskIO().execute(()->{
                ((BaseApplication)getApplication()).getVideoRepository().insertAll(getAllVideos(getApplicationContext()));
            });
        }
    }
    private static List<VideoEntity> getAllVideos(final Context appContext){
        final String[] projection ={
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.MIME_TYPE};
        Cursor cursor = appContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,projection,null,null,null);
        List<VideoEntity> videos=new ArrayList<>();
        if (cursor!=null){
            cursor.moveToFirst();
            do {
                Log.d("do","now");
                VideoEntity video = new VideoEntity();
                video.setId(cursor.getString(0));
                video.setTitle(cursor.getString(1));
                video.setDuration(cursor.getLong(2));
                video.setData(cursor.getString(3));
                video.setMimeType(cursor.getString(4));
                videos.add(video);
            } while (cursor.moveToNext());
        }
        return videos;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==101){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                insertVideos();
            }
        }
    }
}