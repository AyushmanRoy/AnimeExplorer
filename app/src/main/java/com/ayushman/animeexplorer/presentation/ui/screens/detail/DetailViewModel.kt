package com.ayushman.animeexplorer.presentation.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayushman.animeexplorer.domain.repository.AnimeRepository
import com.ayushman.animeexplorer.domain.usecase.GetAnimeDetailUseCase
import com.ayushman.animeexplorer.presentation.states.AnimeDetailState
import com.ayushman.animeexplorer.presentation.states.UiState
import com.ayushman.animeexplorer.presentation.states.getDataOrNull
import com.ayushman.animeexplorer.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Anime Detail Screen
 * Manages UI state and business logic for anime details and video playback
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getAnimeDetailUseCase: GetAnimeDetailUseCase,
    private val animeRepository: AnimeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnimeDetailState())
    val uiState: StateFlow<AnimeDetailState> = _uiState.asStateFlow()

    /**
     * Load anime details by ID
     */
    fun loadAnimeDetail(animeId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(anime = UiState.Loading)

            getAnimeDetailUseCase(animeId)
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        anime = UiState.Error(
                            exception.message ?: Constants.GENERIC_ERROR
                        )
                    )
                }
                .collect { anime ->
                    _uiState.value = _uiState.value.copy(
                        anime = UiState.Success(anime),
                        isFavorite = anime.isFavorite
                    )
                }
        }
    }

    /**
     * Toggle favorite status of current anime
     */
    fun toggleFavorite() {
        val currentAnime = _uiState.value.anime.getDataOrNull() ?: return

        viewModelScope.launch {
            animeRepository.toggleFavorite(currentAnime.malId)

            // Update UI state immediately for better UX
            _uiState.value = _uiState.value.copy(
                isFavorite = !_uiState.value.isFavorite
            )
        }
    }

    /**
     * Set video playing state
     */
    fun setVideoPlaying(isPlaying: Boolean) {
        _uiState.value = _uiState.value.copy(isVideoPlaying = isPlaying)
    }
}