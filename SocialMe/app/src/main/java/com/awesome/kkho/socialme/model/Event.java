package com.awesome.kkho.socialme.model;

import android.content.ContentValues;

import com.awesome.kkho.socialme.storage.SocialContract;
import com.awesome.kkho.socialme.util.DateUtil;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by kkho on 24.01.2016.
 */

public class Event implements Serializable, Comparable<Event> {
    private String id;
    private String city_name;
    private String venue_name;
    private String venue_display;
    private String country_name;
    private String region_name;
    private String title;
    private Object performers;
    private String created;
    private String description;
    private String venue_id;
    private String all_day;
    private double longitude;
    private String stop_time;
    private Image image;
    private String venue_url;
    private String url;
    private String venue_address;
    private String postal_code;
    private String start_time;
    private double latitude;
    private boolean eventReady;

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public void setVenue_name(String venue_name) {
        this.venue_name = venue_name;
    }

    public String getVenue_display() {
        return venue_display;
    }

    public void setVenue_display(String venue_display) {
        this.venue_display = venue_display;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getPerformers() {
        return performers;
    }

    public void setPerformers(Object performers) {
        this.performers = performers;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(String venue_id) {
        this.venue_id = venue_id;
    }

    public String getAll_day() {
        return all_day;
    }

    public void setAll_day(String all_day) {
        this.all_day = all_day;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStop_time() {
        return stop_time;
    }

    public void setStop_time(String stop_time) {
        this.stop_time = stop_time;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getVenue_url() {
        return venue_url;
    }

    public void setVenue_url(String venue_url) {
        this.venue_url = venue_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVenue_address() {
        return venue_address;
    }

    public void setVenue_address(String venue_address) {
        this.venue_address = venue_address;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "id = " + id + ", title = " + title;
    }

    public void setEventReady(boolean state) {
        eventReady = state;
    }

    public boolean isEventReady() {
        return eventReady;
    }

    @Override
    public int compareTo(Event another) {
        return getId().compareTo(another.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Event other = (Event) obj;
        if (this.getId().equals(other.getId())
                || this.getTitle().equals(other.getTitle())) {
            return false;
        }

        return true;
    }

    public static ContentValues getContentValuesForModel(Event event) {
        ContentValues values = new ContentValues(21);
        values.put(SocialContract.EventDB.ID, event.getId());
        values.put(SocialContract.EventDB.CITY_NAME, event.getCity_name());
        values.put(SocialContract.EventDB.VENUE_NAME, event.getVenue_name());
        values.put(SocialContract.EventDB.VENUE_DISPLAY, event.getVenue_display());
        values.put(SocialContract.EventDB.COUNTRY_NAME, event.getCountry_name());
        values.put(SocialContract.EventDB.REGION_NAME, event.getRegion_name());
        values.put(SocialContract.EventDB.TITLE, event.getTitle());
        values.put(SocialContract.EventDB.PERFORMERS, new Gson().toJson(event.getPerformers()));
        values.put(SocialContract.EventDB.CREATED, DateUtil.convertStringToDayMonthYear(event.getCreated()));
        values.put(SocialContract.EventDB.DESCRIPTION, event.getDescription());
        values.put(SocialContract.EventDB.VENUE_ID, event.getVenue_id());
        values.put(SocialContract.EventDB.ALL_DAY, event.getAll_day());
        values.put(SocialContract.EventDB.LATITUDE, event.getLatitude());
        values.put(SocialContract.EventDB.LONGITUDE, event.getLongitude());
        values.put(SocialContract.EventDB.STOP_TIME, DateUtil.convertStringToDayMonthYear(event.getStop_time()));
        values.put(SocialContract.EventDB.START_TIME, DateUtil.convertStringToDayMonthYear(event.getStart_time()));
        values.put(SocialContract.EventDB.IMAGEURL, new Gson().toJson(event.getImage()));
        values.put(SocialContract.EventDB.VENUE_URL, event.getVenue_url());
        values.put(SocialContract.EventDB.URL, event.getUrl());
        values.put(SocialContract.EventDB.VENUE_ADDRESS, event.getVenue_address());
        values.put(SocialContract.EventDB.POSTAL_CODE, event.getPostal_code());
        return values;
    }
}