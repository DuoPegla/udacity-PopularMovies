package com.duopegla.android.popularmovies.utilities;

import android.util.Log;

import com.duopegla.android.popularmovies.Movie;
import com.duopegla.android.popularmovies.Review;
import com.duopegla.android.popularmovies.Trailer;

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

    private static final String TMD_TRAILER_RESULTS = "results";
    private static final String TMD_TRAILER_ID = "id";
    private static final String TMD_TRAILER_KEY = "key";
    private static final String TMD_TRAILER_NAME = "name";
    private static final String TMD_TRAILER_SITE = "site";

    private static final String TMD_REVIEW_RESULTS = "reviews";
    private static final String TMD_REVIEW_ID = "id";
    private static final String TMD_REVIEW_AUTHOR = "author";
    private static final String TMD_REVIEW_CONTENT = "content";
    private static final String TMD_REVIEW_URL = "url";


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

    public static Trailer getTrailerFromJson(JSONObject trailerJson)
    {
        try
        {
            String id = trailerJson.getString(TMD_TRAILER_ID);
            String key = trailerJson.getString(TMD_TRAILER_KEY);
            String name = trailerJson.getString(TMD_TRAILER_NAME);
            String site = trailerJson.getString(TMD_TRAILER_SITE);

            return new Trailer(id, key, name, site);
        }
        catch (JSONException e)
        {
            Log.d(TAG, "Error parsing trailer JSON");
            e.printStackTrace();
        }

        return null;
    }

    public static Trailer[] getTrailersFromJson(String json)
    {
        try
        {
            JSONObject trailerJson = new JSONObject(json);

            JSONArray trailerJsonArray = trailerJson.getJSONArray(TMD_TRAILER_RESULTS);
            Trailer[] resultTrailers = new Trailer[trailerJsonArray.length()];

            for (int i = 0; i < resultTrailers.length; i++)
            {
                resultTrailers[i] = getTrailerFromJson(trailerJsonArray.getJSONObject(i));
            }

            return resultTrailers;
        }
        catch (JSONException e)
        {
            Log.d(TAG, "Error parsing trailers JSON");
            e.printStackTrace();
        }

        return null;
    }

    public static Review getReviewFromJson(JSONObject reviewJson)
    {
        try
        {
            String id = reviewJson.getString(TMD_REVIEW_ID);
            String author = reviewJson.getString(TMD_REVIEW_AUTHOR);
            String content = reviewJson.getString(TMD_REVIEW_CONTENT);
            String url = reviewJson.getString(TMD_REVIEW_URL);

            return new Review(id, author, content, url);
        }
        catch (JSONException e)
        {
            Log.d(TAG, "Error parsing review JSON");
            e.printStackTrace();
        }

        return null;
    }

    public static Review[] getReviewsFromJson(String json)
    {
        try
        {
            JSONObject reviewJson = new JSONObject(json);

            JSONArray reviewJsonArray = reviewJson.getJSONArray(TMD_TRAILER_RESULTS);
            Review[] resultReviews = new Review[reviewJsonArray.length()];

            for (int i = 0; i < resultReviews.length; i++)
            {
                resultReviews[i] = getReviewFromJson(reviewJsonArray.getJSONObject(i));
            }

            return resultReviews;
        }
        catch (JSONException e)
        {
            Log.d(TAG, "Error parsing reviews JSON");
            e.printStackTrace();
        }

        return null;
    }
}
