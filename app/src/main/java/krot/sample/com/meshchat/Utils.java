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
import java.util.UUID;

/**
 * Created by Krot on 5/22/18.
 */

public class Utils {

//    public static String saveFile(byte[] data) {
//        Log.i("WTF", "saveFile: data = " + data + " /// length = " + data.length);
//        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mesh");
//        String fileName = UUID.randomUUID().toString();
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
//
//        File file = new File(dir, "test.mp4");
//
//
//
//        FileOutputStream outputStream;
//
//        try {
//            outputStream = new FileOutputStream(file);
//            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
//            bos.write(data);
//            bos.flush();
//            bos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Log.i("WTF", "filePath = " + file.getAbsolutePath());
//        return file.getAbsolutePath();
//
//    }
//
//
//    public static String convertBytesToFile(byte[] bytearray) {
//        File outputFile = null;
//        try {
//
//            outputFile = File.createTempFile("video", "mp4", HypeApplication.getAppContext().getCacheDir());
//            outputFile.deleteOnExit();
//            FileOutputStream fileoutputstream = new FileOutputStream(outputFile);
//            fileoutputstream.write(bytearray);
//            fileoutputstream.close();
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//        return outputFile.getAbsolutePath();
//    }


    public static String saveFile(byte[] data) {
        Log.i("WTF", "saveFile: data = " + data + " /// length = " + data.length);
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Hype");

        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir, "test.mp4");

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
