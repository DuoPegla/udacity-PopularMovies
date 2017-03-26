package com.duopegla.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by duopegla on 26.3.2017..
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder>
{
    private Review[] mReviewData;

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView mReviewAuthorTextView;
        public final TextView mReviewContentTextView;

        public ReviewAdapterViewHolder(View view)
        {
            super(view);

            mReviewAuthorTextView = (TextView) view.findViewById(R.id.tv_review_author);
            mReviewContentTextView = (TextView) view.findViewById(R.id.tv_review_content);
        }
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position)
    {
        Log.d("REVIEW", "Refresh " + String.valueOf(position));
        Review review = mReviewData[position];
        holder.mReviewAuthorTextView.setText(review.getAuthor());
        holder.mReviewContentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount()
    {
        if (mReviewData == null)
            return 0;

        return mReviewData.length;
    }

    public void setReviewData(Review[] reviewData)
    {
        mReviewData = reviewData;
        notifyDataSetChanged();
    }
}
