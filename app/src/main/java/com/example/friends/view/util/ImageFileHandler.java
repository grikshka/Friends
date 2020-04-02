package com.example.friends.view.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageFileHandler {

    private final static String TAG = "ImageFileHandler";

    /*
        Creates a file object, that is located inside private external storage.
    */
    public static File createImageFilePrivate(Context context) throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String suffix = ".jpg";
        File image = new File(
                storageDir + File.separator + imageFileName + suffix
        );
        return image;
    }

    /*
        Creates a file object that is located inside public external storage.
     */
    public static Uri createImageFilePublic(Context context) throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        ContentResolver resolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.TITLE, imageFileName);
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, imageFileName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
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

            File file = createImageFilePrivate(context);
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
