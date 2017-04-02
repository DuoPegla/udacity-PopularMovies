package com.duopegla.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by duopegla on 1.4.2017..
 */

public class FavoriteMovieDbHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "moviesDb.db";
    private static final int VERSION = 1;

    FavoriteMovieDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String CREATE_TABLE = "CREATE TABLE " + FavouriteMovieContract.MovieEntry.TABLE_NAME + "(" +
                FavouriteMovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                FavouriteMovieContract.MovieEntry.COLUMN_TMDB_ID + " INTEGER UNIQUE NOT NULL, " +
                FavouriteMovieContract.MovieEntry.COLUMN_TITLE + " STRING NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + FavouriteMovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
