package com.awesome.kkho.socialme.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kkho on 29.01.2016.
 */
public class DateUtil {

    public static String convertStringToDayMonthYear(String dateString) {
        if(dateString == null) {
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String convertedDateFormat = null;
        try {
            convertedDateFormat = new SimpleDateFormat("dd-MM-yyyy").format(dateFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDateFormat;
    }
}
