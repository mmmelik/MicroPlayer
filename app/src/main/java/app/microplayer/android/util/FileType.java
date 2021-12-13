package app.microplayer.android.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileType {

    private static final List<String> extensionList= Arrays.asList("mp4", "m4a", "fmp4", "webm", "mp3", "ogg", "wav", "ts", "ps", "flv", "aac", "amr", "mkv");

    public static boolean isSupported(String path){
        if (path.contains(".")){
            String extension=path.substring(path.lastIndexOf(".")+1);
            if (extensionList.contains(extension.toLowerCase())){
                return true;
            }else {
                //TODO: Check without extension. Or hard-check the leading bytes.
                return false;
            }
        }else {
            return false;
        }
    }
}
