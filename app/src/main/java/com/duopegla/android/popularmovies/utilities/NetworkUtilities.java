package com.duopegla.android.popularmovies.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by duopegla on 5.2.2017..
 */

public final class NetworkUtilities
{
    private static final String TAG = NetworkUtilities.class.getSimpleName();

    private static final String MOVIE_DB_URL = "https://api.themoviedb.org/3/";

    private static final String format = "json";

    final static String QUERY_PARAM = "q";

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
