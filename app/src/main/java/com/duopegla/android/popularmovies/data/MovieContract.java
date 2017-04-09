package com.duopegla.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by duopegla on 1.4.2017..
 */

public class MovieContract
{
    public static final String AUTHORITY = "com.duopegla.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_TMDB_ID = "tmdb_id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_IS_MOST_POPULAR = "is_most_popular";
        public static final String COLUMN_IS_TOP_RATED = "is_top_rated";
        public static final String COLUMN_IS_FAVORITE = "is_favorite";
    }
}
