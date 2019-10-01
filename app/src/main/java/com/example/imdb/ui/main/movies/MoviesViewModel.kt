package com.example.imdb.ui.main.movies

import com.example.imdb.api.OmdbClient
import com.example.imdb.di.get
import com.example.imdb.utils.helper.Field
import com.example.imdb.utils.helper.ScopeViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesViewModel : ScopeViewModel() {
    val movies = Field<List<MovieItem>>(listOf())
    val error = Field<Throwable>()
    var isLoading = Field<Boolean>(false)

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
                withContext(IO) { get<OmdbClient>().search(lastQuery, page) }
            } catch (throwable: Throwable) {
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
}