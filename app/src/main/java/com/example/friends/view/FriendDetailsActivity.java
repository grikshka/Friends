package com.example.friends.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.friends.R;
import com.example.friends.entity.Friend;
import com.example.friends.viewmodel.FriendDetailsViewModel;

import java.text.SimpleDateFormat;

public class FriendDetailsActivity extends AppCompatActivity {

    public static final String DATE_PATTERN = "dd/MM/yyyy";

    /*
        We do not need these constants right now, since we are using
        external applications for performing these actions.

        Might be used later if we would like to implement our own funtionality
        for these actions and need to request permission for them
     */
//    public static final int REQUEST_CALL_PHONE = 1;
//    public static final int REQUEST_SEND_SMS = 2;
//    public static final int REQUEST_INTERNET = 3;

    private TextView tvName;
    private ImageView imgProfilePicture;
    private ImageView imgPhone;
    private ImageView imgMessage;
    private ImageView imgEmail;
    private ImageView imgWebsite;
    private TextView tvPhone;
    private TextView tvMessage;
    private TextView tvEmail;
    private TextView tvWebsite;
    private LinearLayout linFriendDataContainer;

    private FriendDetailsViewModel friendDetailsViewModel;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_PATTERN);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_details);
        int friendId = getIntent().getIntExtra("EXTRA_FRIEND_ID", 0);

        initializeViews();
        setUpActionBar();
        initializeViewModel(friendId);
    }

    private void initializeViews()
    {
        tvName = findViewById(R.id.tvName);
        imgProfilePicture = findViewById(R.id.imgProfilePicture);
        imgPhone = findViewById(R.id.imgPhone);
        imgMessage = findViewById(R.id.imgMessage);
        imgEmail = findViewById(R.id.imgEmail);
        imgWebsite = findViewById(R.id.imgWebsite);
        tvPhone = findViewById(R.id.tvPhone);
        tvMessage = findViewById(R.id.tvMessage);
        tvEmail = findViewById(R.id.tvEmail);
        tvWebsite = findViewById(R.id.tvWebsite);
        linFriendDataContainer = findViewById(R.id.linFriendDataContainer);
    }

    /*
        Method for setting action bar to our own customized toolbar
     */
    private void setUpActionBar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
    }

    /*
        Method invoked after user performed some action by clicking
        something from the action bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
            {
                finish();
                return true;
            }
            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    /*
        In the future this code should be replaced by using
        dagger dependency injection tool
     */
    private void initializeViewModel(int friendId)
    {
        friendDetailsViewModel = new ViewModelProvider(this).get(FriendDetailsViewModel.class);
        friendDetailsViewModel.initialize(getApplication(), friendId);
        friendDetailsViewModel.getFriend().observe(this, new Observer<Friend>() {
            @Override
            public void onChanged(Friend friend) {
                updateProfileViews(friend);
                updateActionViews(friend);
                updateDataViews(friend);
            }
        });
    }

    /*
        This method is used for updating friends profile picture and name
     */
    private void updateProfileViews(Friend friend)
    {
        tvName.setText(friend.getName());
        if(!(friend.getPicturePath() == null) && !friend.getPicturePath().isEmpty())
        {
            setProfilePicture(friend.getPicturePath());
        }
        else
        {
            imgProfilePicture.setImageResource(R.drawable.user_icon);
        }
    }

    /*
        Using Glide library for setting the profile picture to benefit
        from image caching and simplifying complex task of setting bitmap
        to achieve smooth behaviour and fast decoding
     */
    private void setProfilePicture(String pathToImage)
    {
        Glide.with(this)
                .load(pathToImage)
                .into(imgProfilePicture);
    }

    /*
        Method used for updating actions that are available for particular
        friend. This method is split into smaller method and checks for friends data
        and depending if friend contains data required for performing particular action,
        disables or enables this functionality.
     */
    private void updateActionViews(Friend friend)
    {
        updatePhoneMessageActionView(friend);
        updateEmailActionView(friend);
        updateWebsiteActionView(friend);
    }

    private void updatePhoneMessageActionView(Friend friend)
    {
        if(friend.getPhone() == null || friend.getPhone().isEmpty())
        {
            imgPhone.setImageResource(R.drawable.ic_phone_disabled);
            imgPhone.setOnClickListener(null);
            tvPhone.setTextColor(ContextCompat.getColor(this, R.color.colorDisabled));

            imgMessage.setImageResource(R.drawable.ic_message_disabled);
            imgMessage.setOnClickListener(null);
            tvMessage.setTextColor(ContextCompat.getColor(this, R.color.colorDisabled));
        }
        else
        {
            imgPhone.setImageResource(R.drawable.ic_phone_active);
            imgPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialActivity();
                }
            });
            tvPhone.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

            imgMessage.setImageResource(R.drawable.ic_message_active);
            imgMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSendMessageActivity();
                }
            });

            tvMessage.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    private void updateEmailActionView(Friend friend)
    {
        if(friend.getEmail() == null || friend.getEmail().isEmpty())
        {
            imgEmail.setImageResource(R.drawable.ic_email_disabled);
            imgEmail.setOnClickListener(null);
            tvEmail.setTextColor(ContextCompat.getColor(this, R.color.colorDisabled));
        }
        else
        {
            imgEmail.setImageResource(R.drawable.ic_email_active);
            imgEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSendMailActivity();
                }
            });
            tvEmail.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    private void updateWebsiteActionView(Friend friend)
    {
        if(friend.getWebsite() == null || friend.getWebsite().isEmpty())
        {
            imgWebsite.setImageResource(R.drawable.ic_website_disabled);
            imgWebsite.setOnClickListener(null);
            tvWebsite.setTextColor(ContextCompat.getColor(this, R.color.colorDisabled));
        }
        else
        {
            imgWebsite.setImageResource(R.drawable.ic_website_active);
            imgWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openBrowserActivity();
                }
            });
            tvWebsite.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    /*
        Method used for updating data of particular friend. It is split into smaller
        methods that checks if user contains some particular information and depending on
        the result, programmatically adds or removes view containing this information
     */
    private void updateDataViews(Friend friend)
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        updatePhoneDataView(friend, inflater);
        updateEmailDataView(friend, inflater);
        updateWebsiteDataView(friend, inflater);
        updateBirthdayDataView(friend, inflater);
    }

    private void updatePhoneDataView(Friend friend, LayoutInflater inflater)
    {
        RelativeLayout relPhoneDataContainer = findViewById(R.id.relPhoneDataContainer);
        if(!(friend.getPhone() == null)
                && !friend.getPhone().isEmpty()
                && relPhoneDataContainer == null)
        {
            inflater.inflate(
                    R.layout.friend_details_phone,
                    linFriendDataContainer,
                    true);

            TextView tvPhoneData = findViewById(R.id.tvPhoneData);
            tvPhoneData.setText(friend.getPhone());
        }
        else if((friend.getPhone() == null || friend.getPhone().isEmpty())
                && !(relPhoneDataContainer == null))
        {
            linFriendDataContainer.removeView(relPhoneDataContainer);
        }
    }

    private void updateEmailDataView(Friend friend, LayoutInflater inflater)
    {
        RelativeLayout relEmailDataContainer = findViewById(R.id.relEmailDataContainer);
        if(!(friend.getEmail() == null)
                && !friend.getEmail().isEmpty()
                && relEmailDataContainer == null)
        {
            inflater.inflate(
                    R.layout.friend_details_email,
                    linFriendDataContainer,
                    true);

            TextView tvEmailData = findViewById(R.id.tvEmailData);
            tvEmailData.setText(friend.getEmail());
        }
        else if((friend.getEmail() == null || friend.getEmail().isEmpty())
                && !(relEmailDataContainer == null))
        {
            linFriendDataContainer.removeView(relEmailDataContainer);
        }
    }

    private void updateWebsiteDataView(Friend friend, LayoutInflater inflater)
    {
        RelativeLayout relWebsiteDataContainer = findViewById(R.id.relWebsiteDataContainer);
        if(!(friend.getWebsite() == null)
                && !friend.getWebsite().isEmpty()
                && relWebsiteDataContainer == null)
        {
            inflater.inflate(
                    R.layout.friend_details_website,
                    linFriendDataContainer,
                    true);

            TextView tvWebsiteData = findViewById(R.id.tvWebsiteData);
            tvWebsiteData.setText(friend.getWebsite());
        }
        else if((friend.getWebsite() == null || friend.getWebsite().isEmpty())
                && !(relWebsiteDataContainer == null))
        {
            linFriendDataContainer.removeView(relWebsiteDataContainer);
        }
    }

    private void updateBirthdayDataView(Friend friend, LayoutInflater inflater)
    {
        RelativeLayout relBirthdayDataContainer = findViewById(R.id.relBirthdayDataContainer);
        if(!(friend.getBirthday() == null) && relBirthdayDataContainer == null)
        {
            inflater.inflate(
                    R.layout.friend_details_birthday,
                    linFriendDataContainer,
                    true);

            TextView tvBirthdayData = findViewById(R.id.tvBirthdayData);
            tvBirthdayData.setText(dateFormatter.format(friend.getBirthday()));
        }
        else if(friend.getBirthday() == null && !(relBirthdayDataContainer == null))
        {
            linFriendDataContainer.removeView(relBirthdayDataContainer);
        }
    }

    /*
        Starts activity for opening a dial and filling it with friends phone number.
     */
    private void openDialActivity()
    {
        String phoneNumber = friendDetailsViewModel.getFriend().getValue().getPhone();
        Uri uri = Uri.parse("tel:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);
    }

    /*
       Starts activity for sending a message to friends number.
    */
    private void openSendMessageActivity()
    {
        String phoneNumber = friendDetailsViewModel.getFriend().getValue().getPhone();
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(intent);
    }

    /*
       Starts activity for sending an mail to friends email address.
    */
    private void openSendMailActivity()
    {
        String emailAddress = friendDetailsViewModel.getFriend().getValue().getEmail();
        Uri uri = Uri.parse("mailto:" + emailAddress);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
    }

    /*
       Starts activity for opening a browser with friends website.
       Opening external browser does not need any permissions
    */
    private void openBrowserActivity()
    {
        String websiteAddress = friendDetailsViewModel.getFriend().getValue().getWebsite();
        if(websiteAddress.startsWith("www"))
        {
            websiteAddress = websiteAddress.substring(3);
            websiteAddress = "http://" + websiteAddress;
        }
        else if (!websiteAddress.startsWith("https://") && !websiteAddress.startsWith("http://")){
            websiteAddress = "http://" + websiteAddress;
        }
        Uri uri = Uri.parse(websiteAddress);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
    }

    /*
        Checks for required permission for making a call.
        If all of the permissions are granted, this method will make a call,
        if not, asks for required permissions.

        This method is not used right now, since we are not making call directly from our
        app. However I will leave it, if later we would like to do this.
     */
//    private void makeCallCheckPermission()
//    {
//        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
//        {
//            if(checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
//            {
//                //MAKE CALL
//            }
//            else
//            {
//                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
//            }
//        }
//    }


    /*
        Checks for required permission for sending a message. This permission
        is only required if we want to send a message from our application. It is
        not required for opening different app for sending a message
        If all of the permissions are granted, invokes the sendMessage method.
        If not, asks for required permissions.

        This method is not used right now, since we are not sending messages directly from our
        app. However I will leave it, if later we would like to do this.
     */
//    private void sendMessageCheckPermission()
//    {
//        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
//        {
//            if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
//            {
//                //SEND MESSAGE FROM THIS APP
//            }
//            else
//            {
//                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS);
//            }
//        }
//    }


    /*
        Checks for required permission for opening a website.
        If all of the permissions are granted, this method should start
        activity for opening website inside application, if not, asks for
        required permissions.

        This method is not used right now, since we are not opening a website directly in our
        app. However I will leave it, if later we would like to do this.
     */
//    private void openWebsiteCheckPermission()
//    {
//        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
//        {
//            if(checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED)
//            {
//                //OPEN WEBSITE IN THIS APP
//            }
//            else
//            {
//                requestPermissions(new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET);
//            }
//        }
//    }


    /*
        This method will never be invoked in current set up.
        Since we are using external applications for sending message,
        opening website, and opening dial we dont need any permissions.

        However I will leave this code if later on we would like to perform
        this actions from our applications instead of depending on other apps.
     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
//    {
//        switch(requestCode)
//        {
//            case REQUEST_CALL_PHONE:
//            {
//                if(grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                {
//                    //MAKE A PHONE CALL
//                }
//                break;
//            }
//            case REQUEST_SEND_SMS:
//            {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                {
//                    //SEND MESSAGE FROM APP
//                }
//                break;
//            }
//            case REQUEST_INTERNET:
//            {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                {
//                   //OPEN WEBSITE INSIDE APP
//                }
//                break;
//            }
//
//        }
//    }
}
