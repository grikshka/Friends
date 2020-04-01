package com.example.friends.view.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ImageFileHandler {

    private final static String TAG = "ImageFileHandler";

    /*
        Creates temporary image file, that is located inside private external storage.
    */
    public static File createTempImageFilePrivateStorage(Context context) throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    /*
        Method used for saving image from given path to private external storage.
     */
    public static String saveImageToPrivateStorage(Context context, Bitmap bitmap)
    {
        try
        {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

            File file = createTempImageFilePrivateStorage(context);
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();

            return file.getAbsolutePath();
        }
        catch(IOException e)
        {
            Log.e(TAG, "Error occurred while saving image to private storage");
            return null;
        }
    }
}
