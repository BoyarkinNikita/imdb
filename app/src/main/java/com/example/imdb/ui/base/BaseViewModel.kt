package com.example.imdb.ui.base

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    lateinit var sharedData: ApplicationViewModel

    private var recreationCount = 0

    var isFirstCreation = true
        private set

    @MainThread
    fun onCreate() {
        recreationCount++
        if (recreationCount > 1) isFirstCreation = false
    }

    protected inline fun doOnce(function: () -> Unit) {
        if (isFirstCreation) function()
    }
}