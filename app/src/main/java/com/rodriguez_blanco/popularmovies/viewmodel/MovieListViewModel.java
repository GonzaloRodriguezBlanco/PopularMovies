/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.rodriguez_blanco.popularmovies.domain.Movie;
import com.rodriguez_blanco.popularmovies.repository.MovieRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieListViewModel extends ViewModel {
    private LiveData<List<Movie>> mMovies;
    private MovieRepository mMovieRepository;

    @Inject
    public MovieListViewModel(MovieRepository movieRepository) {
        mMovieRepository = movieRepository;
    }

    public void init() {
        if (mMovies == null) {
            getPopularMovies();
        }
    }

    public LiveData<List<Movie>> getMovies() {
        return  mMovies;
    }

    public void getPopularMovies() {
        mMovies = mMovieRepository.getPopularMovies();
    }

    public void getTopRatedMovies() {
        mMovies = mMovieRepository.getTopRatedMovies();
    }

    public void setFavoritesMovies(List<Movie> favoriteMovies) {
        final MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
        movies.setValue(favoriteMovies);

        mMovies = movies;
    }
}
