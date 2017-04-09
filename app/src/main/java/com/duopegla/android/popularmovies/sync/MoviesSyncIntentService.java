package com.duopegla.android.popularmovies.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by duopegla on 8.4.2017..
 */

public class MoviesSyncIntentService extends IntentService
{
    public MoviesSyncIntentService()
    {
        super("MoviesSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        MoviesSyncTask.syncMovies(this);
    }
}
