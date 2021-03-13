package app.microplayer.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Video {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String path;

    private String thumbPath;

    private boolean hidden;

    public Video() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
