package com.awesome.kkho.socialme.storage;

import android.net.Uri;
import android.provider.BaseColumns;

import com.awesome.kkho.socialme.model.Image;
import com.awesome.kkho.socialme.model.ImageSize;

/**
 * Created by kkho on 24.01.2016.
 */
public class SocialContract {
    public static final String CONTENT_AUTHORITY = "com.awesome.kkho.socialme";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String TABLE_EVENT = "event";
    public static final String TABLE_VENUE = "venue";


    interface EventColumns {
        String ID = "eventId";
        String CITY_NAME = "city_name";
        String VENUE_NAME = "venue_name";
        String VENUE_DISPLAY = "venue_display";
        String COUNTRY_NAME = "country_name";
        String REGION_NAME = "region_name";
        String TITLE = "title";
        String PERFORMERS = "performers";
        String CREATED = "created";
        String DESCRIPTION = "description";
        String VENUE_ID = "venue_id";
        String ALL_DAY = "all_day";
        String LONGITUDE = "longitude";
        String LATITUDE = "latitude";
        String STOP_TIME = "stop_time";
        String START_TIME = "start_time";
        String IMAGEURL = "image_url";
        String VENUE_URL = "venue_url";
        String URL = "url";
        String VENUE_ADDRESS = "venue_address";
        String POSTAL_CODE = "postal_code";

    }

    public interface VenueColumns {
        String ID = "venueId";
        String CITYNAME = "city_name";
        String NAME = "name";
        String VENUE_NAME = "venue_name";
        String IMAGEURL = "image_url";
        String COUNTRYNAME = "country_name";
        String URL = "url";
        String VENUE_TYPE = "venue_type";
        String REGION_NAME = "region_name";
        String ADDRESS = "address";
        String DESCRIPTION = "description";
        String POSTALCODE = "postal_code";
        String LONGITUDE = "longitude";
        String LATITUDE = "latitude";
    }

    public static class EventDB implements EventColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_EVENT).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.socialme.event";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.socialme.event";

        public static Uri buildUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
    }

    public static class VenueDB implements VenueColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_VENUE).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.socialme.venue";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.socialme.venue";

        public static Uri buildUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
    }
}
