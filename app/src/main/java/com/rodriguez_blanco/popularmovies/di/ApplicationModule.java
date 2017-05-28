/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.di;

import com.rodriguez_blanco.popularmovies.BuildConfig;
import com.rodriguez_blanco.popularmovies.api.TheMovieDbWebservice;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
class ApplicationModule {


    @Singleton
    @Provides
    OkHttpClient provideOkHtppClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // One of: NONE | BASIC | HEADERS | BODY
            builder.interceptors().add(loggingInterceptor);
        }

        return builder.build();
    }

    @Singleton
    @Provides
    TheMovieDbWebservice provideGithubService(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(TheMovieDbWebservice.class);
    }
}
