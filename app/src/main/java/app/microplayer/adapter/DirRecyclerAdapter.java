package app.microplayer.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirRecyclerAdapter extends RecyclerView.Adapter {

    private HashMap<File,List<File>> videoMap;

    public DirRecyclerAdapter(List<File> videoList) {
        videoMap=new HashMap<>();
        for (File file:videoList){
            List<File> vl=videoMap.get(file.getParentFile());
            if (vl==null){
                vl=new ArrayList<>();
            }
            vl.add(file);
            videoMap.put(file.getParentFile(),vl);
        }

        for (Map.Entry<File,List<File>> entry:videoMap.entrySet()){
            Log.d(entry.getKey().getName(), String.valueOf(entry.getValue().size()));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return videoMap.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(int position){

        }
    }
}
