package com.example.imdb.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.imdb.di.GlideApp
import com.example.imdb.di.get
import com.example.imdb.utils.setOnClickWithDoubleCheck

@BindingAdapter(
    value = ["bind:image_uri", "bind:image_placeholder"],
    requireAll = false
)
fun adapterImage(
    view: ImageView,
    uri: String?,
    placeholder: Drawable? = null
) {
    if (uri.isNullOrBlank()) return

    GlideApp.with(get<Context>())
        .load(uri)
        .fitCenter()
        .placeholder(placeholder)
        .into(view)
}

@BindingAdapter(value = ["bind:click_double_check"])
fun adapterClickWithDoubleCheck(
    view: View,
    listener: View.OnClickListener
) {
    view.setOnClickWithDoubleCheck(listener)
}