package com.example.imdb.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OmdbSearchSubject(
    @Expose
    @SerializedName("Title")
    val title: String? = null,

    @Expose
    @SerializedName("Year")
    val year: String? = null,

    @Expose
    @SerializedName("Type")
    val type: String? = null,

    @Expose
    @SerializedName("Poster")
    val posterUrl: String? = null,

    @Expose
    @SerializedName("imdbID")
    val imdbId: String? = null
) : OmdbResult()