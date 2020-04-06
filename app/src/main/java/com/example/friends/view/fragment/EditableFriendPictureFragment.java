package com.example.friends.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.friends.R;
import com.example.friends.view.util.BitmapResolver;
import com.example.friends.view.util.ImageFileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditableFriendPictureFragment extends Fragment {

    public static final String TAG = "FriendPictureFragment";

    /*
        Since getting permissions is strictly connected to performing
        an action that requires those permissions I am using same
        result code for checking permission and starting activity
        connected to those permissions
    */
    public static final int CAMERA_REQUEST = 1;
    public static final int GALLERY_REQUEST = 2;

    /*
        Menu items for dialog box with possible options of
        setting friends profile picture
    */
    public static final String TAKE_PHOTO_MENU_ITEM = "Take Photo";
    public static final String CHOOSE_PHOTO_MENU_ITEM = "Choose from Library";
    public static final String CANCEL_MENU_ITEM = "Cancel";

    private ImageView imgProfilePicture;

    /*
        Path to currently visible profile picture,
        either selected from gallery or taken using
        camera. Used to send along with the other
        friends data on save.
     */
    private String profilePicturePath;

    /*
        Uri to image file that is used by camera to save
        image taken by the user. It is used in onActivityResult
        to set ImageView and add image to public external storage
     */
    private Uri cameraImageUri;

    public EditableFriendPictureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editable_friend_picture, container, false);
        initializeViews(view);
        initializeListeners();
        return view;
    }

    private void initializeViews(View view)
    {
        imgProfilePicture = view.findViewById(R.id.imgProfilePicture);
    }

    private void initializeListeners()
    {
        imgProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectPictureDialog();
            }
        });
    }

    /*
        Opens dialog for letting user select which option he wants to use
        to set friends profile picture.
     */
    private void openSelectPictureDialog()
    {
        final CharSequence[] items = initializeMenuItems();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Profile Picture");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(TAKE_PHOTO_MENU_ITEM))
                {
                    takePhotoCheckPermission();
                }
                else if (items[item].equals(CHOOSE_PHOTO_MENU_ITEM))
                {
                    choosePhotoCheckPermission();
                }
                else if (items[item].equals(CANCEL_MENU_ITEM))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /*
        This method is needed if user does not have a camera in his
        device. Then we should not show Take Photo option in a dialog
        box
    */
    private CharSequence[] initializeMenuItems()
    {
        if(getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
        {
            return new CharSequence[] {TAKE_PHOTO_MENU_ITEM, CHOOSE_PHOTO_MENU_ITEM, CANCEL_MENU_ITEM};
        }
        else
        {
            return new CharSequence[] {CHOOSE_PHOTO_MENU_ITEM, CANCEL_MENU_ITEM};
        }
    }

    /*
        Checks for required permission for taking photo using camera.
        If all of the permissions are granted, invokes the takePhoto method.
        If not, asks for required permissions.

        Writing permission is not required anymore for writing and reading private
        external storage since API 18. However after taking a photo and saving it
        to private external storage by camera, we will also copy image and save it
        to public external storage to make it visible for user in gallery. This will
        be done just for better UX, since we will still use image from private storage
        across application for better protection over deleting images from gallery.
     */
    private void takePhotoCheckPermission()
    {
        List<String> permissions = new ArrayList<String>();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            if(getContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                permissions.add(Manifest.permission.CAMERA);
            }
            if(getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if(permissions.size() > 0)
            {
                requestPermissions(permissions.toArray(new String[permissions.size()]), CAMERA_REQUEST);
            }
            else
            {
                takePhoto();
            }
        }
    }

    /*
        Starts activity for taking picture using camera.

        Uses ImageFileHandler to create image file object in public external storage
        area that will be passed to camera intent to save photo to the specified file.
     */
    private void takePhoto()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getContext().getPackageManager()) != null)
        {
            Uri publicImageUri = null;
            try
            {
                publicImageUri = ImageFileHandler.createImageFilePublic(getContext());
            }
            catch (IOException ex)
            {
                Log.e(TAG, "Error occurred while creating the File");
            }
            if (publicImageUri != null) {
                cameraImageUri = publicImageUri;
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        }

    }

    /*
        Checks for required permission for selecting photo from gallery.
        If all of the permissions are granted, invokes the choosePhoto method.
        If not, asks for required permissions.
     */
    private void choosePhotoCheckPermission()
    {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            if(getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                choosePhoto();
            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST);
            }
        }
    }

    /*
        Starts activity responsible for selecting image from the gallery
     */
    private void choosePhoto()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    /*
        Using Glide library for setting the profile picture from image path
         to benefit from image caching and simplifying complex task of setting
         bitmap to achieve smooth behaviour and fast decoding
     */
    public void setProfilePicture(String pathToImage)
    {
        if(pathToImage != null && !pathToImage.isEmpty())
        {
            profilePicturePath = pathToImage;
            Glide.with(this)
                    .load(pathToImage)
                    .into(imgProfilePicture);
        }
    }

    public String getProfilePicturePath()
    {
        return profilePicturePath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch(requestCode)
        {
            case CAMERA_REQUEST:
            {
                if(grantResults.length > 0)
                {
                    for(int i = 0; i < grantResults.length; i++)
                    {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        {
                            return;
                        }
                    }
                    takePhoto();
                }
                break;
            }
            case GALLERY_REQUEST:
            {
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    choosePhoto();
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (resultCode == Activity.RESULT_OK) {
                    /*
                        Here we are getting the cameraImagePath which camera intent
                        used to save photo taken by user into public storage.

                        In case user will delete image from gallery, we want to
                        copy it to private external storage and use this copy in
                        app to avoid this problem
                     */
                    Uri imageUri = cameraImageUri;
                    Bitmap bitmap = BitmapResolver.getBitmapFromUri(getContext().getContentResolver(), imageUri);
                    String pathToImage = ImageFileHandler.saveImageToPrivateStorage(getContext(), bitmap);
                    setProfilePicture(pathToImage);
                    break;
                }
            }
            case GALLERY_REQUEST: {
                if (resultCode == Activity.RESULT_OK) {
                    /*
                        In case user will delete image from gallery, we want to
                        copy it to private external storage and use this copy in
                        app to avoid this problem
                     */
                    Uri imageUri = data.getData();
                    Bitmap bitmap = BitmapResolver.getBitmapFromUri(getContext().getContentResolver(), imageUri);
                    String pathToImage = ImageFileHandler.saveImageToPrivateStorage(getContext(), bitmap);
                    setProfilePicture(pathToImage);
                    break;
                }
            }
        }
    }
}
