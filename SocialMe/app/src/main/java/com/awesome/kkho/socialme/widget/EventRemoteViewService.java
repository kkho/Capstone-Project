package com.awesome.kkho.socialme.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.awesome.kkho.socialme.R;
import com.awesome.kkho.socialme.model.Image;
import com.awesome.kkho.socialme.storage.SocialContract;
import com.awesome.kkho.socialme.util.Config;
import com.google.gson.Gson;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kkho on 14.01.2016.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EventRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory();
    }

    private class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Cursor mData = null;

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (mData != null) {
                mData.close();
            }

            long token = Binder.clearCallingIdentity();
            Uri uri = SocialContract.EventDB.buildUri("start_time");
            mData = getContentResolver().query(uri,
                    Config.EventQuery.PROJECTION,
                    null,
                    new String[]{new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()))},
                    SocialContract.EventDB.START_TIME + " asc");
            Binder.restoreCallingIdentity(token);
        }

        @Override
        public void onDestroy() {
            if (mData != null) {
                mData.close();
                mData = null;
            }
        }

        @Override
        public int getCount() {
            return mData != null ? mData.getCount() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position == AdapterView.INVALID_POSITION || mData == null || !mData.moveToPosition(position)) {
                return null;
            }

            String address = (mData.getString(Config.EventQuery.VENUE_ADDRESS) + "," + Config.EventQuery.CITY_NAME
                    + ", " + Config.EventQuery.COUNTRY_NAME);
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_events);
            views.setTextViewText(R.id.event_title, mData.getString(Config.EventQuery.TITLE));
            views.setTextViewText(R.id.event_address, address);
            views.setTextViewText(R.id.event_starttime, mData.getString(Config.EventQuery.START_TIME));

            Bundle bundle = new Bundle();
            bundle.putInt("position", mData.getInt(Config.EventQuery.ID));
            Intent intent = new Intent();
            intent.putExtras(bundle);
            views.setOnClickFillInIntent(R.id.widget_main_layout, intent);
            return views;

        }

        @Override
        public RemoteViews getLoadingView() {
            return new RemoteViews(getPackageName(), R.layout.widget_events);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            if (mData.moveToPosition(position))
                return mData.getLong(Config.EventQuery.ID);
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
