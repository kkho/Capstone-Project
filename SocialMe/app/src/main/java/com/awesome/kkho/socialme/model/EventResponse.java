package com.awesome.kkho.socialme.model;

import java.io.Serializable;

/**
 * Created by kkho on 24.01.2016.
 */
public class EventResponse implements Serializable {
    private String page_size;

    private String total_items;

    private Events events;

    private String page_items;

    private String first_item;

    private String page_number;

    private String search_time;

    private String last_item;

    private String page_count;

    public String getPage_size ()
    {
        return page_size;
    }

    public void setPage_size (String page_size)
    {
        this.page_size = page_size;
    }

    public String getTotal_items ()
    {
        return total_items;
    }

    public void setTotal_items (String total_items)
    {
        this.total_items = total_items;
    }

    public Events getEvents ()
    {
        return events;
    }

    public void setEvents (Events events)
    {
        this.events = events;
    }

    public String getPage_items ()
    {
        return page_items;
    }

    public void setPage_items (String page_items)
    {
        this.page_items = page_items;
    }

    public String getFirst_item ()
    {
        return first_item;
    }

    public void setFirst_item (String first_item)
    {
        this.first_item = first_item;
    }

    public String getPage_number ()
    {
        return page_number;
    }

    public void setPage_number (String page_number)
    {
        this.page_number = page_number;
    }

    public String getSearch_time ()
    {
        return search_time;
    }

    public void setSearch_time (String search_time)
    {
        this.search_time = search_time;
    }

    public String getLast_item ()
    {
        return last_item;
    }

    public void setLast_item (String last_item)
    {
        this.last_item = last_item;
    }

    public String getPage_count ()
    {
        return page_count;
    }

    public void setPage_count (String page_count)
    {
        this.page_count = page_count;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [page_size = "+page_size+", total_items = "+total_items+", events = "+events+", page_items = "+page_items+", first_item = "+first_item+", page_number = "+page_number+", search_time = "+search_time+", last_item = "+last_item+", page_count = "+page_count+"]";
    }
}
