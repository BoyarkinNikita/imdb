package com.example.imdb.utils.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.util.TypedValue
import android.view.View
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.resource.drawable.DrawableDecoderCompat

/** Section: Context Resources */

fun Context.string(@StringRes resId: Int): String = getString(resId)

fun Context.string(@StringRes resId: Int, vararg args: Any): String = getString(resId, *args)

@ColorInt
fun Context.color(@ColorRes resId: Int): Int = ContextCompat.getColor(this, resId)

@Dimension(unit = Dimension.PX)
fun Context.dimen(@DimenRes resId: Int): Int = resources.getDimensionPixelSize(resId)

fun Context.drawable(@DrawableRes resId: Int): Drawable =
    DrawableDecoderCompat.getDrawable(this, resId, theme)
        ?: ResourcesCompat.getDrawable(resources, resId, null)!!

fun Context.integer(@IntegerRes resId: Int): Int = resources.getInteger(resId)

@ColorInt
fun Context.attrColor(@AttrRes resId: Int): Int = TypedValue().also {
    theme.resolveAttribute(resId, it, true)
}.data

/** Section: Fragment Resources */

fun Fragment.string(@StringRes resId: Int): String = getString(resId)

fun Fragment.string(@StringRes resId: Int, vararg args: Any): String = getString(resId, *args)

@Suppress("DEPRECATION")
@ColorInt
fun Fragment.color(@ColorRes resId: Int): Int =
    (activity ?: context)?.let { ContextCompat.getColor(it, resId) } ?: resources.getColor(resId)

@Dimension(unit = Dimension.PX)
fun Fragment.dimen(@DimenRes resId: Int): Int = resources.getDimensionPixelSize(resId)

@Suppress("DEPRECATION")
fun Fragment.drawable(@DrawableRes resId: Int): Drawable =
    (activity ?: context)?.run { DrawableDecoderCompat.getDrawable(this, resId, theme) }
        ?: ResourcesCompat.getDrawable(resources, resId, null)!!

fun Fragment.integer(@IntegerRes resId: Int): Int = resources.getInteger(resId)

@ColorInt
fun Fragment.attrColor(@AttrRes resId: Int): Int = (activity ?: context)!!.attrColor(resId)

/** Section: View Resources */

fun View.string(@StringRes resId: Int): String = resources.getString(resId)

fun View.string(@StringRes resId: Int, vararg args: Any): String = resources.getString(resId, *args)

@Suppress("DEPRECATION")
@ColorInt
fun View.color(@ColorRes resId: Int): Int =
    context?.let { ContextCompat.getColor(it, resId) } ?: resources.getColor(resId)

@Dimension(unit = Dimension.PX)
fun View.dimen(@DimenRes resId: Int): Int = resources.getDimensionPixelSize(resId)

@Suppress("DEPRECATION")
fun View.drawable(@DrawableRes resId: Int): Drawable =
    context?.run { DrawableDecoderCompat.getDrawable(this, resId, theme) }
        ?: ResourcesCompat.getDrawable(resources, resId, null)!!

fun View.integer(@IntegerRes resId: Int): Int = resources.getInteger(resId)

@ColorInt
fun View.attrColor(@AttrRes resId: Int): Int = context!!.attrColor(resId)

/** Section: Other. */

inline fun View.setOnClickWithDoubleCheck(crossinline callback: (view: View) -> Unit) {
    var shareClickTime = 0L

    setOnClickListener {
        // Prevent double clicks.
        if (SystemClock.elapsedRealtime() - shareClickTime < 400) return@setOnClickListener
        shareClickTime = SystemClock.elapsedRealtime()

        callback(it)
    }
}

fun View.setOnClickWithDoubleCheck(callback: View.OnClickListener?) {
    if (callback == null) {
        setOnClickListener(null)
        return
    }

    setOnClickWithDoubleCheck { callback.onClick(it) }
}
