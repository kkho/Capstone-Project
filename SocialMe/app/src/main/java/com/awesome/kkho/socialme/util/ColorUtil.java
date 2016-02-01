package com.awesome.kkho.socialme.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import com.awesome.kkho.socialme.R;

/**
 * Created by kkho on 10.01.2016.
 */
public class ColorUtil {

    public static void setActionbarColor(int titleColor, Toolbar actionbar) {
        actionbar.setTitle(TextUtil.setSpannableText(titleColor, actionbar.getTitle()));
    }

    public static void colorProgressBar(ProgressBar mProgressBar, Context mContext) {
        mProgressBar.getIndeterminateDrawable().setColorFilter(mContext.getResources().getColor(R.color.colorPrimary),
                PorterDuff.Mode.MULTIPLY);
    }
}
