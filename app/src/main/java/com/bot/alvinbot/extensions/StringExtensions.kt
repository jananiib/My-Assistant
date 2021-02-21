package com.bot.alvinbot.extensions

import android.text.TextUtils
import android.util.Patterns


fun isNullOrEmpty(value: String?): Boolean {
    return value != "" && value !=null
}

fun isValidEmail(target: String?): Boolean {
    return TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}