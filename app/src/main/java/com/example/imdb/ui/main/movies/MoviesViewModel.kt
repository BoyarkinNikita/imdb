package com.example.imdb.ui.main.movies

import com.example.imdb.di.get
import com.example.imdb.storage.OmdbRepository
import com.example.imdb.ui.main.movies.helper.MovieItem
import com.example.imdb.utils.helper.Field
import com.example.imdb.utils.helper.ScopeViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class MoviesViewModel : ScopeViewModel() {
    val movies = Field<List<MovieItem>>(listOf())
    val error = Field<Throwable>()
    var isLoading = Field<Boolean>(false)

    val clicked = Field<MovieItem?>(null)

    var lastQuery = "Movie"
    var offset: Int = 0
    var page: Int = 1
    var isLimitReached: Boolean = false

    fun initialize() {
        search(lastQuery)
    }

    fun search(query: String) {
        isLoading.value = false
        movies.value = listOf()

        offset = 0
        page = 1
        isLimitReached = false
        lastQuery = query

        loadMovies()
    }

    fun loadMovies() {
        if (isLimitReached) return

        scope.launch {
            isLoading.value = true

            val (items, total) = try {
                withContext(IO) { get<OmdbRepository>().search(lastQuery, page) }
            } catch (throwable: Throwable) {
                Timber.w(throwable)
                isLoading.value = false
                error.value = throwable
                return@launch
            }

            isLimitReached = offset + items.size >= total
            offset += items.size
            page++

            movies.value = movies.value?.toMutableList()?.apply {
                addAll(items)
            } ?: items

            isLoading.value = false
        }
    }

    fun onMovieClick(item: MovieItem) {
        clicked.value = item
    }
}