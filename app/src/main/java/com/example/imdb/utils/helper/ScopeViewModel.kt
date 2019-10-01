package com.example.imdb.utils.helper

import androidx.lifecycle.ViewModel
import com.example.imdb.di.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

abstract class ScopeViewModel : ViewModel() {
    protected val job = SupervisorJob(get<Job>())
    protected val scope = CoroutineScope(Main + job)

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}