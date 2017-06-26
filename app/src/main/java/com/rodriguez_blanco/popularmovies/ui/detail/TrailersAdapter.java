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
import com.rodriguez_blanco.popularmovies.domain.Video;

import java.util.List;

public class TrailersAdapter
        extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {
    private List<Video> mTrailersData;

    private TrailersAdapterOnClickHandler mClickHandler;

    interface TrailersAdapterOnClickHandler {
        void onClick(String videoKey);
    }

    public TrailersAdapter(TrailersAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public TrailersAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForMovieListItem = R.layout.videos_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForMovieListItem, viewGroup, shouldAttachToParentImmediately);

        return new TrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersAdapterViewHolder holder, int position) {
        Video video = mTrailersData.get(position);
        String videoName = video.getName();

        holder.videoNameTextView.setText(videoName);
    }

    @Override
    public int getItemCount() {
        if (null == mTrailersData) return 0;
        return mTrailersData.size();
    }

    public void setTrailersData(List<Video> moviesData) {
        mTrailersData = moviesData;
        notifyDataSetChanged();
    }


    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        final TextView videoNameTextView;

        public TrailersAdapterViewHolder(View itemView) {
            super(itemView);

            videoNameTextView = (TextView) itemView.findViewById(R.id.tv_video_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            Video video = mTrailersData.get(adapterPosition);
            String videoKey = video.getKey();

            mClickHandler.onClick(videoKey);
        }
    }
}
