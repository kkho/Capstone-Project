package com.awesome.kkho.socialme.service;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.location.Address;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.awesome.kkho.socialme.model.ErrorResponse;
import com.awesome.kkho.socialme.model.Event;
import com.awesome.kkho.socialme.model.EventResponse;
import com.awesome.kkho.socialme.storage.SocialContract;
import com.awesome.kkho.socialme.util.Config;
import com.awesome.kkho.socialme.util.GeocoderUtil;
import com.awesome.kkho.socialme.util.SharedPrefUtil;
import com.google.gson.Gson;
import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkho on 24.01.2016.
 */
public class EventIntentService extends IntentService {
    public EventIntentService() {
        super("EventServiceIntent");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Gson gson = new Gson();
        final AndroidHttpClient httpClient = new AndroidHttpClient(Config.HTTP_PATH);
        httpClient.setMaxRetries(3);

        final ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra(Config.RECEIVER);
        int pagenumber = intent.getIntExtra(Config.PAGE_NUMBER, 1);

        List<Address> addresses = GeocoderUtil.GetAddressOfCurrentLocation(getApplicationContext(), intent);
        String locality = addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
        String events = GeocoderUtil.createEventParameter();

        ParameterMap params = httpClient.newParams()
                .add(Config.LOCATION_PATH, locality)
                .add(Config.EVENTS, events)
                .add("date","this week")
                .add(Config.PAGE_NUMBER_PATH, pagenumber + "")
                .add(Config.APP_KEY_PATH, Config.APP_KEY)
                .add("sort_order", "date");



        httpClient.get(Config.EVENT_PATH, params, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                if(httpResponse.getStatus() == 404) {
                    String responseJson = httpResponse.getBodyAsString();
                    ErrorResponse errorResponse = gson.fromJson(responseJson, ErrorResponse.class);
                    if (errorResponse != null) {
                        return;
                    }
                }

                if (HttpURLConnection.HTTP_OK == httpResponse.getStatus()) {
                    String responseJson = httpResponse.getBodyAsString();
                    final ContentResolver resolver = getContentResolver();
                    ArrayList<ContentProviderOperation> batch = new ArrayList<>();
                    EventResponse events = gson.fromJson(responseJson, EventResponse.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Config.EVENTS, events);
                    for(Event event : events.getEvents().getEvent()) {
                        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(SocialContract.EventDB.CONTENT_URI);
                        builder.withValues(Event.getContentValuesForModel(event));
                        batch.add(builder.build());
                    }

                    SharedPrefUtil.storeCurrentTotalPageEvent(Integer.parseInt(events.getPage_count()), getApplicationContext());

                    try {
                        resolver.applyBatch(SocialContract.CONTENT_AUTHORITY, batch);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (OperationApplicationException e) {
                        e.printStackTrace();
                    }
                   // receiver.send(0, bundle);
                } else {
                    Log.e("TAG ME", httpResponse.getStatus() +" " + httpResponse.getBodyAsString());
                }
            }
        });
    }
}
