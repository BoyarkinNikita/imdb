package com.example.imdb.ui.movies.helper

import com.example.imdb.api.model.omdb.OmdbDetailsResult
import java.io.Serializable

data class MovieDetailsItem(
    val imdbId: String,
    val title: String,
    val type: MovieType,
    val description: String,
    val year: String?,
    val plot: String?,
    val posterUrl: String?,
    val rating: String?,
    val votes: String?,
    val director: String?,
    val writers: String?,
    val actors: String?,
    val boxOffice: String?,
    val website: String?
) : Serializable {
    companion object {
        fun fromResultOrThrow(result: OmdbDetailsResult): MovieDetailsItem = with(result) {
            if (imdbId == null) throw RuntimeException("Nullable IMDB identifier.")
            val title = title ?: "[ BLANK ]"
            val type = if (type == "series") MovieType.SERIES else MovieType.MOVIE
            val votes = votes?.replace("[^0-9]".toRegex(), "")
            val description = arrayOf(rated, runtime, genre).filterNotNull()
                .joinToString(separator = " | ")

            return MovieDetailsItem(
                imdbId = imdbId, title = title, type = type, description = description,
                year = year, plot = plot, posterUrl = posterUrl, rating = rating,
                votes = votes, director = director, writers = writers,
                actors = actors, boxOffice = boxOffice, website = website
            )
        }
    }
}
