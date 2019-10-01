package com.example.imdb.ui

import com.example.imdb.di.get
import com.example.imdb.utils.helper.ScopeViewModel
import kotlinx.coroutines.*

class ApplicationViewModel : ScopeViewModel() {
    override fun onCleared() {
        // Finish all tasks on application finished.
        get<Job>().cancelChildren()
    }
}