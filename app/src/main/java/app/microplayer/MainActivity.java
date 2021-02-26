package app.microplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app.microplayer.adapter.DirRecyclerAdapter;
import app.microplayer.util.FileType;

public class MainActivity extends AppCompatActivity {

    private List<File> videoList;
    private RecyclerView dirRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_main);

        dirRecyclerView=findViewById(R.id.main_dir_list_recycler_view);
        dirRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        new Thread(){
            @Override
            public void run() {
                long start=System.currentTimeMillis();
                scanFiles();
                long check=System.currentTimeMillis();
                Log.d("size-time", videoList.size()+"-"+String.valueOf(check-start));
                DirRecyclerAdapter dirRecyclerAdapter=new DirRecyclerAdapter(videoList);
                dirRecyclerView.setAdapter(dirRecyclerAdapter);
                long time=System.currentTimeMillis();
                Log.d("reorganize-time", String.valueOf(time-check));
            }

        }.start();
    }
    private void scanFiles(){
        videoList=new ArrayList<>();
        File externalDir=Environment.getExternalStorageDirectory();
        File sdDir=null;
        try {
            sdDir=new File(Objects.requireNonNull(System.getenv("SECONDARY_STORAGE")));
        }catch (NullPointerException e){
            e.printStackTrace();
            try {
                sdDir=new File(Objects.requireNonNull(System.getenv("EXTERNAL_STORAGE")));
            }catch (NullPointerException ne){
                ne.printStackTrace();
            }
        }

        if (externalDir!=null){
            findVideos(externalDir);
        }
        if (sdDir!=null){
            findVideos(sdDir);
        }
    }

    private void findVideos(File dir){
        for (File file:dir.listFiles()){
            if (file.isDirectory()){
                findVideos(file);
            }else if(FileType.isSupported(file)){
                videoList.add(file);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.main_menu_media_route_item){
            //cast
            return true;
        }else if (id==R.id.main_menu_sort_button){
            //todo:sort
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}