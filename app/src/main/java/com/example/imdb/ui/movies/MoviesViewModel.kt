package com.example.imdb.ui.movies

import com.example.imdb.di.get
import com.example.imdb.storage.OmdbRepository
import com.example.imdb.ui.base.ScopeViewModel
import com.example.imdb.ui.movies.helper.MovieItem
import com.example.imdb.utils.field.EventField
import com.example.imdb.utils.field.NonNullField
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class MoviesViewModel : ScopeViewModel() {
    val movies = NonNullField(listOf<MovieItem>(), isTriggerNotEquals = false)

    val loading = NonNullField(false)

    val eventError = EventField<Throwable>()
    val eventClicked = EventField<MovieItem>()

    private var lastQuery = "Movie"
    private var offset: Int = 0
    private var page: Int = 1

    var isLimitReached: Boolean = false

    fun initialize() {
        search(lastQuery)
    }

    fun search(query: String) {
        loading.value = false
        movies.value = listOf()

        offset = 0
        page = 1
        isLimitReached = false
        lastQuery = query

        loadMovies()
    }

    fun loadMovies() {
        if (isLimitReached) return

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Timber.w(throwable)
            loading.value = false
            eventError.triggerEvent(throwable)
        }

        scope.launch(exceptionHandler) {
            loading.value = true

            val (items, total) = withContext(IO) {
                get<OmdbRepository>().search(lastQuery, page)
            }

            isLimitReached = offset + items.size >= total
            offset += items.size
            page++

            movies.value = movies.value.toMutableList().apply {
                addAll(items)
            }

            loading.value = false
        }
    }

    fun onMovieClick(item: MovieItem) {
        eventClicked.triggerEvent(item)
    }
}