/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.api;

import com.google.gson.annotations.SerializedName;
import com.rodriguez_blanco.popularmovies.domain.Review;

import java.util.List;

public class GetReviewsResponse {
    @SerializedName("id")
    private int movieId;

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("results")
    private List<Review> results;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<Review> getResults() {
        return results;
    }
}
