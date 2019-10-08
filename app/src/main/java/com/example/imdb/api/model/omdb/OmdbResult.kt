package com.example.imdb.api.model.omdb

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

abstract class OmdbResult(
    @Expose
    @SerializedName("Response")
    val response: String? = null,

    @Expose
    @SerializedName("Error")
    val error: String? = null
)