package com.duopegla.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

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
        MOST_POPULAR, TOP_RATED
    }

    private static final String TAG = NetworkUtilities.class.getSimpleName();

    private static final String SCHEME = "http";
    private static final String TMD_AUTHORITY = "api.themoviedb.org";
    private static final String TMD_API_VERSION = "3";

    //private static final String TMD_API_URL = "https://api.themoviedb.org/3/";
    private static final String TMD_API_CATEGORY = "movie";
    private static final String TMD_MOST_POPULAR = "popular";
    private static final String TMD_TOP_RATED = "top_rated";
    private static final String TMD_API_KEY_PARAM = "api_key";

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
