package com.example.imdb.ui.movies.helper

import com.example.imdb.api.model.omdb.OmdbSearchSubject
import com.example.imdb.utils.diff.DiffItem
import java.io.Serializable

data class MovieItem(
    val imdbId: String,
    val title: String,
    val type: MovieType,
    val year: String?,
    val posterUrl: String?
) : DiffItem, Serializable {
    override fun id(): Any = imdbId

    companion object {
        fun fromSubjectOrThrow(subject: OmdbSearchSubject): MovieItem = with(subject) {
            if (imdbId == null) throw RuntimeException("Nullable IMDB identifier.")
            val title = title ?: "[ BLANK ]"
            val type = if (type == "series") MovieType.SERIES else MovieType.MOVIE
            return MovieItem(
                imdbId = imdbId, title = title, type = type,
                year = year, posterUrl = posterUrl
            )
        }
    }
}
