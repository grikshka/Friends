package com.example.friends.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.friends.R;
import com.example.friends.entity.Friend;
import com.example.friends.viewmodel.AddFriendViewModel;
import com.google.android.material.button.MaterialButton;

public class AddFriendActivity extends AppCompatActivity {

    private TextView etName;
    private TextView etPhone;
    private MaterialButton btnBirthday;
    private TextView etEmail;
    private TextView etWebsite;
    private ImageView imgFavorite;
    private MaterialButton btnSave;
    private MaterialButton btnCancel;

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
        etPhone = findViewById(R.id.etPhone);
        btnBirthday = findViewById(R.id.btnBirthday);
        etEmail = findViewById(R.id.etEmail);
        etWebsite = findViewById(R.id.etWebsite);

        imgFavorite = findViewById(R.id.imgFavorite);

        btnCancel = findViewById(R.id.btnCancel);
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

        imgFavorite.setImageResource(R.drawable.ic_favorite_false);
        imgFavorite.setTag(R.drawable.ic_favorite_false);
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFavorite();
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

        if(!name.isEmpty())
        {
            btnSave.setEnabled(true);
        }
        else
        {
            btnSave.setEnabled(false);
        }
    }

    private void clickFavorite()
    {
        Integer tag = (Integer) imgFavorite.getTag();
        if(tag == R.drawable.ic_favorite_false)
        {
            imgFavorite.setImageResource(R.drawable.ic_favorite_true);
            imgFavorite.setTag(R.drawable.ic_favorite_true);
        }
        else
        {
            imgFavorite.setImageResource(R.drawable.ic_favorite_false);
            imgFavorite.setTag(R.drawable.ic_favorite_false);
        }
    }

    private void clickCancel()
    {
        finish();
    }

    private void clickSave()
    {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        boolean favorite = getFavorite();

        Friend friend = new Friend(name, phone, email, favorite);
        addFriendViewModel.insert(friend);
        finish();
    }

    private boolean getFavorite()
    {
        Integer tag = (Integer) imgFavorite.getTag();
        if(tag == R.drawable.ic_favorite_true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
