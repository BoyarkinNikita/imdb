package com.example.imdb.utils

import android.content.Context
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