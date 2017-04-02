package com.duopegla.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
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
import android.widget.Toast;


import com.duopegla.android.popularmovies.data.FavouriteMovieContract;
import com.duopegla.android.popularmovies.utilities.NetworkUtilities;
import com.duopegla.android.popularmovies.utilities.TheMovieDbJsonUtilities;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>
{

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private NetworkUtilities.MovieResultSort movieResultSortMethod;

    public final int NUMBER_OF_COLUMNS = 2;
    public static final String INTENT_EXTRA_KEY = "movie";

    private static final int FAVORITE_MOVIE_LOADER_ID = 0;

    public class FetchMovieDataTask extends AsyncTask<URL, Void, Movie[]>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(URL... params) {

            if (params.length == 0)
            {
                return null;
            }

            try
            {
                String moviesJson = NetworkUtilities.getResponseFromHttpUrl(params[0]);
                return TheMovieDbJsonUtilities.getMoviesFromJson(moviesJson);
            }
            catch (IOException e)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie movies[]) {

            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (movies != null)
            {
                showMovieDataView();
                mMovieAdapter.setMovieData(movies);
                getSupportLoaderManager().initLoader(FAVORITE_MOVIE_LOADER_ID, null, MainActivity.this);
            }
            else
            {
                showErrorMessage();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);


        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        movieResultSortMethod = NetworkUtilities.MovieResultSort.MOST_POPULAR;
        loadMovieData(movieResultSortMethod);
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
            mMovieAdapter.setMovieData(null);
            loadMovieData(movieResultSortMethod);

            return true;
        }

        if (selectedId == R.id.action_sort_most_popular)
        {
            mMovieAdapter.setMovieData(null);
            movieResultSortMethod = NetworkUtilities.MovieResultSort.MOST_POPULAR;
            loadMovieData(movieResultSortMethod);

            return true;
        }

        if (selectedId == R.id.action_sort_top_rated)
        {
            mMovieAdapter.setMovieData(null);
            movieResultSortMethod = NetworkUtilities.MovieResultSort.TOP_RATED;
            loadMovieData(movieResultSortMethod);

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
        return new AsyncTaskLoader<Cursor>(this)
        {
            Cursor mFavoriteMovieData = null;

            @Override
            protected void onStartLoading()
            {
                if (mFavoriteMovieData != null)
                {
                    deliverResult(mFavoriteMovieData);
                }
                else
                {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground()
            {
                try
                {
                    return getContentResolver().query(FavouriteMovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                }
                catch (Exception e)
                {
                    Log.e("DB", "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data)
            {
                mFavoriteMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        mMovieAdapter.setMovieFavorites(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void loadMovieData(NetworkUtilities.MovieResultSort sortBy)
    {
        showMovieDataView();

        String apiKey = getResources().getString(R.string.themoviedb_api_key);
        URL request = NetworkUtilities.buildRequestUrl(apiKey, sortBy);
        new FetchMovieDataTask().execute(request);
    }

    private void showMovieDataView()
    {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage()
    {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
}
