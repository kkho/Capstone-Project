package com.awesome.kkho.socialme.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kkho on 31.01.2016.
 */
public class Venues implements Serializable {
    private List<Venue> venue;

    public List<Venue> getVenue() {
        return venue;
    }

    public void setVenue(List<Venue> venue) {
        this.venue = venue;
    }

    @Override
    public String toString() {
        return "ClassPojo [venue = " + venue + "]";
    }
}