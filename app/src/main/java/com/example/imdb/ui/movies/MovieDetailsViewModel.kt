package com.example.imdb.ui.movies

import com.example.imdb.di.get
import com.example.imdb.storage.OmdbRepository
import com.example.imdb.ui.movies.helper.MovieDetailsItem
import com.example.imdb.ui.base.ScopeViewModel
import com.example.imdb.utils.field.EventField
import com.example.imdb.utils.field.NonNullField
import com.example.imdb.utils.field.NullableField
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class MovieDetailsViewModel : ScopeViewModel() {
    val details = NullableField<MovieDetailsItem?>(null)

    val loading = NonNullField(false)

    val eventError = EventField<Throwable>()

    lateinit var imdbId: String

    fun loadDetails() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Timber.w(throwable)
            loading.value = false
            eventError.triggerEvent(throwable)
        }

        scope.launch(exceptionHandler) {
            loading.value = true

            val result = withContext(IO) {
                get<OmdbRepository>().details(imdbId)
            }

            details.value = result
            loading.value = false
        }
    }
}