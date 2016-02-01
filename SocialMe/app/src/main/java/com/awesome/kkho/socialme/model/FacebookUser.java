package com.awesome.kkho.socialme.model;

import android.hardware.camera2.params.Face;
import android.location.Location;

/**
 * Created by kkho on 28.01.2016.
 */
public class FacebookUser {
    private FacebookPicture picture;

    private String id;

    private String first_name;

    private FacebookCover cover;

    private String email;

    private String last_name;

    public Location location;

    public FacebookPicture getPicture ()
    {
        return picture;
    }

    public void setPicture (FacebookPicture picture)
    {
        this.picture = picture;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getFirst_name ()
    {
        return first_name;
    }

    public void setFirst_name (String first_name)
    {
        this.first_name = first_name;
    }

    public FacebookCover getCover ()
    {
        return cover;
    }

    public void setCover (FacebookCover cover)
    {
        this.cover = cover;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getLast_name ()
    {
        return last_name;
    }

    public void setLast_name (String last_name)
    {
        this.last_name = last_name;
    }

    public Location getSelectedLocation() {
        return location;
    }

    public void setSelectedLocation(Location location) {
        this.location = location;
    }
}
