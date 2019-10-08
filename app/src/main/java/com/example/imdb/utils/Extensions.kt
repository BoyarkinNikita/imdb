package com.example.imdb.utils

import android.content.Context
import android.os.SystemClock
import android.view.View
import androidx.annotation.*
import androidx.annotation.Dimension.PX
import androidx.fragment.app.Fragment

/** Section: Context Resources */

fun Context.string(@StringRes resId: Int): String = getString(resId)

fun Context.string(@StringRes resId: Int, vararg args: Any): String = getString(resId, args)

@ColorInt
fun Context.color(@ColorRes resId: Int): Int = getColor(resId)

@Dimension(unit = PX)
fun Context.dimen(@DimenRes resId: Int): Int = resources.getDimensionPixelSize(resId)

/** Section: Fragment Resources */

fun Fragment.string(@StringRes resId: Int): String = getString(resId)

fun Fragment.string(@StringRes resId: Int, vararg args: Any): String = getString(resId, args)

@Suppress("DEPRECATION")
@ColorInt
fun Fragment.color(@ColorRes resId: Int): Int =
    (activity ?: context)?.getColor(resId) ?: resources.getColor(resId)

@Dimension(unit = PX)
fun Fragment.dimen(@DimenRes resId: Int): Int = resources.getDimensionPixelSize(resId)

/** Section: View Extensions */

inline fun View.setOnClickWithDoubleCheck(crossinline callback: (view: View) -> Unit) {
    var shareClickTime = 0L

    setOnClickListener {
        // Prevent double clicks.
        if (SystemClock.elapsedRealtime() - shareClickTime < 400) return@setOnClickListener
        shareClickTime = SystemClock.elapsedRealtime()

        callback(it)
    }
}

fun View.setOnClickWithDoubleCheck(callback: View.OnClickListener) = setOnClickWithDoubleCheck {
    callback.onClick(it)
}
