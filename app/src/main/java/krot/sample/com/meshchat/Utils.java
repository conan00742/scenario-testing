package krot.sample.com.meshchat;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Krot on 5/22/18.
 */

public class Utils {

    public static String saveFile(byte[] data) {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Hype");
        String fileName = UUID.randomUUID().toString();
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir, fileName + ".mp4");

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("WTF", "filePath = " + file.getAbsolutePath());
        return file.getAbsolutePath();

    }
}
