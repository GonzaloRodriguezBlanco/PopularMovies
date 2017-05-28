/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.rodriguez_blanco.popularmovies.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class DetailsActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    private static final String INTENT_EXTRA_PARAM_MOVIE_ID = "com.rodriguez_blanco.INTENT_PARAM_MOVIE_ID";

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static Intent getCallingIntent(Context context,
                                          String movieId) {
        Intent callingIntent =  new Intent(context, DetailsActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_MOVIE_ID, movieId);

        return callingIntent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeActionBar();
        initializeFragment(savedInstanceState);
    }

    private void initializeActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null){
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.movie_details);
        }
    }

    private void initializeFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            String movieId = getIntent().getStringExtra(INTENT_EXTRA_PARAM_MOVIE_ID);

            MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.forMovie(movieId);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, movieDetailsFragment)
                    .commit();
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }
}
