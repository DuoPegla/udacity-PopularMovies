package com.duopegla.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by duopegla on 25.3.2017..
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder>
{
    private Trailer[] mTrailerData;

    private final TrailerAdapterOnClickHandler mClickHandler;
    public interface TrailerAdapterOnClickHandler
    {
        void onClick(Trailer trailer);
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final TextView mTrailerNameTextView;

        public TrailerAdapterViewHolder(View view)
        {
            super(view);
            mTrailerNameTextView = (TextView) view.findViewById(R.id.tv_trailer_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int adapterPosition = getAdapterPosition();
            Trailer trailer = mTrailerData[adapterPosition];
            mClickHandler.onClick(trailer);
        }
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler)
    {
        mClickHandler = clickHandler;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position)
    {
        Log.d("RV", "Refresh " + String.valueOf(position));
        Trailer trailer = mTrailerData[position];
        holder.mTrailerNameTextView.setText(trailer.getName());
    }

    @Override
    public int getItemCount()
    {
        if (mTrailerData == null)
            return 0;

        return mTrailerData.length;
    }

    public void setTrailerData(Trailer[] trailerData)
    {
        mTrailerData = trailerData;
        notifyDataSetChanged();
    }
}
