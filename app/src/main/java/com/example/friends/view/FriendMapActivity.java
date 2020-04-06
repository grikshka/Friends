package com.example.friends.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.friends.R;
import com.example.friends.entity.Friend;
import com.example.friends.view.util.BitmapResolver;
import com.example.friends.view.util.LocationHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendMapActivity extends AppCompatActivity {

    public static final String TAG = "FriendsMapActivity";
    public static final String EXTRA_FRIEND = "EXTRA_FRIEND";
    public static final int ZOOM_LEVEL = 11;

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_map);
        Friend friend = (Friend) getIntent().getSerializableExtra(EXTRA_FRIEND);

        setUpActionBar();
        loadMap(friend);
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
        setTitle("Friend Details");
    }

    private void loadMap(final Friend friend)
    {
        SupportMapFragment fragmentMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        fragmentMap.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                LatLng friendsLocation = initializeFriendMarker(friend);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(friendsLocation, ZOOM_LEVEL));
            }

        });
    }

    private LatLng initializeFriendMarker(Friend friend)
    {
        LatLng friendsPosition = LocationHelper.getLocationFromAddress(this, friend.getAddress());
        View markerView = initializeMarkerView(friend);

        Bitmap markerBitmap = BitmapResolver.getBitmapFromView(markerView);

        MarkerOptions friendMarker = new MarkerOptions()
                .position(friendsPosition)
                .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap));

        map.addMarker(friendMarker);
        return friendsPosition;
    }

    private View initializeMarkerView(Friend friend)
    {
        View markerView = getLayoutInflater().inflate(R.layout.friend_marker, null);
        CircleImageView markerImage = markerView.findViewById(R.id.imgPicture);
        if(friend.getPicturePath() == null || friend.getPicturePath().isEmpty())
        {
            markerImage.setImageDrawable(getResources().getDrawable(R.drawable.user_icon));
        }
        else
        {
            Glide.with(this).load(friend.getPicturePath()).into(markerImage);
        }
        return markerView;
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

}
