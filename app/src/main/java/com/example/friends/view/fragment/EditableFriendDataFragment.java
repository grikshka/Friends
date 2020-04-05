package com.example.friends.view.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.friends.R;
import com.example.friends.entity.Friend;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EditableFriendDataFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    public static final String TAG = "FriendDataFragment";

    /* Date pattern for displaying birthday */
    public static final String DATE_PATTERN = "dd/MM/yyyy";

    private EditText etName;
    private EditText etPhone;
    private MaterialButton btnBirthday;
    private EditText etEmail;
    private EditText etAddress;
    private EditText etWebsite;
    private ImageView imgFavorite;

    private Calendar calendar;
    private SimpleDateFormat dateFormatter;

    public EditableFriendDataFragment() {
        // Required empty public constructor
        initializeDateObjects();
    }

    private void initializeDateObjects()
    {
        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat(DATE_PATTERN);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editable_friend_data, container, false);
        initializeViews(view);
        initializeDefaultValues();
        initializeListeners();
        return view;
    }

    private void initializeViews(View view)
    {
        etName = view.findViewById(R.id.etName);
        etPhone = view.findViewById(R.id.etPhone);
        btnBirthday = view.findViewById(R.id.btnBirthday);
        etEmail = view.findViewById(R.id.etEmail);
        etAddress = view.findViewById(R.id.etAddress);
        etWebsite = view.findViewById(R.id.etWebsite);
        imgFavorite = view.findViewById(R.id.imgFavorite);
    }

    private void initializeDefaultValues()
    {
        btnBirthday.setText(dateFormatter.format(calendar.getTime()));
        imgFavorite.setImageResource(R.drawable.ic_favorite_false);
        imgFavorite.setTag(R.drawable.ic_favorite_false);
    }

    private void initializeListeners()
    {
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
    }

    public void setFriendData(Friend friend)
    {
        if(friend.getName() != null)
        {
            etName.setText(friend.getName());
        }
        if(friend.getPhone() != null)
        {
            etPhone.setText(friend.getPhone());
        }
        if(friend.getBirthday() != null)
        {
            btnBirthday.setText(dateFormatter.format(friend.getBirthday()));
        }
        if(friend.getEmail() != null)
        {
            etEmail.setText(friend.getEmail());
        }
        if(friend.getAddress() != null)
        {
            etAddress.setText(friend.getAddress());
        }
        if(friend.getWebsite() != null)
        {
            etWebsite.setText(friend.getWebsite());
        }
        setFavorite(friend.isFavorite());
    }

    /*
        Method invoked after users clicks on imgFavorite.
        Switches favorite state from favorite true to favorite false
        and the other way.
     */
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

    public EditText getNameEditText()
    {
        return etName;
    }

    public String getName()
    {
        return etName.getText().toString().trim();
    }

    public String getPhone()
    {
        return etPhone.getText().toString().trim();
    }

    public String getEmail()
    {
        return etEmail.getText().toString().trim();
    }

    public String getAddress()
    {
        return etAddress.getText().toString().trim();
    }

    public String getWebsite()
    {
        return etWebsite.getText().toString().trim();
    }

    /*
        Method used for parsing date from the btnBirthday and
        returning Date object
     */
    public Date getBirthday()
    {
        String birthdayAsString = btnBirthday.getText().toString();
        try
        {
            return birthdayAsString.isEmpty() ? null : dateFormatter.parse(birthdayAsString);
        }
        catch(ParseException e)
        {
            Log.e(TAG, "Error occurred while parsing the date");
            return null;
        }
    }

    /*
        Method used for parsing information from imgFavorite.
        Checks if current image indicate that friend is favorite or not
     */
    public boolean getFavorite()
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

    public void setFavorite(boolean favorite)
    {
        if(favorite)
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

    /*
        Method used for displaying DatePicker
     */
    private void showDatePickerDialog()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    /*
        Method used by DatePicker, invoked after user selected some date.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        btnBirthday.setText(dateFormatter.format(date));
    }
}
