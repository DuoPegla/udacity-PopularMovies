package com.duopegla.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by duopegla on 1.4.2017..
 */

public class MovieDbHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "moviesDb.db";
    private static final int VERSION = 1;

    MovieDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String CREATE_TABLE =
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "(" +
                MovieContract.MovieEntry._ID                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.COLUMN_TMDB_ID         + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE  + " STRING NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POSTER_PATH     + " STRING NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_SYNOPSIS        + " STRING NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_USER_RATING     + " REAL NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE    + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_IS_MOST_POPULAR + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_IS_TOP_RATED    + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_IS_FAVORITE     + " INTEGER NOT NULL, " +
                " UNIQUE (" + MovieContract.MovieEntry.COLUMN_TMDB_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
