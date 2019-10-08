package com.example.imdb.api.model

class Result<T : Any>(
    val data: T? = null,
    val throwable: Throwable? = null
) {
    fun getDataOrThrow(): T =
        data ?: throw (throwable ?: IllegalStateException("Unknown exception."))
}