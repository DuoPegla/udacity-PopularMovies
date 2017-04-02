package com.duopegla.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by duopegla on 1.4.2017..
 */

public class FavouriteMovieContract
{
    public static final String AUTHORITY = "com.duopegla.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVOURITE_MOVIES = "favourite_movies";

    public static final class MovieEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE_MOVIES).build();

        public static final String TABLE_NAME = "favourite_movies";
        public static final String COLUMN_TMDB_ID = "tmdb_id";
        public static final String COLUMN_TITLE = "title";
    }
}
