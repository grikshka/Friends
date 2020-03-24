package com.example.friends.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.friends.R;
import com.example.friends.entity.Friend;
import com.example.friends.viewmodel.FriendDetailsViewModel;

import java.text.SimpleDateFormat;

public class FriendDetailsActivity extends AppCompatActivity {

    public final static String DATE_PATTERN = "dd/MM/yyyy";

    private TextView tvName;
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
        initializeViewModel(friendId);
    }

    private void initializeViews()
    {
        tvName = findViewById(R.id.tvName);
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

    private void updateProfileViews(Friend friend)
    {
        tvName.setText(friend.getName());
    }

    private void updateActionViews(Friend friend)
    {
        updatePhoneMessageActionView(friend);
        updateEmailActionView(friend);
        updateWebsiteActionView(friend);
    }

    private void updatePhoneMessageActionView(Friend friend)
    {
        if(friend.getPhone().isEmpty())
        {
            imgPhone.setImageResource(R.drawable.ic_phone_disabled);
            tvPhone.setTextColor(getResources().getColor(R.color.colorDisabled));
            imgMessage.setImageResource(R.drawable.ic_message_disabled);
            tvMessage.setTextColor(getResources().getColor(R.color.colorDisabled));
        }
        else
        {
            imgPhone.setImageResource(R.drawable.ic_phone_active);
            tvPhone.setTextColor(getResources().getColor(R.color.colorPrimary));
            imgMessage.setImageResource(R.drawable.ic_message_active);
            tvMessage.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void updateEmailActionView(Friend friend)
    {
        if(friend.getEmail().isEmpty())
        {
            imgEmail.setImageResource(R.drawable.ic_email_disabled);
            tvEmail.setTextColor(getResources().getColor(R.color.colorDisabled));
        }
        else
        {
            imgEmail.setImageResource(R.drawable.ic_email_active);
            tvEmail.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void updateWebsiteActionView(Friend friend)
    {
        if(friend.getWebsite().isEmpty())
        {
            imgWebsite.setImageResource(R.drawable.ic_website_disabled);
            tvWebsite.setTextColor(getResources().getColor(R.color.colorDisabled));
        }
        else
        {
            imgWebsite.setImageResource(R.drawable.ic_website_active);
            tvWebsite.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

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
        if(!friend.getPhone().isEmpty() && relPhoneDataContainer == null)
        {
            inflater.inflate(
                    R.layout.friend_details_phone,
                    linFriendDataContainer,
                    true);
            TextView tvPhoneData = findViewById(R.id.tvPhoneData);
            tvPhoneData.setText(friend.getPhone());
        }
        else if(friend.getPhone().isEmpty() && !(relPhoneDataContainer == null))
        {
            linFriendDataContainer.removeView(relPhoneDataContainer);
        }
    }

    private void updateEmailDataView(Friend friend, LayoutInflater inflater)
    {
        RelativeLayout relEmailDataContainer = findViewById(R.id.relEmailDataContainer);
        if(!friend.getEmail().isEmpty() && relEmailDataContainer == null)
        {
            inflater.inflate(
                    R.layout.friend_details_email,
                    linFriendDataContainer,
                    true);
            TextView tvEmailData = findViewById(R.id.tvEmailData);
            tvEmailData.setText(friend.getEmail());
        }
        else if(friend.getEmail().isEmpty() && !(relEmailDataContainer == null))
        {
            linFriendDataContainer.removeView(relEmailDataContainer);
        }
    }

    private void updateWebsiteDataView(Friend friend, LayoutInflater inflater)
    {
        RelativeLayout relWebsiteDataContainer = findViewById(R.id.relWebsiteDataContainer);
        if(!friend.getWebsite().isEmpty() && relWebsiteDataContainer == null)
        {
            inflater.inflate(
                    R.layout.friend_details_website,
                    linFriendDataContainer,
                    true);
            TextView tvWebsiteData = findViewById(R.id.tvWebsiteData);
            tvWebsiteData.setText(friend.getWebsite());
        }
        else if(friend.getWebsite().isEmpty() && !(relWebsiteDataContainer == null))
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
}