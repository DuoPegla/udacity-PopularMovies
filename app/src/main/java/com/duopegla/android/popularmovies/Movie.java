package com.duopegla.android.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.duopegla.android.popularmovies.data.MovieContract;

import java.util.Calendar;

/**
 * Created by duopegla on 6.2.2017..
 */

public class Movie implements Parcelable
{
    private int id;
    private String originalTitle;
    private String posterPath;
    private String synopsis;
    private float userRating;
    private Calendar releaseDate;

    private boolean isPopular;
    private boolean isTopRated;
    private boolean isFavorite;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(synopsis);
        dest.writeFloat(userRating);
        dest.writeLong(releaseDate.getTimeInMillis());

        dest.writeByte((byte) (isPopular ? 1 : 0));
        dest.writeByte((byte) (isTopRated ? 1 : 0));
        dest.writeByte((byte) (isFavorite ? 1 : 0));
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

    public Movie(int id, String title, String posterPath, String synopsis, float userRating, Calendar releaseDate)
    {
        this.id = id;
        this.originalTitle = title;
        this.posterPath = posterPath;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;

        Log.d("MOVIE", toString());
    }

    public Movie(Parcel in)
    {
        this.id = in.readInt();
        this.originalTitle = in.readString();
        this.posterPath = in.readString();
        this.synopsis = in.readString();
        this.userRating = in.readFloat();
        this.releaseDate = Calendar.getInstance();
        this.releaseDate.setTimeInMillis(in.readLong());

        this.isPopular = in.readByte() != 0;
        this.isTopRated = in.readByte() != 0;
        this.isFavorite = in.readByte() != 0;
    }

    public Movie(Cursor cursor)
    {
        this.id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TMDB_ID));
        this.originalTitle = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE));
        this.posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
        this.synopsis = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SYNOPSIS));
        this.userRating = cursor.getFloat(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_USER_RATING));
        this.releaseDate = Calendar.getInstance();
        this.releaseDate.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));

        this.isPopular = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IS_MOST_POPULAR)) == 1;
        this.isTopRated = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IS_TOP_RATED)) == 1;
        this.isFavorite = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IS_FAVORITE)) == 1;

        Log.d("MOVIE_FROM_CURSOR", toString());
    }

    public int getId()
    {
        return this.id;
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

    public void setIsPopular(boolean isPopular)
    {
        this.isPopular = isPopular;
    }

    public boolean getIsPopular()
    {
        return this.isPopular;
    }

    public void setIsTopRated(boolean isTopRated)
    {
        this.isTopRated = isTopRated;
    }

    public boolean getIsTopRated()
    {
        return this.isTopRated;
    }

    public void setIsFavorite(boolean isFavorite)
    {
        this.isFavorite = isFavorite;
    }

    public boolean getIsFavorite()
    {
        return this.isFavorite;
    }

    public ContentValues getContentValues()
    {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_TMDB_ID, id);
        cv.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, originalTitle);
        cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, posterPath);
        cv.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, synopsis);
        cv.put(MovieContract.MovieEntry.COLUMN_USER_RATING, userRating);
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate.getTimeInMillis());

        cv.put(MovieContract.MovieEntry.COLUMN_IS_MOST_POPULAR, isPopular);
        cv.put(MovieContract.MovieEntry.COLUMN_IS_TOP_RATED, isTopRated);
        cv.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, isFavorite);

        return cv;
    }
}
