package com.duopegla.android.popularmovies;

import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by duopegla on 8.2.2017..
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>
{
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements DialogInterface.OnClickListener
    {
        public MovieAdapterViewHolder(View view)
        {
            super(view);
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
