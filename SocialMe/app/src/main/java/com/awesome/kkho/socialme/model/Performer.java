package com.awesome.kkho.socialme.model;

import java.io.Serializable;

/**
 * Created by kkho on 24.01.2016.
 */

public class Performer implements Serializable
{
    private String id;

    private String linker;

    private String short_bio;

    private String name;

    private String url;

    private String creator;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getLinker ()
    {
        return linker;
    }

    public void setLinker (String linker)
    {
        this.linker = linker;
    }

    public String getShort_bio ()
    {
        return short_bio;
    }

    public void setShort_bio (String short_bio)
    {
        this.short_bio = short_bio;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    public String getCreator ()
    {
        return creator;
    }

    public void setCreator (String creator)
    {
        this.creator = creator;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", linker = "+linker+", short_bio = "+short_bio+", name = "+name+", url = "+url+", creator = "+creator+"]";
    }
}

