package com.duopegla.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.duopegla.android.popularmovies.data.MovieContract;
import com.duopegla.android.popularmovies.sync.MoviesSyncUtils;
import com.duopegla.android.popularmovies.utilities.NetworkUtilities;
import com.duopegla.android.popularmovies.utilities.TheMovieDbJsonUtilities;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>
{
    private final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private int mRecyclerViewPosition = RecyclerView.NO_POSITION;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private NetworkUtilities.MovieResultSort movieResultSortMethod;

    public final int NUMBER_OF_COLUMNS = 2;
    public static final String INTENT_EXTRA_KEY = "movie";

    private static final int MOVIE_LOADER_ID = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);


        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        movieResultSortMethod = NetworkUtilities.MovieResultSort.MOST_POPULAR;

        showLoading();

        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);

        MoviesSyncUtils.initialize(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int selectedId = item.getItemId();

        if (selectedId == R.id.action_refresh)
        {
            MoviesSyncUtils.startImmediateSync(this);
            mMovieAdapter.swapCursor(null);
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            showLoading();

            return true;
        }

        if (selectedId == R.id.action_sort_most_popular)
        {
            movieResultSortMethod = NetworkUtilities.MovieResultSort.MOST_POPULAR;
            mMovieAdapter.swapCursor(null);
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            showLoading();

            return true;
        }

        if (selectedId == R.id.action_sort_top_rated)
        {
            movieResultSortMethod = NetworkUtilities.MovieResultSort.TOP_RATED;
            mMovieAdapter.swapCursor(null);
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            showLoading();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movie movie)
    {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(INTENT_EXTRA_KEY, movie);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case MOVIE_LOADER_ID:
                Uri movieQueryUri = MovieContract.MovieEntry.CONTENT_URI;
                String selection;

                if (movieResultSortMethod == NetworkUtilities.MovieResultSort.MOST_POPULAR)
                {
                    selection = MovieContract.MovieEntry.COLUMN_IS_MOST_POPULAR + "= 1";
                }
                else if (movieResultSortMethod == NetworkUtilities.MovieResultSort.TOP_RATED)
                {
                    selection = MovieContract.MovieEntry.COLUMN_IS_TOP_RATED + "= 1";
                }
                else
                {
                    selection = MovieContract.MovieEntry.COLUMN_IS_FAVORITE + "= 1";
                }

                return new CursorLoader(this,
                        movieQueryUri,
                        null,
                        selection,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        mMovieAdapter.swapCursor(data);
        if (mRecyclerViewPosition == RecyclerView.NO_POSITION)
            mRecyclerViewPosition = 0;

        mRecyclerView.smoothScrollToPosition(mRecyclerViewPosition);
        if (data.getCount() != 0)
            showMovieDataView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        mMovieAdapter.swapCursor(null);
    }

    private void showMovieDataView()
    {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading()
    {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
}
