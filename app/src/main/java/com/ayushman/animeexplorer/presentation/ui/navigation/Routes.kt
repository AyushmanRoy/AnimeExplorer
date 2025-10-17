package com.ayushman.animeexplorer.presentation.ui.navigation

/**
 * Navigation routes for the app
 * Centralized place to define all navigation destinations
 */
object Routes {
    const val HOME = "home"
    const val DETAIL = "detail/{animeId}"
    const val SEARCH = "search"
    const val FAVORITES = "favorites"

    /**
     * Create detail route with anime ID
     */
    fun createDetailRoute(animeId: Int): String {
        return "detail/$animeId"
    }
}