package app.microplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.microplayer.MainActivity;
import app.microplayer.R;
import app.microplayer.adapter.FileRecyclerAdapter;
import app.microplayer.database.VideoService;
import app.microplayer.model.Video;
import app.microplayer.util.Constants;

public class FileFragment extends Fragment {

    private VideoService videoService;
    private FileRecyclerAdapter fileRecyclerAdapter;
    private String path;

    public FileFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        videoService=new VideoService(((AppCompatActivity)context).getApplication());
        fileRecyclerAdapter=new FileRecyclerAdapter();
        videoService.getAllVideos().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                for (Video v:videos){
                    Log.d("video", v.getPath());
                }
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path=getArguments().getString(Constants.ARGS_FOLDER_PATH,null);
        Log.d("dir", path);
        videoService.getAllOf(path).observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                fileRecyclerAdapter.setVideoList(videos);
            }
        });
        Log.d("path",path);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).changeTitle("..."+path.substring(path.lastIndexOf("/")));
        RecyclerView recyclerView=view.findViewById(R.id.file_fragment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(fileRecyclerAdapter);
    }

}
