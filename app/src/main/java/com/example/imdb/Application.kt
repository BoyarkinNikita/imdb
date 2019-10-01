package com.example.imdb

import androidx.multidex.MultiDexApplication
import com.example.imdb.BuildConfig.DEBUG
import com.example.imdb.di.contextModule
import com.example.imdb.di.get
import com.example.imdb.di.networkModule
import kotlinx.coroutines.Job
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger
import timber.log.Timber

class Application : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Koin DI modules.

        startKoin {
            androidContext(this@Application)
            modules(listOf(contextModule, networkModule))
            logger(if (DEBUG) AndroidLogger() else EmptyLogger())
        }

        // Initialize logger.

        if (DEBUG) Timber.plant(Timber.DebugTree())
    }
}