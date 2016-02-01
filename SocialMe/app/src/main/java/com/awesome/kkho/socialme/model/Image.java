package com.awesome.kkho.socialme.model;

import java.io.Serializable;

/**
 * Created by kkho on 24.01.2016.
 */

public class Image implements Serializable
{
    private String height;

    private String width;

    private String caption;

    private String url;

    private ImageSize medium;

    public String getHeight ()
    {
        return height;
    }

    public void setHeight (String height)
    {
        this.height = height;
    }

    public String getWidth ()
    {
        return width;
    }

    public void setWidth (String width)
    {
        this.width = width;
    }

    public String getCaption ()
{
    return caption;
}

    public void setCaption (String caption)
    {
        this.caption = caption;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    public ImageSize getMedium ()
    {
        return medium;
    }

    public void setMedium (ImageSize medium)
    {
        this.medium = medium;
    }

}
