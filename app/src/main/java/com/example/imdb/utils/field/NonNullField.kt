package com.example.imdb.utils.field

import android.os.Handler
import android.os.Looper
import androidx.annotation.AnyThread
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class NonNullField<T : Any>(
    value: T,
    private val isTriggerNotEquals: Boolean = true
) : MutableLiveData<T>(value) {
    @AnyThread
    override fun postValue(value: T) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Handler(Looper.getMainLooper()).post { setValue(value) }
        } else @MainThread {
            setValue(value)
        }
    }

    @MainThread
    override fun setValue(value: T) {
        if (isTriggerNotEquals && value == super.getValue()) return
        super.setValue(value)
    }

    @AnyThread
    override fun getValue(): T =
        super.getValue() ?: throw IllegalStateException("Value is not set yet.")

    @MainThread
    fun observeNonNull(owner: LifecycleOwner, observer: NonNullObserver<in T>) =
        super.observe(owner, observer)

    @MainThread
    fun observeNonNullForever(observer: NonNullObserver<in T>) =
        super.observeForever(observer)

    @MainThread
    fun removeNonNullObserver(observer: NonNullObserver<in T>) =
        super.removeObserver(observer)

    @Deprecated(
        "Use a observeNonNull instead.",
        ReplaceWith("observeNonNull(owner, observer)"),
        DeprecationLevel.HIDDEN
    )
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) =
        super.observe(owner, observer)

    @Deprecated(
        "Use a observeNonNullForever instead.",
        ReplaceWith("observeNonNullForever(owner, observer)"),
        DeprecationLevel.HIDDEN
    )
    @MainThread
    override fun observeForever(observer: Observer<in T>) =
        super.observeForever(observer)

    @Deprecated(
        "Use a removeNonNullObserver instead.",
        ReplaceWith("removeNonNullObserver(owner, observer)"),
        DeprecationLevel.HIDDEN
    )
    @MainThread
    override fun removeObserver(observer: Observer<in T>) =
        super.removeObserver(observer)
}