package com.bot.alvinbot.extensions

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern


fun isNullOrEmpty(value: String?): Boolean {
    return value != "" && value != null
}

fun isValidEmail(email: CharSequence?): Boolean {

    val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$"

    val pat = Pattern.compile(emailRegex)
    val result = email != null && pat.matcher(email).matches()
    return !result
}