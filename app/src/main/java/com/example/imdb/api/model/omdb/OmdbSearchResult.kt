package com.example.imdb.api.model.omdb

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OmdbSearchResult(
    @Expose
    @SerializedName("Search")
    val search: List<OmdbSearchSubject>? = null,

    @Expose
    @SerializedName("totalResults")
    val total: String? = null
) : OmdbResult()