package com.duopegla.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.duopegla.android.popularmovies.data.MovieContract;
import com.duopegla.android.popularmovies.utilities.NetworkUtilities;
import com.duopegla.android.popularmovies.utilities.TheMovieDbJsonUtilities;
import com.squareup.picasso.Picasso;

import java.io.IOException;
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

    private RecyclerView mMovieDetailReviews;
    private ReviewAdapter mReviewAdapter;

    private ImageButton mFavoriteMovieButton;

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

    public class FetchMoviewReviewTask extends AsyncTask<URL, Void, Review[]>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Review[] doInBackground(URL... params)
        {
            if (params.length == 0)
                return null;

            try
            {
                String reviewJson = NetworkUtilities.getResponseFromHttpUrl(params[0]);
                return TheMovieDbJsonUtilities.getReviewsFromJson(reviewJson);
            }
            catch (IOException e)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Review[] reviews)
        {
            if (reviews == null)
                return;

            mReviewAdapter.setReviewData(reviews);
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
        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMovieDetailTrailers.setLayoutManager(trailerLayoutManager);
        mMovieDetailTrailers.setHasFixedSize(false);
        mTrailerAdapter = new TrailerAdapter(this);
        mMovieDetailTrailers.setAdapter(mTrailerAdapter);

        mMovieDetailReviews = (RecyclerView) findViewById(R.id.recyclerview_reviews);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMovieDetailReviews.setLayoutManager(reviewLayoutManager);
        mMovieDetailReviews.setHasFixedSize(false);
        mReviewAdapter = new ReviewAdapter();
        mMovieDetailReviews.setAdapter(mReviewAdapter);

        mFavoriteMovieButton = (ImageButton) findViewById(R.id.action_favorite);

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
            URL trailerRequest = NetworkUtilities.buildTrailerRequestUrl(apiKey, mMovie.getId());
            new FetchMovieTrailerTask().execute(trailerRequest);

            URL reviewRequest = NetworkUtilities.buildReviewRequestUrl(apiKey, mMovie.getId());
            new FetchMoviewReviewTask().execute(reviewRequest);

            if (mMovie.getIsFavorite())
            {
                mFavoriteMovieButton.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_star_black_48dp));
            }
            else
            {
                mFavoriteMovieButton.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_star_border_black_48dp));
            }
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

    public void onClickFavourite(View view)
    {
        ImageButton button = (ImageButton) view;
        if (mMovie.getIsFavorite())
        {
            mMovie.setIsFavorite(false);
            button.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_star_border_black_48dp));
            Toast.makeText(this, "Removed from Favourites", Toast.LENGTH_SHORT).show();

            Uri uri = MovieContract.MovieEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(Integer.toString(mMovie.getId())).build();

            Log.d("REMOVE_FAVORITE_URI", uri.toString());

            getContentResolver().update(uri, mMovie.getContentValues(), null, null);
        }
        else
        {
            mMovie.setIsFavorite(true);
            button.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_star_black_48dp));
            Toast.makeText(this, "Added to Favourites", Toast.LENGTH_SHORT).show();

            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry.COLUMN_TMDB_ID, mMovie.getId());
            contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, mMovie.getOriginalTitle());

            Uri uri = MovieContract.MovieEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(Integer.toString(mMovie.getId())).build();

            getContentResolver().update(uri, mMovie.getContentValues(), null, null);
        }
    }
}
