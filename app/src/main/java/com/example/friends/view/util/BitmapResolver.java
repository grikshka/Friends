package com.example.friends.view.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

public class BitmapResolver {

    private final static String TAG = "BitmapResolver";

    /*
        Method used for returning bitmap from image Uri. It uses ImageDecoder for APIs 28
        and higher and legacy solutions for lower APIs that does not support this solution.
     */
    public static Bitmap getBitmap(@NonNull ContentResolver contentResolver, Uri imageUri) {
        if (imageUri == null)
        {
            Log.i(TAG, "Returning null because URI was null");
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        {
            return getBitmapImageDecoder(contentResolver, imageUri);
        }
        else
        {
            return getBitmapLegacy(contentResolver, imageUri);
        }
    }

    /*
        Returns Bitmap from image Uri using legacy solution
     */
    @SuppressWarnings("deprecation")
    private static Bitmap getBitmapLegacy(@NonNull ContentResolver contentResolver, @NonNull Uri imageUri){
        Bitmap bitmap = null;
        try
        {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
        }
        catch (IOException e)
        {
            Log.e(TAG, "Error occured while creating bitmap from URI using Legacy library");
        }

        return bitmap;
    }

    /*
        Returns Bitmap from image Uri using ImageDecoder
     */
    @TargetApi(Build.VERSION_CODES.P)
    private static Bitmap getBitmapImageDecoder(@NonNull ContentResolver contentResolver, @NonNull Uri imageUri){
        Bitmap bitmap = null;

        try
        {
            bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, imageUri));
        }
        catch (IOException e)
        {
            Log.e(TAG, "Error occured while creating bitmap from URI using ImageDecoder");
        }

        return bitmap;
    }
}
