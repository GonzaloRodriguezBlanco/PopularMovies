/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.domain;

import com.google.gson.annotations.SerializedName;

public class Video {
    @SerializedName("id")
    private String mId;

    @SerializedName("iso_639_1")
    private String mIso639_1;

    @SerializedName("iso_3166_1")
    private String mIso3166_1;

    @SerializedName("key")
    private String mKey;

    @SerializedName("name")
    private String mName;

    @SerializedName("site")
    private String mSite;

    @SerializedName("size")
    private int mSize;

    @SerializedName("type")
    private String mType;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getIso639_1() {
        return mIso639_1;
    }

    public void setIso639_1(String mIso639_1) {
        this.mIso639_1 = mIso639_1;
    }

    public String getIso3166_1() {
        return mIso3166_1;
    }

    public void setIso3166_1(String mIso3166_1) {
        this.mIso3166_1 = mIso3166_1;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getSite() {
        return mSite;
    }

    public void setSite(String mSite) {
        this.mSite = mSite;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int mSize) {
        this.mSize = mSize;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }
}
