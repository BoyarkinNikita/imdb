package com.example.imdb.di

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule
import com.example.imdb.config.CacheConfig
import com.example.imdb.config.ContextConfig

@GlideModule
class GlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val cacheSize = get<CacheConfig>().imageCacheSizeBytes
        val factory = InternalCacheDiskCacheFactory(context, "glide", cacheSize)
        builder.setDiskCache(factory)
    }
}