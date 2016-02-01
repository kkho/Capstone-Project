package com.awesome.kkho.socialme.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.awesome.kkho.socialme.model.Event;
import com.awesome.kkho.socialme.model.Venue;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kkho on 11.01.2016.
 */
public class AddFavouriteItem {

    private static final String EVENTID = "EventID";
    private static final String VENUEID = "VenueId";

    public static void storeEvent(Context context, Event event) {
        SharedPreferences prefs = context.getSharedPreferences(EVENTID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> eventIds = prefs.getStringSet(EVENTID, new HashSet<String>());
        eventIds.add(new Gson().toJson(event));
        editor.putStringSet(EVENTID, eventIds);
        editor.commit();
    }

    public static ArrayList<Event> getEvents(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(EVENTID, Context.MODE_PRIVATE);


        ArrayList<Event> events = new ArrayList<Event>();
        Set<String> eventIds = prefs.getStringSet(EVENTID, new HashSet<String>());
        Gson gson = new Gson();
        for(String eventId : eventIds) {
            events.add(gson.fromJson(eventId, Event.class));
        }

        return events;
    }

    public static void deleteEvent(Context context, Event event) {
        SharedPreferences prefs = context.getSharedPreferences(EVENTID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> eventIds = prefs.getStringSet(EVENTID, new HashSet<String>());
        eventIds.remove(new Gson().toJson(event));
        editor.putStringSet(EVENTID, eventIds);
        editor.commit();
    }

    public static void storeVenue(Context context, Venue venue) {
        SharedPreferences prefs = context.getSharedPreferences(VENUEID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> venueIds = prefs.getStringSet(VENUEID, new HashSet<String>());
        venueIds.add(new Gson().toJson(venue));
        editor.putStringSet(VENUEID, venueIds);
        editor.commit();
    }

    public static ArrayList<Venue> getVenues(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(VENUEID, Context.MODE_PRIVATE);


        ArrayList<Venue> venues = new ArrayList<Venue>();
        Set<String> venueIds = prefs.getStringSet(VENUEID, new HashSet<String>());
        Gson gson = new Gson();
        for(String venueId : venueIds) {
            venues.add(gson.fromJson(venueId, Venue.class));
        }

        return venues;
    }

    public static void deleteVenue(Context context, Venue venue) {
        SharedPreferences prefs = context.getSharedPreferences(VENUEID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> venueIds = prefs.getStringSet(VENUEID, new HashSet<String>());
        venueIds.remove(new Gson().toJson(venue));
        editor.putStringSet(VENUEID, venueIds);
        editor.commit();
    }
}


