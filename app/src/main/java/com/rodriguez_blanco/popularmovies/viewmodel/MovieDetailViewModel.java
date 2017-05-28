/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.rodriguez_blanco.popularmovies.domain.Movie;
import com.rodriguez_blanco.popularmovies.repository.MovieRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieDetailViewModel extends ViewModel {
    private LiveData<Movie> mMovie;
    private MovieRepository mMovieRepository;
    private String mMovieId;

    @Inject
    public MovieDetailViewModel(MovieRepository movieRepository) {
        mMovieRepository = movieRepository;
    }

    public LiveData<Movie> getMovie(String movieId) {
        if (mMovieId == null || !mMovieId.equals(movieId)) {
            mMovieId = movieId;
            mMovie = mMovieRepository.getMovie(mMovieId);
        }
        return  mMovie;
    }
}
