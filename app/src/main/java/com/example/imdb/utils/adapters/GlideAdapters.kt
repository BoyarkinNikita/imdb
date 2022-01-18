package com.example.imdb.utils.adapters

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import android.widget.ImageView.ScaleType.FIT_CENTER
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy.AUTOMATIC
import com.bumptech.glide.load.engine.DiskCacheStrategy.NONE
import com.example.imdb.di.GlideApp

@BindingAdapter(
    value = [
        "bind:image_uri", "bind:image_placeholder",
        "bind:image_error", "bind:image_caching_disabled"
    ],
    requireAll = false
)
@SuppressLint("CheckResult")
fun adapterImageUri(
    view: ImageView,
    uri: String?,
    defaultPlaceholder: Drawable? = null,
    errorPlaceholder: Drawable? = null,
    isCachingDisabled: Boolean = false
) {
    val context = view.context
    if (uri.isNullOrBlank() || context == null) {
        (defaultPlaceholder ?: errorPlaceholder)?.let { view.setImageDrawable(it) }
        return
    }

    val scaleType = view.scaleType

    GlideApp.with(context).load(uri).run {
        when (scaleType) {
            CENTER_INSIDE -> centerInside()
            FIT_CENTER -> fitCenter()
            else -> centerCrop()
        }

        placeholder(defaultPlaceholder)
        error(errorPlaceholder)
        skipMemoryCache(isCachingDisabled)
        diskCacheStrategy(if (isCachingDisabled) NONE else AUTOMATIC)
        into(view)
    }
}