package com.awesome.kkho.socialme.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.awesome.kkho.socialme.model.FacebookUser;
import com.google.gson.Gson;

/**
 * Created by kkho on 28.01.2016.
 */
public class SharedPrefUtil {
    public static final String FACEBOOK_USER = "facebook_user";
    public static final String CURRENT_TOTAL_PAGE_EVENT = "current_total_page_event";
    public static final String CURRENT_TOTAL_PAGE_VENUE = "current_total_page_venue";

    public static FacebookUser getUserProfile(Context context) {
        Gson gson = new Gson();
        SharedPreferences preferences = context.getSharedPreferences(FACEBOOK_USER, Context.MODE_PRIVATE);
        if(preferences.getString(FACEBOOK_USER, null) == null) {
            return null;
        }

        return gson.fromJson(preferences.getString(FACEBOOK_USER, null), FacebookUser.class);
    }

    public static void removeUserProfile(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(FACEBOOK_USER, Context.MODE_PRIVATE);
        String getUser = preferences.getString(FACEBOOK_USER, null);
        if (!TextUtils.isEmpty(getUser)) {
            preferences.edit().remove(FACEBOOK_USER).commit();
        }
    }

    public static void storeProfileDescription(FacebookUser facebookUser, Context context) {
        Gson gson = new Gson();
        SharedPreferences preferences = context.getSharedPreferences(FACEBOOK_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putString(FACEBOOK_USER, gson.toJson(facebookUser));
        prefsEditor.commit();
    }

    public static int getCurrentPageNumberEvent(Context context){
        SharedPreferences preferences = context.getSharedPreferences(CURRENT_TOTAL_PAGE_EVENT, Context.MODE_PRIVATE);
        return preferences.getInt(CURRENT_TOTAL_PAGE_EVENT, 0);
    }

    public static void storeCurrentTotalPageEvent(int totalPage, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(CURRENT_TOTAL_PAGE_EVENT, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putInt(CURRENT_TOTAL_PAGE_EVENT, totalPage);
        prefsEditor.commit();
    }

    public static int getCurrentPageNumberVenue(Context context){
        SharedPreferences preferences = context.getSharedPreferences(CURRENT_TOTAL_PAGE_EVENT, Context.MODE_PRIVATE);
        return preferences.getInt(CURRENT_TOTAL_PAGE_EVENT, 0);
    }

    public static void storeCurrentTotalPageVenue(int totalPage, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(CURRENT_TOTAL_PAGE_EVENT, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putInt(CURRENT_TOTAL_PAGE_EVENT, totalPage);
        prefsEditor.commit();
    }

}
