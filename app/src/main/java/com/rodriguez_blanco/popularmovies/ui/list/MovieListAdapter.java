/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.ui.list;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rodriguez_blanco.popularmovies.R;
import com.rodriguez_blanco.popularmovies.api.TheMovieDbWebservice;
import com.rodriguez_blanco.popularmovies.domain.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter
        extends RecyclerView.Adapter<MovieListAdapter.MovieListAdapterViewHolder> {
    private List<Movie> mMoviesData;

    private MovieListAdapterOnClickHandler mClickHandler;

    interface MovieListAdapterOnClickHandler {
        void onClick(String movieId);
    }

    public MovieListAdapter(MovieListAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public MovieListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForMovieListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForMovieListItem, viewGroup, shouldAttachToParentImmediately);

        return new MovieListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieListAdapterViewHolder holder, int position) {
        ImageView imageView = (ImageView) holder.itemView;
        Context context = imageView.getContext();

        Movie movie = mMoviesData.get(position);
        String posterPath = movie.getPosterPath();
        
        Uri poster = Uri.parse(TheMovieDbWebservice.IMAGES_BASE_URL).buildUpon()
                .appendPath(TheMovieDbWebservice.DEFAULT_IMAGE_FILE_SIZE)
                .appendPath(posterPath.replace("/", ""))
                .build();

        Picasso.with(context).load(poster).into(imageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMoviesData) return 0;
        return mMoviesData.size();
    }

    public void setMoviesData(List<Movie> moviesData) {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }


    public class MovieListAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        public MovieListAdapterViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            Movie movie = mMoviesData.get(adapterPosition);
            String movieId = movie.getId();

            mClickHandler.onClick(movieId);
        }
    }
}
