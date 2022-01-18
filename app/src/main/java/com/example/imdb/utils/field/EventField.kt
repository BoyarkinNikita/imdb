package com.example.imdb.utils.field

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.annotation.AnyThread
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.*
import kotlin.collections.ArrayList

class EventField<T : Any>(
    private val isRepetitionValid: Boolean = false
) : LiveData<T>() {
    private val events = ArrayDeque<T>()
    private val observers = ArrayList<Pair<NonNullObserver<in T>, NonNullObserver<in T>>>()

    @AnyThread
    @SuppressLint("WrongThread")
    fun triggerEvent(value: T) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Handler(Looper.getMainLooper()).post {
                addEvent(value)
                setValue(value)
            }
        } else @MainThread {
            addEvent(value)
            setValue(value)
        }
    }

    @MainThread
    fun observeEvent(owner: LifecycleOwner, observer: NonNullObserver<in T>) {
        checkMainThread()
        val fake = createFakeObserver(observer)
        super.observe(owner, fake)
    }

    @MainThread
    fun observeEventForever(observer: NonNullObserver<in T>) {
        checkMainThread()
        val fake = createFakeObserver(observer)
        super.observeForever(fake)
    }

    @MainThread
    fun removeEventObserver(observer: NonNullObserver<in T>) {
        checkMainThread()

        observers.forEach {
            if (it.first === observer || it.second === observer) {
                observers.remove(it)
                super.removeObserver(observer)
                return
            }
        }
    }

    @Deprecated(
        "Use a observeEvent instead.",
        ReplaceWith("observeEvent(owner, observer)"),
        DeprecationLevel.HIDDEN
    )
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        checkObserver(observer)
        observeEvent(owner, observer as NonNullObserver<in T>)
    }

    @Deprecated(
        "Use a observeEventForever instead.",
        ReplaceWith("observeEventForever(owner, observer)"),
        DeprecationLevel.HIDDEN
    )
    @MainThread
    override fun observeForever(observer: Observer<in T>) {
        checkObserver(observer)
        observeEventForever(observer as NonNullObserver<in T>)
    }

    @Deprecated(
        "Use a removeEventObserver instead.",
        ReplaceWith("removeEventObserver(owner, observer)"),
        DeprecationLevel.HIDDEN
    )
    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        checkObserver(observer)
        removeEventObserver(observer as NonNullObserver<in T>)
    }

    @Deprecated(
        "Getting a value from the event makes no sense.",
        level = DeprecationLevel.HIDDEN
    )
    @AnyThread
    override fun getValue(): T? = super.getValue()

    @MainThread
    private fun createFakeObserver(realObserver: NonNullObserver<in T>) = NonNullObserver<T> { _ ->
        events.apply {
            if (isEmpty()) return@NonNullObserver
            forEach { realObserver.onChanged(it) }
            clear()
        }
    }.also { fakeObserver ->
        observers.add(realObserver to fakeObserver)
    }

    @MainThread
    private fun addEvent(event: T) {
        if (!isRepetitionValid) events.removeAll { it == event }
        events.addFirst(event)
    }

    companion object {
        private fun <T : Any> checkObserver(observer: Observer<in T>) =
            require(observer is NonNullObserver<in T>) {
                "Observer class must be an instance of a NonNullObserver class."
            }

        private fun checkMainThread() =
            require(Looper.myLooper() == Looper.getMainLooper()) {
                "You must call this method on the main thread."
            }
    }
}