package com.awesome.kkho.socialme.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import com.awesome.kkho.socialme.R;
import com.awesome.kkho.socialme.ui.activity.HomeActivity;



/**
 * Created by kkho on 14.01.2016.
 */
public class EventResultProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for(int i = 0; i < appWidgetIds.length; i++) {
            updateScores(context, appWidgetManager, appWidgetIds[i]);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);


    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    void updateScores(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Intent intent = new Intent(context, EventRemoteViewService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_events);
        remoteViews.setRemoteAdapter(R.id.result_list, intent);
        remoteViews.setEmptyView(R.id.result_list, R.id.no_events_now);

        Intent launchMainIntent = new Intent(context, HomeActivity.class);
        launchMainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchMainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.result_list, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }
}
