package com.awesome.kkho.socialme.model;

import android.content.ContentValues;

import com.awesome.kkho.socialme.storage.SocialContract;
import com.awesome.kkho.socialme.util.Config;
import com.awesome.kkho.socialme.util.DateUtil;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by kkho on 31.01.2016.
 */
public class Venue implements Serializable {
    private String city_name;
    private String venue_name;
    private Image image;
    private String country_name;
    private String url;
    private String venue_type;
    private String id;
    private String region_name;
    private String address;
    private String description;
    private String name;
    private String postal_code;
    private double longitude;
    private double latitude;
    private boolean venueReady;

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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVenue_type() {
        return venue_type;
    }

    public void setVenue_type(String venue_type) {
        this.venue_type = venue_type;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setVenueReady(boolean venueReady) {
        this.venueReady = venueReady;
    }


    public boolean isVenueReady() {
        return venueReady;
    }

    public static ContentValues getContentValuesForModel(Venue venue) {
        ContentValues values = new ContentValues(14);
        values.put(SocialContract.VenueDB.ID, venue.getId());
        values.put(SocialContract.VenueDB.CITYNAME, venue.getCity_name());
        values.put(SocialContract.VenueDB.NAME, venue.getName());
        values.put(SocialContract.VenueDB.VENUE_NAME, venue.getVenue_name());
        values.put(SocialContract.VenueDB.IMAGEURL, new Gson().toJson(venue.getImage()));
        values.put(SocialContract.VenueDB.COUNTRYNAME, venue.getCountry_name());
        values.put(SocialContract.VenueDB.URL, venue.getUrl());
        values.put(SocialContract.VenueDB.VENUE_TYPE, venue.getVenue_type());
        values.put(SocialContract.VenueDB.REGION_NAME, venue.getRegion_name());
        values.put(SocialContract.VenueDB.ADDRESS, venue.getAddress());
        values.put(SocialContract.VenueDB.DESCRIPTION, venue.getDescription());
        values.put(SocialContract.VenueDB.POSTALCODE, venue.getPostal_code());
        values.put(SocialContract.VenueDB.LONGITUDE, venue.getLongitude());
        values.put(SocialContract.VenueDB.LATITUDE, venue.getLatitude());
        return values;
    }
}
