package com.example.imdb.ui.main.movies

import com.example.imdb.di.get
import com.example.imdb.storage.OmdbRepository
import com.example.imdb.ui.main.movies.helper.MovieDetailsItem
import com.example.imdb.utils.helper.Field
import com.example.imdb.utils.helper.ScopeViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class MovieDetailsViewModel : ScopeViewModel() {
    val details = Field<MovieDetailsItem?>(null)

    val error = Field<Throwable>()
    var isLoading = Field<Boolean>(false)

    lateinit var imdbId: String

    fun loadDetails() = scope.launch {
        isLoading.value = true

        val result = try {
            withContext(IO) { get<OmdbRepository>().details(imdbId) }
        } catch (throwable: Throwable) {
            Timber.w(throwable)
            isLoading.value = false
            error.value = throwable
            return@launch
        }

        details.value = result
        isLoading.value = false
    }
}