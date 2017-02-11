package com.duopegla.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.duopegla.android.popularmovies.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

/**
 * Created by duopegla on 8.2.2017..
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>
{
    private Movie[] mMovieData;

    private final MovieAdapterOnClickHandler mClickHandler;
    public interface MovieAdapterOnClickHandler
    {
        void onClick(Movie movie);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener
    {
        public final ImageView mMoviePosterImageView;

        public MovieAdapterViewHolder(View view)
        {
            super(view);
            mMoviePosterImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovieData[adapterPosition];
            mClickHandler.onClick(movie);
        }
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler)
    {
        mClickHandler = clickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        int layoutIdForGridItem = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForGridItem, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position)
    {
        Movie movie = mMovieData[position];
        Picasso.with(holder.mMoviePosterImageView.getContext()).
                load(NetworkUtilities.buildPosterRequestUrl(movie.getPosterPath()).toString()).
                into(holder.mMoviePosterImageView);
    }

    @Override
    public int getItemCount()
    {
        if (mMovieData == null)
            return 0;

        return mMovieData.length;
    }

    public void setMovieData(Movie[] movieData)
    {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
