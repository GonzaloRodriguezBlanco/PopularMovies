/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.domain;

import com.google.gson.annotations.SerializedName;

public class Configuration {
    @SerializedName("images")
    Images images;

    @SerializedName("change_keys")
    String[] changeKeys;

    public class Images {
        @SerializedName("base_url")
        String baseUrl;

        @SerializedName("secure_base_url")
        String secureBaseUrl;

        @SerializedName("backdrop_sizes")
        String[] backdropSizes;

        @SerializedName("logo_sizes")
        String[] logoSizes;

        @SerializedName("poster_sizes")
        String[] posterSizes;

        @SerializedName("profile_sizes")
        String[] profileSizes;

        @SerializedName("profile_sizes")
        String[] stillSizes;
    }
}
