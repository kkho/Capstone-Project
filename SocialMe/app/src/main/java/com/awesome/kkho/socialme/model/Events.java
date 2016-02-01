package com.awesome.kkho.socialme.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kkho on 24.01.2016.
 */
public class Events implements Serializable
{
    private List<Event> event;

    public List<Event> getEvent ()
    {
        return event;
    }

    public void setEvent (List<Event> event)
    {
        this.event = event;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [event = "+event+"]";
    }
}

