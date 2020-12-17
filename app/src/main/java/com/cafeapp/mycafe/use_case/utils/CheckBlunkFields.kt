package com.cafeapp.mycafe.use_case.utils

import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_addcategory.*

fun checkEditTextFocus(textView: TextView, message: String) {
    textView.setOnFocusChangeListener { view, hasFocus ->
        if (hasFocus) return@setOnFocusChangeListener

        isError(textView, message)
    }
}

fun isError(textView: TextView, message: String): Boolean {
    return if (textView.text.toString().isNotBlank()) {
        hideError(textView)
        false
    } else {
        showError(textView, message)
        true
    }
}

private fun hideError(textView: TextView) {
    textView.error = null
}

private fun showError(textView: TextView, message: String) {
    textView.error = message
}