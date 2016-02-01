package com.awesome.kkho.socialme.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.awesome.kkho.socialme.R;

/**
 * Created by kkho on 10.01.2016.
 */
public class UiUtils {

    public static AlertDialog.Builder CreateAlertDialog(Context context, String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
        alertDialogBuilder.setTitle(title);

        alertDialogBuilder.setNeutralButton(R.string.general_cancel_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void startTransitionSharedElement(View touchedView, Activity activity, int moviePosition,
                                                    Intent movieDetailActivityIntent) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(
                        activity, touchedView, "moviecover");
        activity.startActivity(movieDetailActivityIntent, options.toBundle());
    }

    public static Snackbar initSnackBar(View view, String message, Context context) {
        return Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setActionTextColor(context.getResources().getColor(android.R.color.white));
    }
}
