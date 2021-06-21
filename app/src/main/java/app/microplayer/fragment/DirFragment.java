package app.microplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app.microplayer.MainActivity;
import app.microplayer.R;
import app.microplayer.adapter.DirRecyclerAdapter;
import app.microplayer.database.VideoService;
import app.microplayer.model.Video;
import app.microplayer.util.Constants;
import app.microplayer.util.FileType;

public class DirFragment extends Fragment {
    private static final String TAG="DirFragment";

    private VideoService videoService;
    private RecyclerView recyclerView;
    private DirRecyclerAdapter dirRecyclerAdapter;
    private GridLayoutManager gridLayoutManager;
    private List<Video> videos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        videoService=new VideoService(((AppCompatActivity)context).getApplication());
        gridLayoutManager=new GridLayoutManager(context,3,RecyclerView.VERTICAL,false);
        dirRecyclerAdapter=new DirRecyclerAdapter(gridLayoutManager,new ArrayList<>());
        videoService.getAllVideos().observe(DirFragment.this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                dirRecyclerAdapter.setVideos(videos);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dir,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.main_dir_list_recycler_view);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(dirRecyclerAdapter);
        dirRecyclerAdapter.setOnItemClickListener(new DirRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(String path) {
                Log.d("folder_path",path);
                FileFragment fileFragment=new FileFragment();
                Bundle args=new Bundle();
                args.putString(Constants.ARGS_FOLDER_PATH,path);
                fileFragment.setArguments(args);
                ((MainActivity)getActivity()).switchPage(fileFragment);
            }
        });
        ((MainActivity)getActivity()).changeTitle(getString(R.string.app_name));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.dir_fragment_menu_sort_button){
            //todo:sort
            if (gridLayoutManager!=null){
                if (gridLayoutManager.getSpanCount()==1){
                    gridLayoutManager.setSpanCount(3);
                }else {
                    gridLayoutManager.setSpanCount(1);
                }
                dirRecyclerAdapter.notifyItemRangeChanged(0,dirRecyclerAdapter.getItemCount());
            }
            return true;
        }else if (id==R.id.dir_fragment_menu_refresh_button){
            new Thread(){
                @Override
                public void run() {
                    scanFiles();
                    videoService.addVideoBatch(videos, new VideoService.JobListener() {
                        @Override
                        public void onFinish() {
                            Log.d(TAG,"search finished.");
                        }

                        @Override
                        public void onFail(Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }.start();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void scanFiles(){
        videos=new ArrayList<>();
        File externalDir= Environment.getExternalStorageDirectory();
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
    private void findVideos(@NonNull File dir){
        final File[] dirList=dir.listFiles();
        if (dirList!=null){
            for (File file:dirList){
                if (file.isDirectory()){
                    findVideos(file);
                }else if(FileType.isSupported(file)){
                    Video video=new Video(file.getAbsolutePath());
                    videos.add(video);
                }
            }
        }
    }
}
