package com.example.friends.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.friends.R;
import com.example.friends.entity.Friend;
import com.example.friends.view.fragment.EditableFriendDataFragment;
import com.example.friends.view.fragment.EditableFriendPictureFragment;
import com.example.friends.viewmodel.EditFriendViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.Date;

public class EditFriendActivity extends AppCompatActivity {

    public static final String TAG = "EditFriendActivity";
    public static final String EXTRA_EDITED_FRIEND = "EXTRA_EDITED_FRIEND";

    private EditableFriendPictureFragment fragmentPicture;
    private EditableFriendDataFragment fragmentData;

    private MaterialButton btnSave;

    private EditFriendViewModel editFriendViewModel;
    private Friend editedFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editedFriend = (Friend) getIntent().getSerializableExtra(EXTRA_EDITED_FRIEND);

        setContentView(R.layout.activity_editable_friend);
        initializeViews();
        initializeViewModel();
        setUpActionBar();
        initalizeListeners();
    }

    private void initializeViews()
    {
        fragmentPicture = (EditableFriendPictureFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentProfilePicture);
        fragmentPicture.setProfilePicture(editedFriend.getPicturePath());

        fragmentData = (EditableFriendDataFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentData);
        fragmentData.setFriendData(editedFriend);

        btnSave = findViewById(R.id.btnSave);
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
        actionBar.setHomeAsUpIndicator(R.drawable.ic_cancel);
        setTitle("Cancel");
    }

    /*
        In the future this code should be replaced by using
        dagger dependency injection tool
     */
    private void initializeViewModel()
    {
        editFriendViewModel = new ViewModelProvider(this).get(EditFriendViewModel.class);
        editFriendViewModel.initialize(getApplication());
    }

    private void initalizeListeners()
    {
        EditText etName = fragmentData.getNameEditText();
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkEnableSave();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSave();
            }
        });
    }

    /*
        Checks if user entered all of the required data for creating a friend
        and if yes - enables the save button
     */
    private void checkEnableSave()
    {
        String name = fragmentData.getName();

        if(!name.isEmpty())
        {
            btnSave.setEnabled(true);
        }
        else
        {
            btnSave.setEnabled(false);
        }
    }

    /*
        Method for saving friend. Gets all data from views and
        path to image from instance variable and sends this data
        to AddFriendModel.
     */
    private void clickSave()
    {
        editedFriend.setName(fragmentData.getName());
        editedFriend.setPhone(fragmentData.getPhone());
        editedFriend.setBirthday(fragmentData.getBirthday());
        editedFriend.setEmail(fragmentData.getEmail());
        editedFriend.setAddress(fragmentData.getAddress());
        editedFriend.setWebsite(fragmentData.getWebsite());
        editedFriend.setPicturePath(fragmentPicture.getProfilePicturePath());
        editedFriend.setFavorite(fragmentData.getFavorite());

        editFriendViewModel.updateFriend(editedFriend);
        finish();
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
        Note - we do not have to save other values, since simple elements
        like EditView or TextView are automatically restored if you give them
        an id.
    */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("picturePath", fragmentPicture.getProfilePicturePath());
        outState.putBoolean("isFavorite", fragmentData.getFavorite());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String picturePath = outState.getString("picturePath");
        boolean isFavorite = outState.getBoolean("isFavorite");
        fragmentPicture.setProfilePicture(picturePath);
        fragmentData.setFavorite(isFavorite);
    }
}
