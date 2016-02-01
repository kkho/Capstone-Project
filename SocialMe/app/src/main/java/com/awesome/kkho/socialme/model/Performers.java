package com.awesome.kkho.socialme.model;

import java.io.Serializable;

/**
 * Created by kkho on 24.01.2016.
 */
public class Performers implements Serializable
{
    private Performer performer;

    public Performer getPerformer ()
    {
        return performer;
    }

    public void setPerformer (Performer performer)
    {
        this.performer = performer;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [performer = "+performer+"]";
    }
}
