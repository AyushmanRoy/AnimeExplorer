package com.ayushman.animeexplorer.utils

/**
 * App-wide constants used throughout the application
 */
object Constants {
    // API Configuration
    const val BASE_URL = "https://api.jikan.moe/v4/"
    const val TOP_ANIME_ENDPOINT = "top/anime"
    const val ANIME_DETAIL_ENDPOINT = "anime/{id}"
    const val ANIME_SEARCH_ENDPOINT = "anime"

    // Database Configuration
    const val DATABASE_NAME = "anime_database"
    const val DATABASE_VERSION = 1

    // Network Configuration
    const val NETWORK_TIMEOUT = 30L // seconds
    const val CACHE_SIZE = 10 * 1024 * 1024L // 10 MB

    // Pagination
    const val DEFAULT_PAGE_SIZE = 20
    const val INITIAL_PAGE = 1

    // Image URLs
    const val PLACEHOLDER_IMAGE_URL = "https://via.placeholder.com/300x400"

    // Video Player
    const val VIDEO_CACHE_SIZE = 100 * 1024 * 1024L // 100 MB

    // Preferences
    const val PREFERENCES_NAME = "anime_explorer_prefs"
    const val LAST_SYNC_TIME = "last_sync_time"

    // Error Messages
    const val NETWORK_ERROR = "Network connection error"
    const val GENERIC_ERROR = "Something went wrong"
    const val NO_DATA_ERROR = "No data available"
}