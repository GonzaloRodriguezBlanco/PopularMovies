/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.rodriguez_blanco.popularmovies.domain.Movie;
import com.rodriguez_blanco.popularmovies.domain.Review;
import com.rodriguez_blanco.popularmovies.domain.Video;
import com.rodriguez_blanco.popularmovies.repository.MovieRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieDetailViewModel extends ViewModel {
    private LiveData<Movie> mMovie;
    private LiveData<List<Video>> mVideos;
    private LiveData<List<Review>> mReviews;
    private MovieRepository mMovieRepository;
    private String mMovieId;
    private String mVideosMovieId;
    private String mReviewsMovieId;

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

    public LiveData<List<Video>> getVideos(String movieId) {
        if (mVideos == null || !mVideosMovieId.equals(movieId)) {
            mVideosMovieId = movieId;
            mVideos = mMovieRepository.getVideos(movieId);
        }
        return  mVideos;
    }

    public LiveData<List<Review>> getReviews(String movieId) {
        if (mReviews == null || !mReviewsMovieId.equals(movieId)) {
            mReviewsMovieId = movieId;
            mReviews = mMovieRepository.getReviews(movieId);
        }
        return  mReviews;
    }
}
