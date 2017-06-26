/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class PopularMoviesContract {
    public static final String CONTENT_AUTHORITY = "com.rodriguez_blanco.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoriteEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID= "movie_id";

        public static final String COLUMN_MOVIE_TITLE= "title";

        public static final String COLUMN_POSTER_PATH= "poster";
    }
}
