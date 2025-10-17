package com.ayushman.animeexplorer.presentation.states

import com.ayushman.animeexplorer.domain.model.Anime

/**
 * UI state specific to anime detail screen
 */
data class AnimeDetailState(
    val anime: UiState<Anime> = UiState.Loading,
    val isVideoPlaying: Boolean = false,
    val isFavorite: Boolean = false
)