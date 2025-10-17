package com.ayushman.animeexplorer.presentation.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayushman.animeexplorer.domain.usecase.GetTopAnimeUseCase
import com.ayushman.animeexplorer.domain.usecase.SearchAnimeUseCase
import com.ayushman.animeexplorer.presentation.states.AnimeListState
import com.ayushman.animeexplorer.presentation.states.UiState
import com.ayushman.animeexplorer.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Home Screen
 * Manages UI state and business logic for the anime list screen
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopAnimeUseCase: GetTopAnimeUseCase,
    private val searchAnimeUseCase: SearchAnimeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnimeListState())
    val uiState: StateFlow<AnimeListState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadTopAnime()
    }

    /**
     * Load top anime from repository
     */
    fun loadTopAnime() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(animeList = UiState.Loading)

            getTopAnimeUseCase(page = 1, limit = Constants.DEFAULT_PAGE_SIZE)
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        animeList = UiState.Error(
                            exception.message ?: Constants.GENERIC_ERROR
                        )
                    )
                }
                .collect { animeList ->
                    _uiState.value = _uiState.value.copy(
                        animeList = if (animeList.isEmpty()) {
                            UiState.Empty
                        } else {
                            UiState.Success(animeList)
                        }
                    )
                }
        }
    }

    /**
     * Refresh anime list
     */
    fun refreshAnime() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)

            getTopAnimeUseCase(page = 1, limit = Constants.DEFAULT_PAGE_SIZE)
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        animeList = UiState.Error(
                            exception.message ?: Constants.GENERIC_ERROR
                        ),
                        isRefreshing = false
                    )
                }
                .collect { animeList ->
                    _uiState.value = _uiState.value.copy(
                        animeList = if (animeList.isEmpty()) {
                            UiState.Empty
                        } else {
                            UiState.Success(animeList)
                        },
                        isRefreshing = false
                    )
                }
        }
    }

    /**
     * Search anime with debounced input
     */
    fun searchAnime(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)

        // Cancel previous search job
        searchJob?.cancel()

        if (query.isBlank()) {
            loadTopAnime()
            return
        }

        searchJob = viewModelScope.launch {
            // Debounce search to avoid too many API calls
            delay(500)

            _uiState.value = _uiState.value.copy(
                animeList = UiState.Loading,
                isSearching = true
            )

            searchAnimeUseCase(query = query, page = 1)
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        animeList = UiState.Error(
                            exception.message ?: Constants.GENERIC_ERROR
                        ),
                        isSearching = false
                    )
                }
                .collect { searchResults ->
                    _uiState.value = _uiState.value.copy(
                        animeList = if (searchResults.isEmpty()) {
                            UiState.Empty
                        } else {
                            UiState.Success(searchResults)
                        },
                        isSearching = false
                    )
                }
        }
    }

    /**
     * Clear search and return to top anime
     */
    fun clearSearch() {
        searchJob?.cancel()
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            isSearching = false
        )
        loadTopAnime()
    }
}