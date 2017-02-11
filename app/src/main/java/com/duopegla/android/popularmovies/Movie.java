package com.duopegla.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by duopegla on 6.2.2017..
 */

public class Movie implements Parcelable
{
    private String originalTitle;
    private String posterPath;
    private String synopsis;
    private float userRating;
    private Calendar releaseDate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(synopsis);
        dest.writeFloat(userRating);
        dest.writeLong(releaseDate.getTimeInMillis());
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(originalTitle)
                .append(" (")
                .append(releaseDate.get(Calendar.DAY_OF_MONTH))
                .append("/")
                .append(releaseDate.get(Calendar.MONTH)+1)
                .append("/")
                .append(releaseDate.get(Calendar.YEAR))
                .append(")");
        return stringBuilder.toString();
    }

    public Movie(String title, String posterPath, String synopsis, float userRating, Calendar releaseDate)
    {
        this.originalTitle = title;
        this.posterPath = posterPath;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;

        Log.d("MOVIE", toString());
    }

    public Movie(Parcel in)
    {
        this.originalTitle = in.readString();
        this.posterPath = in.readString();
        this.synopsis = in.readString();
        this.userRating = in.readFloat();
        this.releaseDate = Calendar.getInstance();
        this.releaseDate.setTimeInMillis(in.readLong());
    }

    public String getPosterPath()
    {
        return this.posterPath;
    }

    public String getOriginalTitle()
    {
        return this.originalTitle;
    }

    public String getSynopsis()
    {
        return this.synopsis;
    }

    public float getUserRating()
    {
        return this.userRating;
    }

    public Calendar getReleaseDate()
    {
        return this.releaseDate;
    }
}
