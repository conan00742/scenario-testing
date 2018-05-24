package krot.sample.com.meshchat;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Krot on 5/22/18.
 */

public class Utils {


    public static String saveFile(byte[] data) {
        Log.i("WTF", "saveFile: data = " + data + " /// length = " + data.length);
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Hype");
        String fileName = "video" + new Random().nextInt(80 - 1) + 1 + ".mp4";

        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir, fileName);

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

        Log.i("WTF", "path = " + file.getAbsolutePath());
        return file.getAbsolutePath();

    }
}
