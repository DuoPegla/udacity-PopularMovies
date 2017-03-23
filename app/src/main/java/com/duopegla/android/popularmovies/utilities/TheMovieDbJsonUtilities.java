package com.duopegla.android.popularmovies.utilities;

import android.util.Log;

import com.duopegla.android.popularmovies.Movie;

import org.json.JSONArray;
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

    private static final String TMD_MOVIE_RESULTS = "results";

    private static final String TMD_ID = "id";
    private static final String TMD_TITLE = "original_title";
    private static final String TMD_POSTER_PATH = "poster_path";
    private static final String TMD_SYNOPSIS = "overview";
    private static final String TMD_USER_RATING = "vote_average";
    private static final String TMD_RELEASE_DATE = "release_date";

    public static Movie getMovieFromJson(JSONObject movieJson)
    {
        try
        {
            int movieId = movieJson.getInt(TMD_ID);
            String movieTitle = movieJson.getString(TMD_TITLE);
            String posterPath = movieJson.getString(TMD_POSTER_PATH);
            String synopsis = movieJson.getString(TMD_SYNOPSIS);
            float userRating = (float) movieJson.getDouble(TMD_USER_RATING);
            Calendar releaseDate = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM", Locale.ENGLISH);

            try
            {
                releaseDate.setTime(dateFormat.parse(movieJson.getString(TMD_RELEASE_DATE)));
            }
            catch (ParseException e)
            {
                Log.d(TAG, "Error parsing release data");
                e.printStackTrace();
            }

            return new Movie(movieId, movieTitle, posterPath, synopsis, userRating, releaseDate);

        }
        catch (JSONException e)
        {
            Log.d(TAG, "Error parsing movie JSON");
            e.printStackTrace();
        }

        return null;
    }

    public static Movie getMovieFromJson(String json)
    {
        try
        {
            JSONObject movieJson = new JSONObject(json);

            return getMovieFromJson(movieJson);
        }
        catch (JSONException e)
        {
            Log.d(TAG, "Error parsing movie JSON");
            e.printStackTrace();
        }

        return null;
    }

    public static Movie[] getMoviesFromJson(String json)
    {
        try
        {
            JSONObject moviesJson = new JSONObject(json);

            JSONArray moviesJsonArray = moviesJson.getJSONArray(TMD_MOVIE_RESULTS);
            Movie[] resultMovies = new Movie[moviesJsonArray.length()];

            for (int i = 0; i < resultMovies.length; i++)
            {
                resultMovies[i] = getMovieFromJson(moviesJsonArray.getJSONObject(i));
            }

            return resultMovies;
        }
        catch (JSONException e)
        {
            Log.d(TAG, "Error parsing movies JSON");
            e.printStackTrace();
        }

        return null;
    }
}
