package krot.sample.com.meshchat;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Krot on 5/13/18.
 */

public class ImageUtils {

    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImg) throws IOException {

        //Uri co scheme la content
        if (selectedImg.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor cursor = context.getContentResolver().query(selectedImg, projection, null, null, null);

            if (cursor.moveToFirst()) {
                final int rotation = cursor.getInt(0);
                cursor.close();
                return rotateImage(img, rotation);
            }

            return img;
        }

        //khong phai scheme content
        else {
            ExifInterface exifInterface = new ExifInterface(selectedImg.getPath());
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.i("WTF", "orientation = " + orientation);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img , 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }


    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }
}
