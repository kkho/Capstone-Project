package com.awesome.kkho.socialme.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.awesome.kkho.socialme.model.Image;

/**
 * Created by kkho on 24.01.2016.
 */
public class SocialDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "socialme_database.db";
    private static final int VER_LAUNCH = 2;
    private static final int DB_VERSION = VER_LAUNCH;

    public SocialDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SocialContract.TABLE_EVENT + " ("
                + SocialContract.VenueDB._ID + " integer primary key autoincrement, "
                + SocialContract.EventDB.ID + " text,"
                + SocialContract.EventDB.CITY_NAME + " text,"
                + SocialContract.EventDB.VENUE_NAME + " text,"
                + SocialContract.EventDB.VENUE_DISPLAY + " text, "
                + SocialContract.EventDB.COUNTRY_NAME + " text, "
                + SocialContract.EventDB.REGION_NAME + " text, "
                + SocialContract.EventDB.TITLE + " text, "
                + SocialContract.EventDB.PERFORMERS + " text, "
                + SocialContract.EventDB.CREATED + " text, "
                + SocialContract.EventDB.DESCRIPTION + " text, "
                + SocialContract.EventDB.VENUE_ID + " text, "
                + SocialContract.EventDB.ALL_DAY + " text, "
                + SocialContract.EventDB.LATITUDE + " real, "
                + SocialContract.EventDB.LONGITUDE + " real, "
                + SocialContract.EventDB.STOP_TIME + " text, "
                + SocialContract.EventDB.START_TIME + " text, "
                + SocialContract.EventDB.IMAGEURL + " text, "
                + SocialContract.EventDB.VENUE_URL + " text, "
                + SocialContract.EventDB.URL + " text, "
                + SocialContract.EventDB.VENUE_ADDRESS + " text, "
                + SocialContract.EventDB.POSTAL_CODE + " text) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + SocialContract.TABLE_VENUE + " ("
                        + SocialContract.VenueDB._ID + " integer primary key autoincrement,"
                        + SocialContract.VenueDB.ID + " text not null, "
                        + SocialContract.VenueDB.CITYNAME + " text, "
                        + SocialContract.VenueDB.NAME + " text, "
                        + SocialContract.VenueDB.VENUE_NAME + " text, "
                        + SocialContract.VenueDB.IMAGEURL + " text, "
                        + SocialContract.VenueDB.COUNTRYNAME + " text,"
                        + SocialContract.VenueDB.URL + " text, "
                        + SocialContract.VenueDB.VENUE_TYPE + " text, "
                        + SocialContract.VenueDB.REGION_NAME + " text, "
                        + SocialContract.VenueDB.ADDRESS + " text, "
                        + SocialContract.VenueDB.DESCRIPTION + " text, "
                        + SocialContract.VenueDB.POSTALCODE + " text, "
                        + SocialContract.VenueDB.LONGITUDE + " real, "
                        + SocialContract.VenueDB.LATITUDE + " real)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SocialContract.TABLE_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + SocialContract.TABLE_VENUE);
        onCreate(db);

    }
}
