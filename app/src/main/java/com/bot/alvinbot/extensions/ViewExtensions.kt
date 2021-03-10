package com.bot.alvinbot.extensions

import android.content.Context
import android.view.View
import android.widget.Toast
import com.bot.alvinbot.utils.widgets.CustomToast

fun View.showView() {
    visibility = View.VISIBLE
}

fun View.hideView() {
    visibility = View.GONE
}

fun showErrorMessage(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) =
    CustomToast.makeText(context, message, duration, CustomToast.ERROR).show()


fun showWarningMessage(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) =
    CustomToast.makeText(context, message, duration, CustomToast.WARNING).show()
