package com.example.friends.view.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class LocationHelper {

    public static final String TAG = "LocationHelper";

    public static LatLng getLocationFromAddress(Context context, String strAddress)
    {
        return getLocationFromAddressGeocoder(context, strAddress);
    }

    /*
        Gets location from an address using Geocoder.

        This solution is not working "sometimes". This issue was reported to google 2 years ago
        and is still not fixed.
     */
    private static LatLng getLocationFromAddressGeocoder(Context context, String strAddress)
    {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null)
            {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        }
        catch (IOException ex)
        {
            Log.e(TAG, "Error occurred while getting getting location from address");
            ex.printStackTrace();
        }
        return p1;
    }
}
