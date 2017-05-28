/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.rodriguez_blanco.popularmovies.domain.Movie;
import com.rodriguez_blanco.popularmovies.repository.MovieRepository;

import java.util.List;

import javax.inject.Inject;

public class MovieListViewModel extends ViewModel {
    private LiveData<List<Movie>> mMovies;
    private MovieRepository mMovieRepository;

    @Inject
    public MovieListViewModel(MovieRepository movieRepository) {
        mMovieRepository = movieRepository;
    }

    public void init() {
        if (this.mMovies != null) {
            return;
        }
        mMovies = mMovieRepository.getPopularMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return  mMovies;
    }
}
