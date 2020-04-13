package com.example.imdb.ui.base

import com.example.imdb.di.get
import kotlinx.coroutines.*

class ApplicationViewModel : ScopeViewModel() {
    override fun onCleared() {
        // Finish all tasks on application finished.
        get<Job>().cancelChildren()
    }
}