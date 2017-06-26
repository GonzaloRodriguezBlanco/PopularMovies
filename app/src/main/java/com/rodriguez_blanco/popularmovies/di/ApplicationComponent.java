/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.di;

import android.content.Context;

import com.rodriguez_blanco.popularmovies.PopularMoviesApplication;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        ApplicationModule.class,
        MainActivityModule.class,
        DetailsActivityModule.class
})
public interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        Builder applicationModule(ApplicationModule applicationModule);
        ApplicationComponent build();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Exposed to sub-graphs
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // From ApplicationModule
    Context context();

    void inject(PopularMoviesApplication popularMoviesApplication);
}
