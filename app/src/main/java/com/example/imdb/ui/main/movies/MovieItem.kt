package com.example.imdb.ui.main.movies

import com.example.imdb.api.model.OmdbSearchSubject

data class MovieItem(
    val imdbId: String,
    val title: String,
    val year: String?,
    val type: Type,
    val posterUrl: String?
) {
    enum class Type { MOVIE, SERIES }

    companion object {
        fun fromSubjectOrThrow(subject: OmdbSearchSubject): MovieItem = with(subject) {
            if (imdbId == null) throw RuntimeException("Nullable IMDB identifier.")
            val title = title ?: "[ BLANK ]"
            val type = if (type == "series") Type.SERIES else Type.MOVIE
            return MovieItem(imdbId, title, year, type, posterUrl)
        }
    }
}
