package com.duopegla.android.popularmovies.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by duopegla on 8.4.2017..
 */

public class MoviesFirebaseJobService extends JobService
{
    private AsyncTask<Void, Void, Void> mFetchMoviesTask;

    @Override
    public boolean onStartJob(final JobParameters job)
    {
        mFetchMoviesTask = new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                Context context = getApplicationContext();
                MoviesSyncTask.syncMovies(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                jobFinished(job, false);
            }
        };

        mFetchMoviesTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job)
    {
        if (mFetchMoviesTask != null)
        {
            mFetchMoviesTask.cancel(true);
        }

        return true;
    }
}
