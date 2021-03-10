package com.bot.alvinbot.utils

import android.content.Context
import android.net.ConnectivityManager
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext

object NetworkUtils {
    fun isNetWorkAvailable(context: Context): Boolean {
        val connectivityManager: ConnectivityManager =
            context .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    fun isInFlightMode(context: Context): Boolean {
        return Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }
}