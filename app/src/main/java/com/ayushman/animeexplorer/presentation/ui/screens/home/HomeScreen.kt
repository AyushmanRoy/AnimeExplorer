package com.ayushman.animeexplorer.presentation.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator // New import
import androidx.compose.material.pullrefresh.pullRefresh // New import
import androidx.compose.material.pullrefresh.rememberPullRefreshState // New import
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment // New import
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayushman.animeexplorer.presentation.states.UiState
import com.ayushman.animeexplorer.presentation.ui.components.*

/**
 * Home screen displaying anime list with search functionality
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    onAnimeClick: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // 1. Updated state for pull-to-refresh
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = viewModel::refreshAnime
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("AnimeExplorer")
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::searchAnime,
                onClearClick = viewModel::clearSearch,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // 2. Wrap the content in a Box and apply the .pullRefresh modifier
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                when (val animeListState = uiState.animeList) {
                    is UiState.Loading -> {
                        LoadingIndicator(
                            message = if (uiState.isSearching) "Searching..." else "Loading anime..."
                        )
                    }

                    is UiState.Success -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(animeListState.data) { anime ->
                                AnimeCard(
                                    anime = anime,
                                    onClick = { onAnimeClick(anime.malId) }
                                )
                            }
                        }
                    }

                    is UiState.Error -> {
                        ErrorComponent(
                            message = animeListState.message,
                            onRetryClick = {
                                if (uiState.searchQuery.isBlank()) {
                                    viewModel.loadTopAnime()
                                } else {
                                    viewModel.searchAnime(uiState.searchQuery)
                                }
                            }
                        )
                    }

                    is UiState.Empty -> {
                        ErrorComponent(
                            message = if (uiState.searchQuery.isBlank()) {
                                "No anime found"
                            } else {
                                "No results for \"${uiState.searchQuery}\""
                            },
                            onRetryClick = {
                                if (uiState.searchQuery.isNotBlank()) {
                                    viewModel.clearSearch()
                                }
                            }
                        )
                    }
                }

                // 3. Add the indicator, aligned to the top center of the Box
                PullRefreshIndicator(
                    refreshing = uiState.isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}
