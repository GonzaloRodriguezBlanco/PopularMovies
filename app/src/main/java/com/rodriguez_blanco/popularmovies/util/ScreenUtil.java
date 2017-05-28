/*
 * Copyright (C) 2017 Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.popularmovies.util;

import android.content.res.Resources;


public class ScreenUtil {

    public static int getScreenWidthInPixels() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}
