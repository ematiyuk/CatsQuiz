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

    public static Uri saveImage(Context context, Bitmap bitmap, String imageFileName,
                                String subFolderPath, String fileDescription) {
        long currentTime = System.currentTimeMillis();

        File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Pictures/" + subFolderPath);
        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                return null;
            }
        }

        String mimeType = "image/jpeg";
        if (!(imageFileName.endsWith(".jpg") || imageFileName.endsWith(".jpeg")))
            imageFileName += ".jpg";

        int quality = 100;

        String filePath = fileDir.getAbsolutePath() + "/" + imageFileName;
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

        ContentValues values = new ContentValues(7);

        // store the details
        values.put(MediaStore.Images.Media.TITLE, imageFileName);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName);
        values.put(MediaStore.Images.Media.DATE_ADDED, currentTime);
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
        values.put(MediaStore.Images.Media.DESCRIPTION, fileDescription);
        values.put(MediaStore.Images.Media.DATA, filePath);
        values.put(MediaStore.Images.Media.SIZE, size);

        return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}
