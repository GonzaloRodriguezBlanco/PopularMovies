/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.api;

import com.rodriguez_blanco.popularmovies.domain.Configuration;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TheMovieDbWebservice {
    String API_KEY_V3 = "CHANGE_ME";
    String API_VERSION = "/3";

    String IMAGES_BASE_URL = "https://image.tmdb.org/t/p/";
    String DEFAULT_IMAGE_FILE_SIZE = "w342"; // "w92", "w154","w185", "w342", "w500", "w780", "original"

    @GET(API_VERSION + "/movie/popular")
    Call<GetPopularMoviesResponse> getPopularMovies(@Query("api_key") String api_key,
                                           @Query("language") String language,
                                           @Query("page") Integer page,
                                           @Query("region") String region);

    @GET(API_VERSION + "/movie/top_rated")
    Call<GetPopularMoviesResponse> getTopRatedMovies(@Query("api_key") String api_key,
                                           @Query("language") String language,
                                           @Query("page") Integer page,
                                           @Query("region") String region);

    @GET(API_VERSION + "/configuration")
    Call<Configuration> getConfiguration(@Query("api_key") String api_key);
}
