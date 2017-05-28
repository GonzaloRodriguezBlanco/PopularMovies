/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.di;

import com.rodriguez_blanco.popularmovies.ui.MovieListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentsModule {
    @ContributesAndroidInjector
    abstract MovieListFragment contributeMovieListFragment();

}
