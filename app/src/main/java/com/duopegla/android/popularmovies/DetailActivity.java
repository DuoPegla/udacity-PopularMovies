package com.duopegla.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.duopegla.android.popularmovies.utilities.NetworkUtilities;
import com.duopegla.android.popularmovies.utilities.TheMovieDbJsonUtilities;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by duopegla on 11.2.2017..
 */

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler
{
    private Movie mMovie;
    
    private TextView mMovieDetailTitle;
    private ImageView mMovieDetailPoster;
    private TextView mMovieDetailReleaseDate;
    private TextView mMovieDetailUserRating;
    private TextView mMovieDetailSynopsis;

    private RecyclerView mMovieDetailTrailers;
    private TrailerAdapter mTrailerAdapter;

    public class FetchMovieTrailerTask extends AsyncTask<URL, Void, Trailer[]>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Trailer[] doInBackground(URL... params) {
            if (params.length == 0)
            {
                return null;
            }

            try
            {
                String trailerJson = NetworkUtilities.getResponseFromHttpUrl(params[0]);
                return TheMovieDbJsonUtilities.getTrailersFromJson(trailerJson);
            }
            catch (IOException e)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Trailer[] trailers) {
            if (trailers == null)
                return;

            mTrailerAdapter.setTrailerData(trailers);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMovieDetailTitle = (TextView) findViewById(R.id.tv_detail_title);
        mMovieDetailPoster = (ImageView) findViewById(R.id.iv_detail_poster);
        mMovieDetailReleaseDate = (TextView) findViewById(R.id.tv_detail_release_date);
        mMovieDetailUserRating = (TextView) findViewById(R.id.tv_detail_user_rating);
        mMovieDetailSynopsis = (TextView) findViewById(R.id.tv_detail_synopsis);

        mMovieDetailTrailers = (RecyclerView) findViewById(R.id.recyclerview_trailers);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMovieDetailTrailers.setLayoutManager(layoutManager);
        mMovieDetailTrailers.setHasFixedSize(false);

        mTrailerAdapter = new TrailerAdapter(this);
        mMovieDetailTrailers.setAdapter(mTrailerAdapter);


        Intent parentIntent = getIntent();
        if (parentIntent != null && parentIntent.hasExtra(MainActivity.INTENT_EXTRA_KEY))
        {
            mMovie = parentIntent.getExtras().getParcelable(MainActivity.INTENT_EXTRA_KEY);
            mMovieDetailTitle.setText(mMovie.getOriginalTitle());
            Picasso.with(this).load(NetworkUtilities.buildPosterRequestUrl(mMovie.getPosterPath()).toString()).into(mMovieDetailPoster);
            mMovieDetailReleaseDate.setText(String.valueOf(mMovie.getReleaseDate().get(Calendar.YEAR)));
            mMovieDetailUserRating.setText(String.valueOf(mMovie.getUserRating()) + "/10");
            mMovieDetailSynopsis.setText(mMovie.getSynopsis());

            String apiKey = getString(R.string.themoviedb_api_key);
            URL request = NetworkUtilities.buildTrailerRequestUrl(apiKey, mMovie.getId());
            new FetchMovieTrailerTask().execute(request);
        }
    }

    @Override
    public void onClick(Trailer trailer)
    {
        if (!trailer.isYouTubeTrailer())
        {
            Toast.makeText(this, "Trailer " + trailer.getName() + " is not a YouTube video.", Toast.LENGTH_LONG).show();
            return;
        }

        Uri trailerUri = Uri.parse(NetworkUtilities.buildYouTubeTrailerUrl(trailer).toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, trailerUri);

        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
    }
}
