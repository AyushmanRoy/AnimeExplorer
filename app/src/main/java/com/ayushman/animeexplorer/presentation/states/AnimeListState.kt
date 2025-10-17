package com.ayushman.animeexplorer.presentation.states

import com.ayushman.animeexplorer.domain.model.Anime

/**
 * UI state specific to anime list screens
 */
data class AnimeListState(
    val animeList: UiState<List<Anime>> = UiState.Loading,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val isSearching: Boolean = false
)