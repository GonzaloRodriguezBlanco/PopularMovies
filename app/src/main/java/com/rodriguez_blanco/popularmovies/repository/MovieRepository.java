/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.rodriguez_blanco.popularmovies.api.GetPopularMoviesResponse;
import com.rodriguez_blanco.popularmovies.api.TheMovieDbWebservice;
import com.rodriguez_blanco.popularmovies.domain.Movie;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MovieRepository {
    private TheMovieDbWebservice mTheMovieDbWebservice;
    private int page = 1;

    final MutableLiveData<List<Movie>> data = new MutableLiveData<>();

    @Inject
    public MovieRepository(TheMovieDbWebservice theMovieDbWebservice) {
        this.mTheMovieDbWebservice = theMovieDbWebservice;
    }

    public LiveData<List<Movie>> getPopularMovies() {
        mTheMovieDbWebservice
                .getPopularMovies(TheMovieDbWebservice.API_KEY_V3,
                        null,
                        page,
                        null)
                .enqueue(getMoviesCallback);
        return data;
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        mTheMovieDbWebservice
                .getTopRatedMovies(TheMovieDbWebservice.API_KEY_V3,
                        null,
                        page,
                        null)
                .enqueue(getMoviesCallback);
        return data;
    }

    Callback getMoviesCallback = new Callback<GetPopularMoviesResponse>() {
        @Override
        public void onResponse(Call<GetPopularMoviesResponse> call, Response<GetPopularMoviesResponse> response) {
            boolean isSuccesful = response.isSuccessful();
            int code = response.code();
            if (isSuccesful){
                GetPopularMoviesResponse getPopularMoviesResponse = response.body();

                if (getPopularMoviesResponse != null) {
                    page = getPopularMoviesResponse.getPage();
                }
                List<Movie> movies = getPopularMoviesResponse.getResults();
                data.setValue(movies);
            }
        }

        @Override
        public void onFailure(Call<GetPopularMoviesResponse> call, Throwable t) {
            Timber.e(t, t.getMessage());
            data.setValue(null);
        }
    };
}
