package com.example.imdb.storage

import com.example.imdb.api.OmdbClient
import com.example.imdb.di.get
import com.example.imdb.utils.helper.TimedCache
import java.util.concurrent.TimeUnit

class OmdbRepository {
    suspend fun search(
        query: String,
        page: Int = 1
    ) = get<TimedCache>().run {
        val key = cacheKey("${OMDB_PREFIX}search", query, page)
        getOrPut(key, suspend {
            get<OmdbClient>().search(query, page)
        }, TimeUnit.MINUTES.toMillis(3))
    }

    suspend fun details(
        imdbId: String
    ) = get<TimedCache>().run {
        val key = cacheKey("${OMDB_PREFIX}details", imdbId)
        getOrPut(key, suspend {
            get<OmdbClient>().details(imdbId)
        }, TimeUnit.MINUTES.toMillis(2))
    }

    companion object {
        private const val OMDB_PREFIX = "omdb"
    }
}