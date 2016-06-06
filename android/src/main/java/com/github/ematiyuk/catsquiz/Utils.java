package com.github.ematiyuk.catsquiz;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {

    public static Uri saveImage(Context context, Bitmap bitmap, String imageFileName) {
        long currentTime = System.currentTimeMillis();

        File fileDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        String mimeType = "image/jpeg";
        if (!(imageFileName.endsWith(".jpg") || imageFileName.endsWith(".jpeg")))
            imageFileName += ".jpg";

        int quality = 100;

        String filePath = fileDir + "/" + imageFileName;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);

            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);

            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }

        long size = new File(filePath).length();

        ContentValues values = new ContentValues(6);

        // store the details
        values.put(MediaStore.Images.Media.TITLE, imageFileName);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName);
        values.put(MediaStore.Images.Media.DATE_ADDED, currentTime);
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
        values.put(MediaStore.Images.Media.DATA, filePath);
        values.put(MediaStore.Images.Media.SIZE, size);

        return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}
