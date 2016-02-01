package com.awesome.kkho.socialme.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by kkho on 05.06.2015.
 */
public class NetworkUtil {

    public static boolean isNetworkOn(Activity activity) {
        if (activity == null) return false;
        ConnectivityManager conMan = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        //mobile
        NetworkInfo.State state = conMan.getActiveNetworkInfo().getState();
        return state == NetworkInfo.State.CONNECTED;
    }
}
