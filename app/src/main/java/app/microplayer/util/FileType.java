package app.microplayer.util;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileType {
    private static List<String> extensionList=List.of("mp4","m4a","fmp4","webm","mp3","ogg","wav","ts","ps","flv","aac","amr","mkv");

    public static boolean isSupported(File file){
        String path=file.getPath();
        if (path.contains(".")){
            String extension=path.substring(path.lastIndexOf(".")+1);
            if (extensionList.contains(extension.toLowerCase())){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
