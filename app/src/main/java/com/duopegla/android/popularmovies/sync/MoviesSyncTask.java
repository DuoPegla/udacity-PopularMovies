package com.duopegla.android.popularmovies.sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.duopegla.android.popularmovies.Movie;
import com.duopegla.android.popularmovies.R;
import com.duopegla.android.popularmovies.data.MovieContract;
import com.duopegla.android.popularmovies.utilities.NetworkUtilities;
import com.duopegla.android.popularmovies.utilities.TheMovieDbJsonUtilities;

import java.net.URL;

/**
 * Created by duopegla on 8.4.2017..
 */

public class MoviesSyncTask
{
    public static final String TAG = MoviesSyncTask.class.getSimpleName();

    synchronized public static void syncMovies(Context context)
    {
        Log.d(TAG, "Started data sync...");
        try
        {
            // Get most popular movies from TMDB
            URL mostPopularMoviesUrl = NetworkUtilities.buildRequestUrl(
                    context.getString(R.string.themoviedb_api_key),
                    NetworkUtilities.MovieResultSort.MOST_POPULAR);

            Movie[] mostPopularMovies = TheMovieDbJsonUtilities.getMoviesFromJson(
                    NetworkUtilities.getResponseFromHttpUrl(mostPopularMoviesUrl));


            // Get top rated movies from TMDB
            URL topRatedMoviesUrl = NetworkUtilities.buildRequestUrl(
                    context.getString(R.string.themoviedb_api_key),
                    NetworkUtilities.MovieResultSort.TOP_RATED);

            Movie[] topRatedMovies = TheMovieDbJsonUtilities.getMoviesFromJson(
                    NetworkUtilities.getResponseFromHttpUrl(topRatedMoviesUrl));

            // Delete all non favorite movies
            context.getContentResolver().delete(
                    MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.COLUMN_IS_FAVORITE + "= 0",
                    null);

            if (mostPopularMovies != null && mostPopularMovies.length != 0)
            {
                for (Movie movie : mostPopularMovies)
                {
                    int tmdbId = movie.getId();
                    Uri movieWithIdUri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(tmdbId)).build();

                    // Check if this movie is already in db
                    Cursor existingMovieFromDb = context.getContentResolver().query(movieWithIdUri, null, null, null, null);

                    movie.setIsPopular(true);
                    movie.setIsTopRated(false);
                    ContentValues movieContentValues = movie.getContentValues();
                    // If the movie is already in db update it
                    if (existingMovieFromDb.getCount() != 0)
                    {
                        movie.setIsFavorite(true);
                        context.getContentResolver().update(movieWithIdUri, movieContentValues, null, null);
                    }
                    // else insert
                    else
                    {
                        context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieContentValues);
                    }
                }
            }

            if (topRatedMovies != null && topRatedMovies.length != 0)
            {
                for (Movie movie : topRatedMovies)
                {
                    int tmdbId = movie.getId();
                    Uri movieWithIdUri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(tmdbId)).build();

                    Cursor existingMovieFromDb = context.getContentResolver().query(movieWithIdUri, null, null, null, null);

                    ContentValues movieContentValues;
                    if (existingMovieFromDb.getCount() != 0)
                    {
                        existingMovieFromDb.moveToFirst();
                        boolean isFavorite = existingMovieFromDb.getInt(
                                existingMovieFromDb.getColumnIndex(
                                        MovieContract.MovieEntry.COLUMN_IS_FAVORITE)) == 1;

                        boolean isMostPopular = existingMovieFromDb.getInt(
                                existingMovieFromDb.getColumnIndex(
                                        MovieContract.MovieEntry.COLUMN_IS_MOST_POPULAR)) == 1;

                        movie.setIsFavorite(isFavorite);
                        movie.setIsPopular(isMostPopular);
                        movie.setIsTopRated(true);
                        movieContentValues = movie.getContentValues();

                        context.getContentResolver().update(movieWithIdUri, movieContentValues, null, null);
                    }
                    else
                    {
                        movieContentValues = movie.getContentValues();
                        context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieContentValues);
                    }
                }
            }

            Log.d(TAG, "Data sync successful.");
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error syncing data.");
            e.printStackTrace();
        }
    }
}
