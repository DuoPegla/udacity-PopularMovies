package com.duopegla.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.duopegla.android.popularmovies.utilities.NetworkUtilities;
import com.duopegla.android.popularmovies.utilities.TheMovieDbJsonUtilities;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private TextView mTextView;
    private ProgressBar mLoadingIndicator;

    private NetworkUtilities.MovieResultSort movieResultSortMethod;

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
                StringBuilder resultStringBuilder = new StringBuilder();

                for (int i = 0; i < movies.length; i++)
                {
                    resultStringBuilder.append(movies[i].toString()).append("\n\n\n");
                }

                mTextView.setText(resultStringBuilder.toString());

                Log.d("POSTER", NetworkUtilities.buildPosterRequestUrl(movies[0].getPosterPath()).toString());
                Picasso.with(getBaseContext()).load(NetworkUtilities.buildPosterRequestUrl(movies[0].getPosterPath()).toString()).into(mImageView);

                Log.d("LOG", "Received result for " + movies.length + " movies.");
            }
            else
            {
                Log.d("ERROR", "Returned null result");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("KEY", getResources().getString(R.string.themoviedb_api_key));

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mImageView = (ImageView) findViewById(R.id.iv_image_test);
        //Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").into(mImageView);

        mTextView = (TextView) findViewById(R.id.tv_movie_test);

        movieResultSortMethod = NetworkUtilities.MovieResultSort.MOST_POPULAR;
        LoadMovieData(movieResultSortMethod);
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
            mTextView.setText("");
            LoadMovieData(movieResultSortMethod);

            return true;
        }

        if (selectedId == R.id.action_sort_most_popular)
        {
            mTextView.setText("");
            movieResultSortMethod = NetworkUtilities.MovieResultSort.MOST_POPULAR;
            LoadMovieData(movieResultSortMethod);

            return true;
        }

        if (selectedId == R.id.action_sort_top_rated)
        {
            mTextView.setText("");
            movieResultSortMethod = NetworkUtilities.MovieResultSort.TOP_RATED;
            LoadMovieData(movieResultSortMethod);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void LoadMovieData(NetworkUtilities.MovieResultSort sortBy)
    {
        String apiKey = getResources().getString(R.string.themoviedb_api_key);

        URL request = NetworkUtilities.buildRequestUrl(apiKey, sortBy);

        new FetchMovieDataTask().execute(request);
    }
}
