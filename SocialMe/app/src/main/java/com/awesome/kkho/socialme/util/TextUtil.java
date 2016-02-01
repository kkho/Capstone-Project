package com.awesome.kkho.socialme.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kkho on 10.01.2016.
 */
public class TextUtil {

    public static String unaccent(String s) {
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        return normalized.replaceAll("[^\\p{ASCII}]", "");
    }

    public static Spannable setSpannableText(int titleTextColor, CharSequence title) {
        Spannable text = new SpannableString(title);
        text.setSpan(new ForegroundColorSpan(titleTextColor), 0, text.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return text;
    }

    public static String getYearFromDate(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date startDate = df.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            return calendar.get(Calendar.YEAR) + "";

        }catch(ParseException e) {

        }

        return null;
    }
}
