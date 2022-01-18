package com.example.imdb.utils.adapters

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.databinding.BindingAdapter
import com.example.imdb.utils.extensions.setOnClickWithDoubleCheck
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("bind:html_text")
fun adapterHtmlText(
    view: TextView,
    source: CharSequence
) = with(view) {
    text = HtmlCompat.fromHtml(source.toString(), FROM_HTML_MODE_COMPACT)
}

@BindingAdapter("bind:click_double_check")
fun adapterClickDoubleCheck(
    view: View,
    listener: OnClickListener
) = with(view) {
    setOnClickWithDoubleCheck(listener)
}

@BindingAdapter("bind:gone_condition")
fun adapterViewVisibilityGone(
    view: View,
    isGone: Boolean
) = with(view) {
    visibility = if (isGone) GONE else VISIBLE
}

@BindingAdapter("bind:invisible_condition")
fun adapterViewVisibilityInvisible(
    view: View,
    isInvisible: Boolean
) = with(view) {
    visibility = if (isInvisible) INVISIBLE else VISIBLE
}

@BindingAdapter("bind:gone_if_null")
fun adapterGoneIfNull(
    view: View,
    value: Any?
) = with(view) {
    visibility = if (value == null) GONE else VISIBLE
}

@BindingAdapter("bind:invisible_if_null")
fun adapterInvisibleIfNull(
    view: View,
    value: Any?
) = with(view) {
    visibility = if (value == null) INVISIBLE else VISIBLE
}

@BindingAdapter("bind:gone_if_null_or_blank")
fun adapterGoneIfNullOrBlank(
    view: View,
    value: Any?
) = with(view) {
    visibility = if ((value?.toString()).isNullOrBlank()) GONE else VISIBLE
}

@BindingAdapter("bind:invisible_if_null_or_blank")
fun adapterInvisibleIfNullOrBlank(
    view: View,
    value: Any?
) = with(view) {
    visibility = if ((value?.toString()).isNullOrBlank()) INVISIBLE else VISIBLE
}

@BindingAdapter(
    value = ["bind:selectable_ripple", "bind:white_ripple"],
    requireAll = false
)
fun adapterSelectableRipple(
    view: View,
    isSelectable: Boolean,
    isWhite: Boolean?
) = with(view) {
    isClickable = isSelectable
    isFocusable = isSelectable
    isFocusableInTouchMode = false

    if (!isSelectable) return

    val array = context.obtainStyledAttributes(IntArray(1) {
        android.R.attr.selectableItemBackground
    })

    val drawable = array.getDrawable(0)
    array.recycle()
    drawable ?: return

    if (drawable is RippleDrawable) {
        val color = if (isWhite == true) 0x30FFFFFF else 0x10000000
        drawable.setColor(ColorStateList.valueOf(color))
    }

    foreground = drawable
}

@BindingAdapter("app:srcCompat")
fun adapterSrcCompat(
    view: AppCompatImageView,
    drawable: Drawable?
) = with(view) {
    drawable ?: return
    setImageDrawable(drawable)
}

@BindingAdapter("android:layout_marginStart")
fun adapterLayoutMarginStart(view: TextView, value: Float) {
    val params = view.layoutParams
    if (params !is MarginLayoutParams) return
    params.marginStart = value.toInt()
    view.requestLayout()
}

@BindingAdapter("android:layout_marginEnd")
fun adapterLayoutMarginEnd(view: TextView, value: Float) {
    val params = view.layoutParams
    if (params !is MarginLayoutParams) return
    params.marginEnd = value.toInt()
    view.requestLayout()
}

@BindingAdapter("android:layout_marginTop")
fun adapterLayoutMarginTop(view: TextView, value: Float) {
    val params = view.layoutParams
    if (params !is MarginLayoutParams) return
    params.topMargin = value.toInt()
    view.requestLayout()
}

@BindingAdapter("android:layout_marginBottom")
fun adapterLayoutMarginBottom(view: TextView, value: Float) {
    val params = view.layoutParams
    if (params !is MarginLayoutParams) return
    params.bottomMargin = value.toInt()
    view.requestLayout()
}

@BindingAdapter("android:textColorHint")
fun adapterTextColorHint(view: TextInputLayout, @ColorInt value: Int) {
    view.defaultHintTextColor = ColorStateList.valueOf(value)
}