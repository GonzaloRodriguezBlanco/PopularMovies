/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.di;

import com.rodriguez_blanco.popularmovies.ui.list.MovieListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MovieListFragmentsModule {

    @ContributesAndroidInjector
    abstract MovieListFragment contributeMovieListFragment();
}
