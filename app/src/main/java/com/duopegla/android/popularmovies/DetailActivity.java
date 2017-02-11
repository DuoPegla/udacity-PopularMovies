package com.duopegla.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.duopegla.android.popularmovies.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

/**
 * Created by duopegla on 11.2.2017..
 */

public class DetailActivity extends AppCompatActivity
{
    private Movie mMovie;
    
    private TextView mMovieDetailsTitle;
    private ImageView mMovieDetailsPoster;
    private TextView mMovieDetailReleaseDate;
    private TextView mMovieDetailUserRating;
    private TextView mMovieDetailSynopsis;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMovieDetailsTitle = (TextView) findViewById(R.id.tv_detail_title);
        mMovieDetailsPoster = (ImageView) findViewById(R.id.iv_detail_poster);
        mMovieDetailReleaseDate = (TextView) findViewById(R.id.tv_detail_release_date);
        mMovieDetailUserRating = (TextView) findViewById(R.id.tv_detail_user_rating);
        mMovieDetailSynopsis = (TextView) findViewById(R.id.tv_detail_synopsis);

        Intent parentIntent = getIntent();
        if (parentIntent != null)
        {
            if (parentIntent.hasExtra("movie"))
            {

                mMovie = parentIntent.getExtras().getParcelable("movie");
                mMovieDetailsTitle.setText(mMovie.getOriginalTitle());
                Picasso.with(this).load(NetworkUtilities.buildPosterRequestUrl(mMovie.getPosterPath()).toString()).into(mMovieDetailsPoster);
                mMovieDetailReleaseDate.setText(String.valueOf(mMovie.getReleaseDate().get(Calendar.YEAR)));
                mMovieDetailUserRating.setText(String.valueOf(mMovie.getUserRating()) + "/10");
                mMovieDetailSynopsis.setText(mMovie.getSynopsis());
            }
        }
    }
}
