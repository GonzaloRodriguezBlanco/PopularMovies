/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.ui.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rodriguez_blanco.popularmovies.R;
import com.rodriguez_blanco.popularmovies.domain.Review;

import java.util.List;

public class ReviewsAdapter
        extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {
    private List<Review> mReviewsData;

    public ReviewsAdapter() {
        super();
    }

    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForMovieListItem = R.layout.reviews_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForMovieListItem, viewGroup, shouldAttachToParentImmediately);

        return new ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapterViewHolder holder, int position) {
        Review review = mReviewsData.get(position);

        String author = review.getAuthor();
        String content = review.getContent();

        holder.authorTextView.setText(author);
        holder.contentTextView.setText(content);
    }

    @Override
    public int getItemCount() {
        if (null == mReviewsData) return 0;
        return mReviewsData.size();
    }

    public void setReviewsData(List<Review> reviewsData) {
        mReviewsData = reviewsData;
        notifyDataSetChanged();
    }


    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView authorTextView;
        final TextView contentTextView;

        public ReviewsAdapterViewHolder(View itemView) {
            super(itemView);

            authorTextView = (TextView) itemView.findViewById(R.id.tv_author);
            contentTextView = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}
