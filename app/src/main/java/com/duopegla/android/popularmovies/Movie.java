package com.duopegla.android.popularmovies;

import java.util.Calendar;

/**
 * Created by duopegla on 6.2.2017..
 */

public class Movie
{
    private String originalTitle;
    private String posterPath;
    private String synopsis;
    private float userRating;
    private Calendar releaseDate;

    public Movie(String title, String posterPath, String synopsis, float userRating, Calendar releaseDate)
    {
        this.originalTitle = title;
        this.posterPath = posterPath;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(originalTitle.length() + 7);
        stringBuilder.append(originalTitle).append(" (").append(releaseDate.get(Calendar.YEAR)).append(")");
        return stringBuilder.toString();
    }

    public String getPosterPath()
    {
        return this.posterPath;
    }
}
