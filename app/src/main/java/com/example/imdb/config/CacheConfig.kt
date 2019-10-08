package com.example.imdb.config

class CacheConfig {
    val imageCacheSizeBytes =50L * 1_024 * 1_024 // 50 Mb
    val networkCacheSizeBytes = 10L * 1_024 * 1_024 // 10 Mb
    val commonCacheSizeBytes = 40L * 1_024 * 1_024 // 40 Mb
}