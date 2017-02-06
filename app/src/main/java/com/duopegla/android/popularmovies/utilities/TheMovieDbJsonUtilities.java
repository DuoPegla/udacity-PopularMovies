package com.duopegla.android.popularmovies.utilities;

import android.util.Log;

import com.duopegla.android.popularmovies.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by duopegla on 6.2.2017..
 */

public final class TheMovieDbJsonUtilities {

    public static final String TAG = TheMovieDbJsonUtilities.class.getSimpleName();

    private static final String TMD_TITLE = "original_title";
    private static final String TMD_POSTER_PATH = "poster_path";
    private static final String TMD_SYNOPSIS = "overview";
    private static final String TMD_USER_RATING = "vote_average";
    private static final String TMD_RELEASE_DATE = "release_date";

    public static Movie getMovieFromJson(String json)
    {
        try {
            JSONObject movieJson = new JSONObject(json);

            String movieTitle = movieJson.getString(TMD_TITLE);
            String posterPath = movieJson.getString(TMD_POSTER_PATH);
            String synopsis = movieJson.getString(TMD_SYNOPSIS);
            float userRating = (float) movieJson.getDouble(TMD_USER_RATING);
            Calendar releaseDate = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM", Locale.ENGLISH);

            try {
                releaseDate.setTime(dateFormat.parse(movieJson.getString(TMD_RELEASE_DATE)));
            } catch (ParseException e) {
                Log.d(TAG, "Error parsing release data");
                e.printStackTrace();
            }

            return new Movie(movieTitle, posterPath, synopsis, userRating, releaseDate);

        } catch (JSONException e) {
            Log.d(TAG, "Error parsing movie JSON");
            e.printStackTrace();
        }

        return null;
    }

//    public static Movie[] getMoviesFromJson(String json)
//    {
//
//    }
}
