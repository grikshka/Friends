package com.example.friends.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.friends.R;
import com.example.friends.entity.Friend;
import com.example.friends.viewmodel.AddFriendViewModel;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddFriendActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public final static String DATE_PATTERN = "dd/MM/yyyy";

    private TextView etName;
    private TextView etPhone;
    private MaterialButton btnBirthday;
    private TextView etEmail;
    private TextView etWebsite;
    private ImageView imgFavorite;
    private MaterialButton btnSave;

    private AddFriendViewModel addFriendViewModel;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        initializeViewModel();
        initializeDateObjects();
        initializeViews();
        setUpActionBar();
        initializeDefaultValues();
        initalizeListeners();
    }

    private void initializeDateObjects()
    {
        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat(DATE_PATTERN);
    }

    private void initializeViews()
    {
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        btnBirthday = findViewById(R.id.btnBirthday);
        etEmail = findViewById(R.id.etEmail);
        etWebsite = findViewById(R.id.etWebsite);
        imgFavorite = findViewById(R.id.imgFavorite);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setUpActionBar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
    }

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

    private void initializeDefaultValues()
    {
        btnBirthday.setText(dateFormatter.format(calendar.getTime()));
        imgFavorite.setImageResource(R.drawable.ic_favorite_false);
        imgFavorite.setTag(R.drawable.ic_favorite_false);
        btnSave.setEnabled(false);
    }

    private void initalizeListeners()
    {
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
        btnBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFavorite();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSave();
            }
        });
    }

    private void initializeViewModel()
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
        Date birthday = getBirthday();
        String email = etEmail.getText().toString().trim();
        String website = etWebsite.getText().toString().trim();
        boolean favorite = getFavorite();

        Friend friend = new Friend(name, phone, birthday, email, website, favorite);
        addFriendViewModel.insertFriend(friend);
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

    private Date getBirthday()
    {
        String birthdayAsString = btnBirthday.getText().toString();
        try
        {
            return birthdayAsString.isEmpty() ? null : dateFormatter.parse(birthdayAsString);
        }
        catch(ParseException e)
        {
            return null;
        }
    }

    private void showDatePickerDialog()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        btnBirthday.setText(dateFormatter.format(date));
    }
}
