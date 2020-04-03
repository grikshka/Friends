package com.example.friends.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
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

    public static final String EXTRA_FRIEND_ID = "EXTRA_FRIEND_ID";
    public static final String DATE_PATTERN = "dd/MM/yyyy";

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
        int friendId = getIntent().getIntExtra(EXTRA_FRIEND_ID, 0);

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
       Method invoked when options menu gets created.
       We want to set the menu for this activity here.
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friend_details, menu);
        return true;
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
            case R.id.action_edit:
            {
                startEditFriendActivity();
                return true;
            }
            case R.id.action_delete:
            {
                openDeleteFriendDialog();
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
                if(friend != null)
                {
                    updateProfileViews(friend);
                    updateActionViews(friend);
                    updateDataViews(friend);
                }
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
       Opening external browser does not need any permissions.

       Also since last Google Play update, Google removed the need to
       ask permission for the internet at all.
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

    private void startEditFriendActivity()
    {

    }

    private void openDeleteFriendDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("Are you sure you want to delete " +
                friendDetailsViewModel.getFriend().getValue().getName() +
                " from your Friends?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                friendDetailsViewModel.deleteFriend();
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        setDialogStyle(alert);
        alert.show();
    }

    /*
            Set dialog buttons style. Since we don't wanna customize the whole dialog,
            this is the simplest way of achieving small visual changes. This could anyways
            should be removed from here and we should create style for the dialog in res
            folder
         */
    private void setDialogStyle(final AlertDialog alert)
    {
        alert.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                        ContextCompat.getColor(FriendDetailsActivity.this, R.color.colorPrimary));
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(
                        ContextCompat.getColor(FriendDetailsActivity.this, R.color.colorPrimaryLight));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(FriendDetailsActivity.this, R.color.colorPrimary));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(
                        ContextCompat.getColor(FriendDetailsActivity.this, R.color.colorPrimaryLight));
            }
        });
    }

}
