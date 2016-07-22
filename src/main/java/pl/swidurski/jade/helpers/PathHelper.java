package pl.swidurski.jade.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Krystek on 2016-07-22.
 */
public class PathHelper {
    public static InputStream getStream(String path) throws FileNotFoundException {
        return new FileInputStream(getPath(path));
    }

    public static String getPath(String path) throws FileNotFoundException {
        return getFile(path).getAbsolutePath();
    }

    public static File getFile(String path) {
        File file = new File(path);
        if (!file.exists())
            file = new File(String.format("%s/%s", "src/main/resources", path));
        return file;
    }
}
