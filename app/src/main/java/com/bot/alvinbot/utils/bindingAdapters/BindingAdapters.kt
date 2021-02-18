package com.bot.alvinbot.utils.bindingAdapters

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bot.alvinbot.extensions.isNullOrEmpty


@BindingAdapter("isVisible")
fun setIsVisible(view: View, value: String) {
    if (isNullOrEmpty(value)) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}


@BindingAdapter(value = ["errorText"], requireAll = false)
fun setErrorMessage(view: TextView, errorMessage: String?) {
    view.error = errorMessage
}