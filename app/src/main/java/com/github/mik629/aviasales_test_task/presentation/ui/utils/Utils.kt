@file:Suppress("NOTHING_TO_INLINE")

package com.github.mik629.aviasales_test_task.presentation.ui.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

inline fun View.showSnackBar(message: String, duration: Int = BaseTransientBottomBar.LENGTH_LONG) {
    Snackbar.make(
        this,
        message,
        duration
    ).show()
}

inline fun View.hideKeyboard(context: Context) {
    (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(this.windowToken, 0)
}

inline fun dpToPx(dp: Float, density: Float): Int =
    (dp * density).toInt()