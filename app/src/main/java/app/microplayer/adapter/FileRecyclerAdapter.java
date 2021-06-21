package app.microplayer.adapter;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import app.microplayer.R;
import app.microplayer.model.Video;

public class FileRecyclerAdapter extends RecyclerView.Adapter {

    private List<Video> videoList;

    public FileRecyclerAdapter() {
        videoList=new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_file_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView title;
        private TextView duration;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon=itemView.findViewById(R.id.list_item_file_icon);
            title=itemView.findViewById(R.id.list_item_file_title);
            duration=itemView.findViewById(R.id.list_item_file_duration);
        }
        public void bind(int position){
            Video video=videoList.get(position);
            Glide.with(icon).load(video.getPath()).into(icon);
            title.setText(new File(video.getPath()).getName());

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(duration.getContext().getApplicationContext(), Uri.fromFile(new File(video.getPath())));
            long d = Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            duration.setText(String.valueOf(d));
        }
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
        notifyDataSetChanged();
        Log.d("size", String.valueOf(videoList.size()));
    }
}
