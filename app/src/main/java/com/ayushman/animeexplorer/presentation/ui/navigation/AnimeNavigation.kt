package com.ayushman.animeexplorer.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ayushman.animeexplorer.presentation.ui.screens.detail.DetailScreen
import com.ayushman.animeexplorer.presentation.ui.screens.home.HomeScreen

/**
 * Main navigation component for the app
 * Sets up navigation between different screens
 */
@Composable
fun AnimeNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        // Home Screen - displays anime list
        composable(Routes.HOME) {
            HomeScreen(
                onAnimeClick = { animeId ->
                    navController.navigate(Routes.createDetailRoute(animeId))
                }
            )
        }

        // Detail Screen - displays anime details
        composable(Routes.DETAIL) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getString("animeId")?.toIntOrNull()

            if (animeId != null) {
                DetailScreen(
                    animeId = animeId,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}