package com.duopegla.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.duopegla.android.popularmovies.data.MovieContract;
import com.duopegla.android.popularmovies.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

/**
 * Created by duopegla on 8.2.2017..
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>
{
    private final Context mContext;
    private Cursor mCursor;

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
            mCursor.moveToPosition(adapterPosition);
            Movie movie = new Movie(mCursor);
            mClickHandler.onClick(movie);
        }
    }

    public MovieAdapter(@NonNull Context context, MovieAdapterOnClickHandler clickHandler)
    {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        int layoutIdForGridItem = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForGridItem, parent, false);

        view.setFocusable(true);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position)
    {
        mCursor.moveToPosition(position);

        Movie movie = new Movie(mCursor);
        Picasso.with(holder.mMoviePosterImageView.getContext()).
                load(NetworkUtilities.buildPosterRequestUrl(movie.getPosterPath()).toString()).
                into(holder.mMoviePosterImageView);
    }

    @Override
    public int getItemCount()
    {
        if (mCursor == null)
            return 0;

        return mCursor.getCount();
    }

//    public void setMovieData(Movie[] movieData)
//    {
//        mCursor = movieData;
//        notifyDataSetChanged();
//    }
    public void swapCursor(Cursor newCursor)
    {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

//    public void setMovieFavorites(Cursor movieFavorites)
//    {
//        if (movieFavorites == null || movieFavorites.getCount() == 0)
//            return;
//
//        while (movieFavorites.moveToNext())
//        {
//            int tmdbId = movieFavorites.getInt(movieFavorites.getColumnIndex(MovieContract.MovieEntry.COLUMN_TMDB_ID));
//            for (Movie m : mCursor)
//            {
//                if (tmdbId == m.getId())
//                {
//                    m.setIsFavorite(true);
//                    Log.d("MOVIE", "Movie with id " + m.getId() + " is favorite.");
//                    break;
//                }
//            }
//        }
//    }
}
