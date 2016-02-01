package com.awesome.kkho.socialme.util;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderApi;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by kkho on 24.01.2016.
 */
public class GeocoderUtil {

    public static String[] typeOfEvents = {"music", "comedy",
            "festivals_parades", "food", "art", "holiday", "attractions", "singles_social", "performing"};

    public static List<Address> GetAddressOfCurrentLocation(Context context, Intent intent) {
        Location currentLocation = (Location) intent.getExtras().get(Config.LOCATION);
        List<Address> addresses = null;
        if (currentLocation != null) {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            if (Geocoder.isPresent()) {
                try {
                    addresses = gcd.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return addresses;
    }

    public static List<Address> GetAddressOfLocation(Context context, Location location) {
        List<Address> addresses = null;
        if (location != null) {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            if (Geocoder.isPresent()) {
                try {
                    addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return addresses;
    }


    public static String createEventParameter() {
        String events = "";
        for(String typeEvent : typeOfEvents) {
            events += typeEvent + ",";
        }

        events = events.substring(0, events.length()-1);
        return events;
    }

}
