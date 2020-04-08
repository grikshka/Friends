package com.example.friends.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
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

        setUpActionBar(friend);
        loadMap(friend);
    }

    /*
        Method for setting action bar to our own customized toolbar
     */
    private void setUpActionBar(Friend friend)
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        setTitle(friend.getName());
    }

    /*
        Method used for loading and initializing a map.
     */
    private void loadMap(final Friend friend)
    {
        SupportMapFragment fragmentMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        fragmentMap.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                LatLng friendsLocation = LocationHelper.getLocationFromAddress(FriendMapActivity.this, friend.getAddress());
                addFriendMarker(friend, friendsLocation);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(friendsLocation, ZOOM_LEVEL));
            }

        });
    }

    /*
        Method for adding friends marker to the map
     */
    private void addFriendMarker(Friend friend, LatLng friendsLocation)
    {
        View markerView = initializeFriendMarkerView(friend);
        Bitmap markerBitmap = BitmapResolver.getBitmapFromView(markerView);

        MarkerOptions friendMarker = new MarkerOptions()
                .position(friendsLocation)
                .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap));

        map.addMarker(friendMarker);
    }

    /*
        Method responsible for initializing view for friends marker
     */
    private View initializeFriendMarkerView(Friend friend)
    {
        View markerView = getLayoutInflater().inflate(R.layout.friend_marker, null);
        CircleImageView markerImage = markerView.findViewById(R.id.imgPicture);
        if(friend.getPicturePath() == null || friend.getPicturePath().isEmpty())
        {
            markerImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.user_icon));
        }
        else
        {
            /*
                Since we are using Glide library in whole project for
                loading images, I wanted to keep consitency and use it here
                as well, but for some reason image is not being loaded sometimes
                when we change users picture (i think it has something to do with
                caching)
             */
//            Glide.with(this)
//                    .load(friend.getPicturePath())
//                    .into(markerImage);
            Bitmap myBitmap = BitmapFactory.decodeFile(friend.getPicturePath());
            markerImage.setImageBitmap(myBitmap);
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
