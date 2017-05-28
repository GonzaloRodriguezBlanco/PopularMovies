/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rodriguez_blanco.popularmovies.R;
import com.rodriguez_blanco.popularmovies.api.TheMovieDbWebservice;
import com.rodriguez_blanco.popularmovies.util.NetworkUtil;
import com.rodriguez_blanco.popularmovies.util.ScreenUtil;
import com.rodriguez_blanco.popularmovies.viewmodel.MovieListViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListFragment extends LifecycleFragment implements MovieListAdapter.MovieListAdapterOnClickHandler {
    private static final int DEFAULT_SPAN_COUNT = 2;
    @Inject
    MovieListViewModel mViewModel;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.ll_error)
    View mErrorLayout;

    @BindView(R.id.tv_error_message)
    TextView mErrorMessageTextView;

    @BindView(R.id.recyclerview_movies)
    RecyclerView mMovieListRecyclerView;
    private MovieListAdapter mMovieListAdapter;

    Unbinder unbinder;

    int spanCount = DEFAULT_SPAN_COUNT;

    public MovieListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeColumnCountForGrid();
        setHasOptionsMenu(true);
    }

    private void initializeColumnCountForGrid() {
        final String FILE_SIZE_PREFIX = "w";
        int screenWidth = ScreenUtil.getScreenWidthInPixels();
        int posterWidth = Integer.valueOf(TheMovieDbWebservice.DEFAULT_IMAGE_FILE_SIZE.replace(FILE_SIZE_PREFIX,""));

        spanCount = (screenWidth / posterWidth) + 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort_order_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        switch (itemThatWasClickedId) {
            case R.id.action_most_popular:
                showLoadingIndicator();
                mViewModel.getPopularMovies();
                Toast.makeText(getActivity(), "Most popular", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.action_top_rated:
                showLoadingIndicator();
                mViewModel.getTopRatedMovies();
                Toast.makeText(getActivity(), "Top rated", Toast.LENGTH_SHORT).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupRecyclerView();
        loadMovies();
    }

    private void loadMovies() {
        Context context = getActivity();
        if (!NetworkUtil.isNetworkAvailable(context)) {
            showConnectionError();
        } else if (TheMovieDbWebservice.API_KEY_V3.equals("CHANGE_ME")){
            showApiKeyError();
        } else {
            showLoadingIndicator();
            mViewModel.init();
            mViewModel.getMovies().observe(this, movies -> {
                if (movies == null) {
                    showUnknownError();
                } else {
                    mMovieListAdapter.setMoviesData(movies);
                    hideLoadingIndicator();
                }

            });
        }
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

    private void setupRecyclerView() {
        Context context = getActivity();
        int orientation = GridLayoutManager.VERTICAL;
        boolean isReverseLayout = false;

        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(context, spanCount, orientation, isReverseLayout);
        mMovieListRecyclerView.setLayoutManager(gridLayoutManager);
        mMovieListRecyclerView.setHasFixedSize(true);
        mMovieListAdapter = new MovieListAdapter(this);
        mMovieListRecyclerView.setAdapter(mMovieListAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.bt_retry)
    void onRetry() {
        hideErrorDisplay();
        loadMovies();
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

    @Override
    public void onClick(String movieId) {
        Timber.d("Movie Id: %s", movieId);
    }

}
