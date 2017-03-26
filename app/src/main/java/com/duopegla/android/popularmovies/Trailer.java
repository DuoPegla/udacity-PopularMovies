package com.duopegla.android.popularmovies;

import android.util.Log;

/**
 * Created by duopegla on 25.3.2017..
 */

public class Trailer
{
    private String id;
    private String key;
    private String name;
    private String site;

    private static final String YOUTUBE = "YouTube";

    public Trailer(String id, String key, String name, String site)
    {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
    }

    public String getId()
    {
        return this.id;
    }

    public String getKey()
    {
        return this.key;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean isYouTubeTrailer()
    {
        return this.site.equals(YOUTUBE);
    }
}
