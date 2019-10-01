package com.example.imdb.api

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("/v2/everything")
    suspend fun search(
        @Query("apikey") key: String,
        @Query("t") query: String
    ): String
}

class NewsClient {

}