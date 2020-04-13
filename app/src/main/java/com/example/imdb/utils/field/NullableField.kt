package com.example.imdb.utils.field

import android.os.Handler
import android.os.Looper
import androidx.annotation.AnyThread
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class NullableField<T : Any?> : MutableLiveData<T?> {
    private val isTriggerNotEquals: Boolean

    constructor(isTriggerNotEquals: Boolean = true) : super() {
        this.isTriggerNotEquals = isTriggerNotEquals
    }

    constructor(value: T?, isTriggerNotEquals: Boolean = true) : super(value) {
        this.isTriggerNotEquals = isTriggerNotEquals
    }

    @AnyThread
    override fun postValue(value: T?) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Handler(Looper.getMainLooper()).post { setValue(value) }
        } else @MainThread {
            setValue(value)
        }
    }

    @MainThread
    override fun setValue(value: T?) {
        if (isTriggerNotEquals && value == super.getValue()) return
        super.setValue(value)
    }

    @AnyThread
    override fun getValue(): T? = super.getValue()

    @MainThread
    fun observeNullable(owner: LifecycleOwner, observer: NullableObserver<in T?>) =
        super.observe(owner, observer)

    @MainThread
    fun observeNullableForever(observer: NullableObserver<in T?>) =
        super.observeForever(observer)

    @MainThread
    fun removeNullableObserver(observer: NullableObserver<in T?>) =
        super.removeObserver(observer)

    @Deprecated(
        "Use a observeNullable instead.",
        ReplaceWith("observeNullable(owner, observer)"),
        DeprecationLevel.HIDDEN
    )
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) =
        super.observe(owner, observer)

    @Deprecated(
        "Use a observeNullableForever instead.",
        ReplaceWith("observeNullableForever(owner, observer)"),
        DeprecationLevel.HIDDEN
    )
    @MainThread
    override fun observeForever(observer: Observer<in T?>) =
        super.observeForever(observer)

    @Deprecated(
        "Use a removeNullableObserver instead.",
        ReplaceWith("removeNullableObserver(owner, observer)"),
        DeprecationLevel.HIDDEN
    )
    @MainThread
    override fun removeObserver(observer: Observer<in T?>) =
        super.removeObserver(observer)
}