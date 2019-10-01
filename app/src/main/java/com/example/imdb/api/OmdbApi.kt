package com.example.imdb.api

import com.example.imdb.api.model.OmdbResult
import com.example.imdb.api.model.OmdbSearchResult
import com.example.imdb.config.NetworkConfig
import com.example.imdb.di.get
import com.example.imdb.ui.main.movies.MovieItem
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    @GET(".")
    suspend fun search(
        @Query("apikey") key: String,
        @Query("s") query: String,
        @Query("page") page: Int
    ): OmdbSearchResult
}

class OmdbClient {
    suspend fun search(
        query: String,
        page: Int = 1
    ): Pair<List<MovieItem>, Int> {
        val key = get<NetworkConfig>().omdbKeys.random()
        val result = get<OmdbApi>().search(key, query, page).apply {
            if (response != "True") throw RuntimeException(error ?: "Unknown error!")
        }

        val total = result.total?.toIntOrNull() ?: throw RuntimeException("Wrong total.")
        val movies = result.search?.map { MovieItem.fromSubjectOrThrow(it) } ?: listOf()
        return movies to total
    }
}