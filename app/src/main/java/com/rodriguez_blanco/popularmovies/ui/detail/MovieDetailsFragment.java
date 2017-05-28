/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.ui.detail;

import android.arch.lifecycle.LifecycleFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rodriguez_blanco.popularmovies.R;
import com.rodriguez_blanco.popularmovies.api.TheMovieDbWebservice;
import com.rodriguez_blanco.popularmovies.domain.Movie;
import com.rodriguez_blanco.popularmovies.util.NetworkUtil;
import com.rodriguez_blanco.popularmovies.viewmodel.MovieDetailViewModel;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

public class MovieDetailsFragment extends LifecycleFragment {
    private static final String PARAM_MOVIE_ID = "param_movie_id";
    @Inject
    MovieDetailViewModel mViewModel;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.ll_error)
    View mErrorLayout;

    @BindView(R.id.tv_error_message)
    TextView mErrorMessageTextView;

    @BindView(R.id.tv_original_title)
    TextView mOriginalTitleTextView;

    @BindView(R.id.iv_movie_poster_thumbnail)
    ImageView mPosterThumbnailImageView;

    @BindView(R.id.tv_release_date)
    TextView mReleaseDateTextView;

    @BindView(R.id.tv_user_rating)
    TextView mUserRatingTextView;

    @BindView(R.id.tv_plot_synopsis)
    TextView mSynopsisTextView;

    Unbinder unbinder;

    public MovieDetailsFragment() {
        setRetainInstance(true);
    }

    public static MovieDetailsFragment forMovie(String movieId) {
        final MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        final Bundle arguments = new Bundle();
        arguments.putString(PARAM_MOVIE_ID, movieId);
        movieDetailsFragment.setArguments(arguments);
        return movieDetailsFragment;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadMovie();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMovie();
    }

    private void loadMovie() {
        Context context = getActivity();
        if (!NetworkUtil.isNetworkAvailable(context)) {
            showConnectionError();
        } else if (TheMovieDbWebservice.API_KEY_V3.equals("CHANGE_ME")){
            showApiKeyError();
        } else {
            showLoadingIndicator();
            String movieId = getParamMovieId();
            mViewModel.getMovie(movieId).observe(this, movie -> {
                if (movie == null) {
                    showUnknownError();
                } else {
                    showMovieInView(movie);
                    hideLoadingIndicator();
                }

            });
        }
    }

    private void showMovieInView(Movie movie) {
        mOriginalTitleTextView.setText(movie.getOriginalTitle());
//        mPosterThumbnailImageView
        String realeDateYear = movie.getReleaseDate().substring(0, 4);
        mReleaseDateTextView.setText(realeDateYear);
        String maxRatingPoints = "10";
        mUserRatingTextView.setText(movie.getVoteAverage() + " / " + maxRatingPoints);
        mSynopsisTextView.setText(movie.getOverview());
        String posterPath = movie.getPosterPath();

        Uri posterThumbnail = Uri.parse(TheMovieDbWebservice.IMAGES_BASE_URL).buildUpon()
                .appendPath(TheMovieDbWebservice.DEFAULT_THUMB_FILE_SIZE)
                .appendPath(posterPath.replace("/", ""))
                .build();

        Context context = getActivity();

        Picasso.with(context).load(posterThumbnail).into(mPosterThumbnailImageView);

    }

    private String getParamMovieId(){
        Bundle arguments = getArguments();
        return arguments.getString(PARAM_MOVIE_ID);
    }

    private void showUnknownError() {
        hideLoadingIndicator();
        mErrorMessageTextView.setText(R.string.error_message_unknown_error);
        showErrorDisplay();
    }

    private void showConnectionError() {
        hideLoadingIndicator();
        mErrorMessageTextView.setText(R.string.error_message_connection_unavailable);
        showErrorDisplay();
    }

    private void showApiKeyError() {
        hideLoadingIndicator();
        mErrorMessageTextView.setText(R.string.error_message_api_key);
        showErrorDisplay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.bt_retry)
    void onRetry() {
        hideErrorDisplay();
        loadMovie();
    }

    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void showErrorDisplay() {
        mErrorLayout.setVisibility(View.VISIBLE);
    }

    private void hideErrorDisplay() {
        mErrorLayout.setVisibility(View.INVISIBLE);
    }

}
