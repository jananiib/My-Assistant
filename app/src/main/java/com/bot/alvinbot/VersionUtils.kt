package com.bot.alvinbot

import android.content.Context
import android.os.Build

object VersionUtils {

    /**
     * Check android version is lollipop
     */
    fun isGreaterThanL(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Check android version is marshmallow
     */
    fun isGreaterThanM(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * Check android version is nougat
     */
    fun isGreaterThanN(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    /**
     * Check android version is oreo
     */
    fun isGreaterThanO(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getOSVersion(): Int {
        return Build.VERSION.SDK_INT
    }
    //t.setPadding(0, getStatusBarHeight(), 0, 0)
}