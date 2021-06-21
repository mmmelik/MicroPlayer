package app.microplayer.adapter;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.util.VLCUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import app.microplayer.R;
import app.microplayer.model.Video;

public class DirRecyclerAdapter extends RecyclerView.Adapter<DirRecyclerAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;
    private static final int TYPE_LIST=1;
    private static final int TYPE_GRID=2;
    private LinkedHashMap<File,List<Video>> videoMap;
    private GridLayoutManager gridLayoutManager;

    public DirRecyclerAdapter(GridLayoutManager gridLayoutManager, List<Video> videoList) {
        this.gridLayoutManager=gridLayoutManager;
        videoMap=prepareVideoMap(videoList);

    }

    private LinkedHashMap<File, List<Video>> prepareVideoMap(List<Video> videoList) {
        videoMap=new LinkedHashMap<>();
        for (Video video:videoList){
            File file=new File(video.getPath());
            List<Video> vl=videoMap.get(file.getParentFile());
            if (vl==null){
                vl=new ArrayList<>();
            }
            vl.add(video);
            videoMap.put(file.getParentFile(),vl);
        }
        return videoMap;
    }

    @Override
    public int getItemViewType(int position) {
        if (gridLayoutManager.getSpanCount()==1){
            return TYPE_LIST;
        }else {
            return TYPE_GRID;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==TYPE_LIST){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dir_list,parent,false);
        }else if (viewType==TYPE_GRID){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dir_grid,parent,false);
        }else {
            return null;
        }
        return new ViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return videoMap.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View container;
        TextView name;
        TextView count;
        ImageView image;
        ImageView image1;
        ImageView image2;
        ImageView image3;

        public ViewHolder(@NonNull View itemView,int viewType) {
            super(itemView);
            if (viewType==TYPE_GRID){
                container=itemView.findViewById(R.id.list_item_dir_grid_container);
                name=itemView.findViewById(R.id.list_item_dir_grid_name);
                count=itemView.findViewById(R.id.list_item_dir_grid_count);
                image1=itemView.findViewById(R.id.collection_image_1);
                image2=itemView.findViewById(R.id.collection_image_2);
                image3=itemView.findViewById(R.id.collection_image_3);
            }else {
                container=itemView.findViewById(R.id.list_item_dir_container);
                name=itemView.findViewById(R.id.list_item_dir_name);
                count=itemView.findViewById(R.id.list_item_dir_count);
                image=itemView.findViewById(R.id.list_item_dir_image);
            }
        }

        public void bind(int position){
            File key=new ArrayList<>(videoMap.keySet()).get(position);
            List<Video> videos=videoMap.get(key);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(key.getAbsolutePath());
                }
            });

            name.setText(key.getName());

            if (getItemViewType()==TYPE_GRID){
                if (videos.size()>=1) {
                    Glide.with(image1).load(videos.get(videos.size() - 1).getPath()).listener(requestListener).into(image1);
                    if (videos.size()>=2){
                        Glide.with(image2).load(videos.get(videos.size()-2).getPath()).listener(requestListener).into(image2);
                        if (videos.size()>=3){
                            Glide.with(image2).load(videos.get(videos.size()-3).getPath()).listener(requestListener).into(image2);
                        }
                    }
                }

                count.setText("("+videos.size()+")");
            }else {
                if (videos.size()>=1){
                    Glide.with(image).load(videos.get(videos.size()-1).getPath()).into(image);
                }
                if (videos.size()>1){
                    count.setText(videos.size()+count.getContext().getResources().getString(R.string.items));
                }else {
                    count.setText(videos.size()+count.getContext().getResources().getString(R.string.item));
                }
            }
        }

        private RequestListener<Drawable> requestListener=new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (e!=null){
                    //e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //Log.d("width", String.valueOf(resource.getMinimumWidth()));
                //Log.d("height", String.valueOf(resource.getMinimumHeight()));
                return false;
            }
        };
    }

    public void setVideos(List<Video> videoList){
        List<Video> filteredList=new ArrayList<>();
        for (Video video:videoList){
            if (!video.isHidden()){
                filteredList.add(video);
            }
        }
        videoMap=prepareVideoMap(filteredList);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onClick(String path);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
