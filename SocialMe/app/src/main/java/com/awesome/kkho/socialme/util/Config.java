package com.awesome.kkho.socialme.util;

import com.awesome.kkho.socialme.storage.SocialContract;

/**
 * Created by kkho on 24.01.2016.
 */
public class Config {

    public static final String EVENTS = "events";
    public static final String PAGE_NUMBER = "page_number";
    public static final String VENUES = "venues";
    public static String HTTP_PATH = "http://api.eventful.com/json/";
    public static String APP_KEY_PATH = "app_key";
    public static String LOCATION_PATH = "location";
    public static String PAGE_NUMBER_PATH = "page_number";
    public static String CATEGORY_PATH = "category";
    public static String APP_KEY = "OWN APP KEY, get from eventful.com";

    public static String EVENT_PATH = "events/search";
    public static String VENUE_PATH = "venues/search";

    public static final String RECEIVER = "Receiver";
    public static final String LOCATION = "Location";


    public static interface EventQuery {
        public static String[] PROJECTION = {
                SocialContract.EventDB.ID,
                SocialContract.EventDB.CITY_NAME,
                SocialContract.EventDB.VENUE_NAME,
                SocialContract.EventDB.VENUE_DISPLAY,
                SocialContract.EventDB.COUNTRY_NAME,
                SocialContract.EventDB.REGION_NAME,
                SocialContract.EventDB.TITLE,
                SocialContract.EventDB.PERFORMERS,
                SocialContract.EventDB.CREATED,
                SocialContract.EventDB.DESCRIPTION,
                SocialContract.EventDB.VENUE_ID,
                SocialContract.EventDB.ALL_DAY,
                SocialContract.EventDB.LONGITUDE,
                SocialContract.EventDB.LATITUDE,
                SocialContract.EventDB.STOP_TIME,
                SocialContract.EventDB.START_TIME,
                SocialContract.EventDB.IMAGEURL,
                SocialContract.EventDB.VENUE_URL,
                SocialContract.EventDB.URL,
                SocialContract.EventDB.VENUE_ADDRESS,
                SocialContract.EventDB.POSTAL_CODE
        };


        public static int ID = 0;
        public static int CITY_NAME = 1;
        public static int VENUE_NAME = 2;
        public static int VENUE_DISPLAY = 3;
        public static int COUNTRY_NAME = 4;
        public static int REGION_NAME = 5;
        public static int TITLE = 6;
        public static int PERFORMERS = 7;
        public static int CREATED = 8;
        public static int DESCRIPTION = 9;
        public static int VENUE_ID = 10;
        public static int ALL_DAY = 11;
        public static int LONGITUDE = 12;
        public static int LATITUDE = 13;
        public static int STOP_TIME = 14;
        public static int START_TIME = 15;
        public static int IMAGEURL = 16;
        public static int VENUE_URL = 17;
        public static int URL = 18;
        public static int VENUE_ADDRESS = 19;
        public static int POSTAL_CODE = 20;
    }

    public static interface VenueQuery {
        public static String[] PROJECTION = {
                SocialContract.VenueDB.ID,
                SocialContract.VenueDB.CITYNAME,
                SocialContract.VenueDB.NAME,
                SocialContract.VenueDB.VENUE_NAME,
                SocialContract.VenueDB.IMAGEURL,
                SocialContract.VenueDB.COUNTRYNAME,
                SocialContract.VenueDB.URL,
                SocialContract.VenueDB.VENUE_TYPE,
                SocialContract.VenueDB.REGION_NAME,
                SocialContract.VenueDB.ADDRESS,
                SocialContract.VenueDB.DESCRIPTION,
                SocialContract.VenueDB.POSTALCODE,
                SocialContract.VenueDB.LONGITUDE,
                SocialContract.VenueDB.LATITUDE
        };

        public static int ID = 0;
        public static int CITYNAME = 1;
        public static int NAME = 2;
        public static int VENUE_NAME = 3;
        public static int IMAGEURL = 4;
        public static int COUNTRYNAME = 5;
        public static int URL = 6;
        public static int VENUE_TYPE = 7;
        public static int REGION_NAME = 8;
        public static int ADDRESS = 9;
        public static int DESCRIPTION = 10;
        public static int POSTALCODE = 11;
        public static int LONGITUDE = 12;
        public static int LATITUDE = 13;
    }

    // Permissions
    public static final int REQUEST_LOCATION = 0;
}
