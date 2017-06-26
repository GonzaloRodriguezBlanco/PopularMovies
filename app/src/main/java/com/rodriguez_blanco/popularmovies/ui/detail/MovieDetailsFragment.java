/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.ui.detail;

import android.arch.lifecycle.LifecycleFragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rodriguez_blanco.popularmovies.R;
import com.rodriguez_blanco.popularmovies.api.TheMovieDbWebservice;
import com.rodriguez_blanco.popularmovies.data.PopularMoviesContract.FavoriteEntry;
import com.rodriguez_blanco.popularmovies.domain.Movie;
import com.rodriguez_blanco.popularmovies.domain.Review;
import com.rodriguez_blanco.popularmovies.domain.Video;
import com.rodriguez_blanco.popularmovies.util.NetworkUtil;
import com.rodriguez_blanco.popularmovies.viewmodel.MovieDetailViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

import static android.content.ContentValues.TAG;

public class MovieDetailsFragment extends LifecycleFragment
        implements TrailersAdapter.TrailersAdapterOnClickHandler {
    private static final String PARAM_MOVIE_ID = "param_movie_id";
    private static final String PARAM_MOVIE_TITLE = "param_movie_title";
    private static final String PARAM_POSTER_PATH = "param_poster_path";

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

    @BindView(R.id.recyclerview_trailers)
    RecyclerView mTrailersRecyclerView;
    private TrailersAdapter mTrailersAdapter;

    @BindView(R.id.recyclerview_reviews)
    RecyclerView mReviewsRecyclerView;
    private ReviewsAdapter mReviewsAdapter;

    @BindView(R.id.bt_favorite)
    ImageButton mFavoriteButton;

    Unbinder unbinder;

    boolean hasMovieDetails = false;
    boolean hasMovieTrailers = false;
    boolean hasMovieReviews = false;

    boolean mIsFavorite = false;

    public MovieDetailsFragment() {
        setRetainInstance(true);
    }

    public static MovieDetailsFragment forMovie(String movieId, String movieTitle, String posterPath) {
        final MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        final Bundle arguments = new Bundle();
        arguments.putString(PARAM_MOVIE_ID, movieId);
        arguments.putString(PARAM_MOVIE_TITLE, movieTitle);
        arguments.putString(PARAM_POSTER_PATH, posterPath);
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
        setupTrailersView();
        setupReviewsView();
        loadMovie();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMovie();
    }

    private void setupTrailersView() {
        Context context = getActivity();
        int orientation = LinearLayoutManager.VERTICAL;
        boolean isReverseLayout = false;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, orientation, isReverseLayout);

        mTrailersRecyclerView.setLayoutManager(linearLayoutManager);
        mTrailersRecyclerView.setHasFixedSize(true);
        mTrailersRecyclerView.setNestedScrollingEnabled(false);
        mTrailersAdapter = new TrailersAdapter(this);
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);
    }

    private void setupReviewsView() {
        Context context = getActivity();
        int orientation = LinearLayoutManager.VERTICAL;
        boolean isReverseLayout = false;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, orientation, isReverseLayout);

        mReviewsRecyclerView.setLayoutManager(linearLayoutManager);
        mReviewsRecyclerView.setHasFixedSize(true);
        mReviewsRecyclerView.setNestedScrollingEnabled(false);
        mReviewsAdapter = new ReviewsAdapter();
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
    }

    private void loadMovie() {
        Context context = getActivity();
        if (!NetworkUtil.isNetworkAvailable(context)) {
            showConnectionError();
        } else if (TheMovieDbWebservice.API_KEY_V3.equals("CHANGE_ME")){
            showApiKeyError();
        } else {
            hasMovieDetails = false;
            hasMovieTrailers = false;
            hasMovieReviews = false;
            showLoadingIndicator();
            String movieId = getParamMovieId();

            mViewModel.getMovie(movieId).observe(this, movie -> {
                if (movie == null) {
                    showUnknownError();
                } else {
                    hasMovieDetails = true;
                    showMovieInView(movie);
                    if (hasMovieDetails && hasMovieTrailers && hasMovieReviews) {
                        hideLoadingIndicator();
                    }
                }

            });

            mViewModel.getVideos(movieId).observe(this, videos -> {
                if (videos == null) {
                    showUnknownError();
                } else {
                    hasMovieTrailers = true;
                    showVideosInView(videos);
                    if (hasMovieDetails && hasMovieTrailers && hasMovieReviews) {
                        hideLoadingIndicator();
                    }
                }

            });

            mViewModel.getReviews(movieId).observe(this, reviews -> {
                if (reviews == null) {
                    showUnknownError();
                } else {
                    hasMovieReviews = true;
                    showReviewsInView(reviews);
                    if (hasMovieDetails && hasMovieTrailers && hasMovieReviews) {
                        hideLoadingIndicator();
                    }
                }

            });

            CheckFavoriteTask checkFavoriteTask = new CheckFavoriteTask();
            checkFavoriteTask.execute(movieId);
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

        Picasso.with(context)
                .load(posterThumbnail)
                .placeholder(R.drawable.ic_photo)
                .into(mPosterThumbnailImageView);

    }

    private void showVideosInView(List<Video> videos) {
        mTrailersAdapter.setTrailersData(videos);
        Timber.d("Videos: %s", new Gson().toJsonTree(videos).toString());
    }

    private void showReviewsInView(List<Review> reviews) {
        mReviewsAdapter.setReviewsData(reviews);
        Timber.d("Reviews: %s", new Gson().toJsonTree(reviews).toString());

    }

    private String getParamMovieId(){
        Bundle arguments = getArguments();
        return arguments.getString(PARAM_MOVIE_ID);
    }

    private String getParamMovieTitle(){
        Bundle arguments = getArguments();
        return arguments.getString(PARAM_MOVIE_TITLE);
    }

    private String getParamPosterPath(){
        Bundle arguments = getArguments();
        return arguments.getString(PARAM_POSTER_PATH);
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

    private void showFavoriteButton() {
        mFavoriteButton.setVisibility(View.VISIBLE);
    }

    private void hideFavoriteButton() {
        mFavoriteButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(String videoKey) {
        Timber.d("Video key: %s", videoKey);
        openVideo(videoKey);
    }

    private void openVideo(String videoKey) {
        Uri videoUri = Uri.parse("https://www.youtube.com/watch?v=" + videoKey);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(videoUri);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Timber.d("Couldn't call %s, no receiving apps installed!", videoUri.toString());
        }
    }

    class CheckFavoriteTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... movieIds) {
            try {
                return getContext().getContentResolver().query(FavoriteEntry.CONTENT_URI,
                        null,
                        FavoriteEntry.COLUMN_MOVIE_ID + "=?",
                        movieIds,
                        null).moveToNext();

            } catch (Exception e) {
                Log.e(TAG, "Failed to asynchronously load data.");
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isFavorite) {
            super.onPostExecute(isFavorite);
            mIsFavorite = isFavorite;
            updateFavoriteButtonInView();
        }
    }

    private void updateFavoriteButtonInView() {
        Drawable favoriteDrawable;
        int resId;
        if (mIsFavorite) {
            resId = R.drawable.ic_action_favorite_enable;
        } else {
            resId = R.drawable.ic_action_favorite_disable;

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            favoriteDrawable = getResources().getDrawable(resId, null);
        } else {
            favoriteDrawable = getResources().getDrawable(resId);
        }

        mFavoriteButton.setImageDrawable(favoriteDrawable);

        showFavoriteButton();
    }

    @OnClick(R.id.bt_favorite)
    public void onFavoriteClick() {
        ContentResolver contentResolver = getContext().getContentResolver();
        if (mIsFavorite) {
            int favoritesDeletedCount = contentResolver.delete(FavoriteEntry.CONTENT_URI.buildUpon().appendPath(getParamMovieId()).build(),null, null);

            if (favoritesDeletedCount == 1) {
                mIsFavorite = false;
                updateFavoriteButtonInView();
            }
        } else {
            // insert
            ContentValues contentValues = new ContentValues();

            contentValues.put(FavoriteEntry.COLUMN_MOVIE_ID, getParamMovieId());
            contentValues.put(FavoriteEntry.COLUMN_POSTER_PATH, getParamPosterPath());
            contentValues.put(FavoriteEntry.COLUMN_MOVIE_TITLE, getParamMovieTitle());

            Uri uri = contentResolver.insert(FavoriteEntry.CONTENT_URI, contentValues);

            if(uri != null) {
                mIsFavorite = true;
                updateFavoriteButtonInView();
            }
        }
    }
}
