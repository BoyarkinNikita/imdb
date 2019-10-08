package com.example.imdb.di

import com.example.imdb.BuildConfig.*
import com.example.imdb.config.CacheConfig
import com.example.imdb.storage.OmdbRepository
import com.example.imdb.utils.helper.TimedCache
import com.jakewharton.disklrucache.DiskLruCache
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val storageModule = module {
    single { CacheConfig() }

    single {
        val config = get<CacheConfig>()
        val cache = DiskLruCache.open(
            androidContext().cacheDir, VERSION_CODE,
            2, config.commonCacheSizeBytes
        )

        TimedCache(cache)
    }

    single { OmdbRepository() }
}