package com.example.friends.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.friends.R;
import com.example.friends.entity.Friend;
import com.example.friends.viewmodel.AddFriendViewModel;
import com.google.android.material.button.MaterialButton;

public class AddFriendActivity extends AppCompatActivity {

    private TextView etName;
    private TextView etEmail;
    private ImageView imgFavourite;
    private MaterialButton btnSave;

    private AddFriendViewModel addFriendViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        initializeViews();
        initializeModel();
    }

    private void initializeViews()
    {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        imgFavourite = findViewById(R.id.imgFavourite);
        MaterialButton btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);

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

        etEmail.addTextChangedListener(new TextWatcher() {
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

        imgFavourite.setImageResource(R.drawable.favourite_false);
        imgFavourite.setTag(R.drawable.favourite_false);
        imgFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFavourite();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCancel();
            }
        });

        btnSave.setEnabled(false);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSave();
            }
        });
    }

    private void initializeModel()
    {
        addFriendViewModel = new ViewModelProvider(this).get(AddFriendViewModel.class);
        addFriendViewModel.initialize(getApplication());
    }

    private void checkEnableSave()
    {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if(!name.isEmpty() && !email.isEmpty())
        {
            btnSave.setEnabled(true);
        }
        else
        {
            btnSave.setEnabled(false);
        }
    }

    private void clickFavourite()
    {
        Integer tag = (Integer) imgFavourite.getTag();
        if(tag == R.drawable.favourite_false)
        {
            imgFavourite.setImageResource(R.drawable.favourite_true);
            imgFavourite.setTag(R.drawable.favourite_true);
        }
        else
        {
            imgFavourite.setImageResource(R.drawable.favourite_false);
            imgFavourite.setTag(R.drawable.favourite_false);
        }
    }

    private void clickCancel()
    {
        finish();
    }

    private void clickSave()
    {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        boolean favourite = getFavourite();

        Friend friend = new Friend(name, email, favourite);
        addFriendViewModel.insert(friend);
        finish();
    }

    private boolean getFavourite()
    {
        Integer tag = (Integer) imgFavourite.getTag();
        if(tag == R.drawable.favourite_true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
