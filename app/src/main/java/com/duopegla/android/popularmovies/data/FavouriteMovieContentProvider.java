package com.duopegla.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by duopegla on 1.4.2017..
 */

public class FavouriteMovieContentProvider extends ContentProvider
{
    public static final int FAVOURITE_MOVIES = 100;
    public static final int FAVOURITE_MOVIES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoriteMovieDbHelper mFavoriteMovieDbHelper;

    public static UriMatcher buildUriMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavouriteMovieContract.AUTHORITY, FavouriteMovieContract.PATH_FAVOURITE_MOVIES, FAVOURITE_MOVIES);
        uriMatcher.addURI(FavouriteMovieContract.AUTHORITY, FavouriteMovieContract.PATH_FAVOURITE_MOVIES + "/#", FAVOURITE_MOVIES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate()
    {
        Context context = getContext();
        mFavoriteMovieDbHelper = new FavoriteMovieDbHelper(context);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        final SQLiteDatabase db = mFavoriteMovieDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor resultCursor;

        switch (match)
        {
            case FAVOURITE_MOVIES:
                resultCursor = db.query(
                        FavouriteMovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVOURITE_MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                resultCursor = db.query(
                        FavouriteMovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        FavouriteMovieContract.MovieEntry.COLUMN_TMDB_ID + "=?",
                        new String[] {id},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        resultCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return resultCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        final SQLiteDatabase db = mFavoriteMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri resultUri;

        switch (match)
        {
            case FAVOURITE_MOVIES:
                long id = db.insert(FavouriteMovieContract.MovieEntry.TABLE_NAME, null, values);
                if (id > 0)
                {
                    resultUri = ContentUris.withAppendedId(FavouriteMovieContract.MovieEntry.CONTENT_URI, values.getAsInteger(FavouriteMovieContract.MovieEntry.COLUMN_TMDB_ID));
                }
                else
                {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = mFavoriteMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int tasksDeleted;

        switch (match)
        {
            case FAVOURITE_MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(FavouriteMovieContract.MovieEntry.TABLE_NAME, FavouriteMovieContract.MovieEntry.COLUMN_TMDB_ID + "=?", new String[] {id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksDeleted != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
