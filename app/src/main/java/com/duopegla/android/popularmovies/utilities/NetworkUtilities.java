package com.duopegla.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.duopegla.android.popularmovies.Trailer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by duopegla on 5.2.2017..
 */

public final class NetworkUtilities
{
    public enum MovieResultSort
    {
        MOST_POPULAR, TOP_RATED, FAVORITE
    }

    private static final String TAG = NetworkUtilities.class.getSimpleName();

    private static final String SCHEME = "http";
    private static final String TMD_AUTHORITY = "api.themoviedb.org";
    private static final String TMD_API_VERSION = "3";

    private static final String TMD_API_CATEGORY = "movie";
    private static final String TMD_MOST_POPULAR = "popular";
    private static final String TMD_TOP_RATED = "top_rated";
    private static final String TMD_API_KEY_PARAM = "api_key";
    private static final String TMD_TRAILER_PATH = "videos";
    private static final String TMD_REVIEW_PATH = "reviews";

    private static final String TMD_POSTER_AUTHORITY = "image.tmdb.org";
    private static final String TMD_POSTER_PATH = "t/p";
    private static final String TMD_POSTER_WIDTH = "w500"; // can be: "w92", "w154", "w185", "w342", "w500", "w780", or "original"

    private static final String YOUTUBE_AUTHORITY = "www.youtube.com";
    private static final String YOUTUBE_API_CATEGORY = "watch";
    private static final String YOUTUBE_VIDEO_PARAM = "v";

    private static final String format = "json";

    public static URL buildRequestUrl(String apiKey, MovieResultSort sortBy)
    {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(SCHEME)
            .authority(TMD_AUTHORITY)
            .appendPath(TMD_API_VERSION)
            .appendPath(TMD_API_CATEGORY);

        if (sortBy == MovieResultSort.MOST_POPULAR)
        {
            uriBuilder.appendPath(TMD_MOST_POPULAR);
        }
        else if (sortBy == MovieResultSort.TOP_RATED)
        {
            uriBuilder.appendPath(TMD_TOP_RATED);
        }

        uriBuilder.appendQueryParameter(TMD_API_KEY_PARAM, apiKey);
        uriBuilder.build();

        try
        {
            URL builtUrl = new URL(uriBuilder.toString());
            return builtUrl;
        }
        catch (MalformedURLException e)
        {
            Log.d(TAG, "Unable to build requested URL " + uriBuilder.toString());
            e.printStackTrace();
        }

        return null;
    }

    public static URL buildPosterRequestUrl(String relativePath)
    {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(SCHEME)
                .authority(TMD_POSTER_AUTHORITY)
                .appendEncodedPath(TMD_POSTER_PATH)
                .appendPath(TMD_POSTER_WIDTH)
                .appendEncodedPath(relativePath).build();

        try
        {
            URL builtUrl = new URL(uriBuilder.toString());
            return builtUrl;
        }
        catch (MalformedURLException e)
        {
            Log.d(TAG, "Unable to build requested URL " + uriBuilder.toString());
            e.printStackTrace();
        }

        return null;
    }

    public static URL buildTrailerRequestUrl(String apiKey, int movieId)
    {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(SCHEME)
                .authority(TMD_AUTHORITY)
                .appendPath(TMD_API_VERSION)
                .appendPath(TMD_API_CATEGORY)
                .appendPath(String.valueOf(movieId))
                .appendPath(TMD_TRAILER_PATH)
                .appendQueryParameter(TMD_API_KEY_PARAM, apiKey);

        uriBuilder.build();

        try
        {
            URL builtUrl = new URL(uriBuilder.toString());
            return builtUrl;
        }
        catch (MalformedURLException e)
        {
            Log.d(TAG, "Unable to build request URL " + uriBuilder.toString());
            e.printStackTrace();
        }

        return null;
    }

    public static URL buildReviewRequestUrl(String apiKey, int movieId)
    {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(SCHEME)
                .authority(TMD_AUTHORITY)
                .appendPath(TMD_API_VERSION)
                .appendPath(TMD_API_CATEGORY)
                .appendPath(String.valueOf(movieId))
                .appendPath(TMD_REVIEW_PATH)
                .appendQueryParameter(TMD_API_KEY_PARAM, apiKey);

        uriBuilder.build();

        try
        {
            URL builtUrl = new URL(uriBuilder.toString());
            return builtUrl;
        }
        catch (MalformedURLException e)
        {
            Log.d(TAG, "Unable to build request URL " + uriBuilder.toString());
            e.printStackTrace();
        }

        return null;
    }

    public static URL buildYouTubeTrailerUrl(Trailer trailer)
    {
        if (!trailer.isYouTubeTrailer())
            return null;

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(SCHEME)
                .authority(YOUTUBE_AUTHORITY)
                .appendPath(YOUTUBE_API_CATEGORY)
                .appendQueryParameter(YOUTUBE_VIDEO_PARAM, trailer.getKey());

        uriBuilder.build();

        try
        {
            URL builtUrl = new URL(uriBuilder.toString());
            return builtUrl;
        }
        catch (MalformedURLException e)
        {
            Log.d(TAG, "Unable to build request URL " + uriBuilder.toString());
            e.printStackTrace();
        }

        return null;
    }

    public static String getResponseFromHttpUrl(URL requestUrl) throws IOException
    {
        HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();

        try
        {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput)
            {
                return scanner.next();
            }
            else
            {
                return null;
            }
        }
        finally
        {
            urlConnection.disconnect();
        }
    }
}
