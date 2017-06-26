/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rodriguez_blanco.popularmovies.data.PopularMoviesContract.FavoriteEntry;

import static com.rodriguez_blanco.popularmovies.data.PopularMoviesContract.FavoriteEntry.TABLE_NAME;

public class PopularMoviesContentProvider extends ContentProvider {
    public static final int FAVORITES = 100;
    public static final int FAVORITE_WITH_MOVIE_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.PATH_FAVORITES, FAVORITES);
        uriMatcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.PATH_FAVORITES + "/*", FAVORITE_WITH_MOVIE_ID);

        return uriMatcher;
    }

    private PopularMoviesDbHelper mPopularMoviesDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mPopularMoviesDbHelper = new PopularMoviesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = mPopularMoviesDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case FAVORITES:
                long id = db.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(FavoriteEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        final SQLiteDatabase db = mPopularMoviesDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {

            case FAVORITES:
                retCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mPopularMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int favoritesDeleted;

        switch (match) {
            case FAVORITE_WITH_MOVIE_ID:
                String movieId = uri.getPathSegments().get(1);

                favoritesDeleted = db.delete(TABLE_NAME,
                        FavoriteEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{movieId});

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (favoritesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return favoritesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
