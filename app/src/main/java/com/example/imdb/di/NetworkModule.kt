package com.example.imdb.di

import com.example.imdb.BuildConfig.*
import com.example.imdb.api.NewsApi
import com.example.imdb.api.NewsClient
import com.example.imdb.api.OmdbApi
import com.example.imdb.api.OmdbClient
import com.example.imdb.config.NetworkConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private const val NETWORK_MODULE = "network."

val OMDB_OK_HTTP_QUALIFIER = named("${NETWORK_MODULE}omdb_ok_http")
val OMDB_RETROFIT_QUALIFIER = named("${NETWORK_MODULE}omdb_retrofit")

val NEWS_OK_HTTP_QUALIFIER = named("${NETWORK_MODULE}news_ok_http")
val NEWS_RETROFIT_QUALIFIER = named("${NETWORK_MODULE}news_retrofit")

val networkModule = module {
    single { NetworkConfig() }

    single {
        Cache(androidContext().cacheDir, get<NetworkConfig>().networkCacheSizeBytes)
    }

    single<Gson> {
        GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setLenient()
            .create()
    }

    single(OMDB_OK_HTTP_QUALIFIER) {
        newOkHttpClient()
    }

    single(OMDB_RETROFIT_QUALIFIER) {
        Retrofit.Builder()
            .client(get(OMDB_OK_HTTP_QUALIFIER))
            .baseUrl(OMDB_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single {
        get<Retrofit>(OMDB_RETROFIT_QUALIFIER).create(OmdbApi::class.java)
    }

    single { OmdbClient() }

    single(NEWS_OK_HTTP_QUALIFIER) {
        newOkHttpClient()
    }

    single(NEWS_RETROFIT_QUALIFIER) {
        Retrofit.Builder()
            .client(get(NEWS_OK_HTTP_QUALIFIER))
            .baseUrl(NEWS_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single {
        get<Retrofit>(NEWS_RETROFIT_QUALIFIER).create(NewsApi::class.java)
    }

    single { NewsClient() }
}

private fun newOkHttpClient(): OkHttpClient {
    val config = get<NetworkConfig>()

    val dispatcher = Dispatcher().apply {
        maxRequests = config.maxRequests
        maxRequestsPerHost = config.maxRequestPerHost
    }

    val logging = HttpLoggingInterceptor().apply {
        level = if (DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else HttpLoggingInterceptor.Level.NONE
    }

    return OkHttpClient.Builder()
        .dispatcher(dispatcher)
        .readTimeout(config.readTimeoutSeconds, TimeUnit.SECONDS)
        .writeTimeout(config.writeTimeoutSeconds, TimeUnit.SECONDS)
        .connectTimeout(config.connectTimeoutSeconds, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .cache(get())
        .build()
}