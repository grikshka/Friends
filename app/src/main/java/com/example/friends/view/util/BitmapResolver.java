package com.example.friends.view.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import java.io.IOException;

public class BitmapResolver {

    private final static String TAG = "BitmapResolver";

    /*
        Method used for returning bitmap from image Uri. It uses ImageDecoder for APIs 28
        and higher and legacy solutions for lower APIs that does not support ImageDecoder.
     */
    public static Bitmap getBitmapFromUri(@NonNull ContentResolver contentResolver, Uri imageUri) {
        if (imageUri == null)
        {
            Log.i(TAG, "Returning null because URI was null");
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        {
            return getBitmapFromUriImageDecoder(contentResolver, imageUri);
        }
        else
        {
            return getBitmapFromUriLegacy(contentResolver, imageUri);
        }
    }

    /*
        Returns Bitmap from image Uri using legacy solution
     */
    @SuppressWarnings("deprecation")
    private static Bitmap getBitmapFromUriLegacy(@NonNull ContentResolver contentResolver, @NonNull Uri imageUri){
        Bitmap bitmap = null;
        try
        {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
        }
        catch (IOException e)
        {
            Log.e(TAG, "Error occurred while creating bitmap from URI using Legacy library");
        }

        return bitmap;
    }

    /*
        Returns Bitmap from image Uri using ImageDecoder
     */
    @TargetApi(Build.VERSION_CODES.P)
    private static Bitmap getBitmapFromUriImageDecoder(@NonNull ContentResolver contentResolver, @NonNull Uri imageUri){
        Bitmap bitmap = null;

        try
        {
            bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, imageUri));
        }
        catch (IOException e)
        {
            Log.e(TAG, "Error occurred while creating bitmap from URI using ImageDecoder");
        }
        return bitmap;
    }

    /*
        Returns Bitmap from View
     */
    public static Bitmap getBitmapFromView(View view)
    {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        view.draw(canvas);
        return returnedBitmap;
    }
}
