package com.ayushman.animeexplorer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class that initializes Hilt dependency injection
 * This is the entry point of our app and enables Hilt to manage dependencies
 */
@HiltAndroidApp
class AnimeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}