package com.example.imdb.api.model.omdb

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OmdbDetailsResult(
    @Expose
    @SerializedName("Title")
    val title: String? = null,

    @Expose
    @SerializedName("Year")
    val year: String? = null,

    @Expose
    @SerializedName("Rated")
    val rated: String? = null,

    @Expose
    @SerializedName("Released")
    val released: String? = null,

    @Expose
    @SerializedName("Runtime")
    val runtime: String? = null,

    @Expose
    @SerializedName("Genre")
    val genre: String? = null,

    @Expose
    @SerializedName("Director")
    val director: String? = null,

    @Expose
    @SerializedName("Writer")
    val writer: String? = null,

    @Expose
    @SerializedName("Actors")
    val actors: String? = null,

    @Expose
    @SerializedName("Plot")
    val plot: String? = null,

    @Expose
    @SerializedName("Language")
    val language: String? = null,

    @Expose
    @SerializedName("Country")
    val country: String? = null,

    @Expose
    @SerializedName("Awards")
    val awards: String? = null,

    @Expose
    @SerializedName("Poster")
    val posterUrl: String? = null,

    @Expose
    @SerializedName("imdbRating")
    val rating: String? = null,

    @Expose
    @SerializedName("imdbVotes")
    val votes: String? = null,

    @Expose
    @SerializedName("imdbID")
    val imdbId: String? = null,

    @Expose
    @SerializedName("Type")
    val type: String? = null,

    @Expose
    @SerializedName("DVD")
    val dvd: String? = null,

    @Expose
    @SerializedName("BoxOffice")
    val boxOffice: String? = null,

    @Expose
    @SerializedName("Production")
    val production: String? = null,

    @Expose
    @SerializedName("Website")
    val website: String? = null
) : OmdbResult()