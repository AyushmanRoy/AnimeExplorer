package com.ayushman.animeexplorer.presentation.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ayushman.animeexplorer.presentation.states.UiState
import com.ayushman.animeexplorer.presentation.ui.components.*
import com.ayushman.animeexplorer.presentation.ui.theme.AnimeOrange

/**
 * Detail screen showing comprehensive anime information
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    animeId: Int,
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(animeId) {
        viewModel.loadAnimeDetail(animeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anime Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (uiState.anime is UiState.Success) {
                        IconButton(onClick = viewModel::toggleFavorite) {
                            Icon(
                                imageVector = if (uiState.isFavorite) {
                                    Icons.Default.Favorite
                                } else {
                                    Icons.Default.FavoriteBorder
                                },
                                contentDescription = "Toggle Favorite",
                                tint = if (uiState.isFavorite) AnimeOrange else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val animeState = uiState.anime) {
            is UiState.Loading -> {
                LoadingIndicator(message = "Loading anime details...")
            }

            is UiState.Success -> {
                val anime = animeState.data

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        // Anime Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Poster Image
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(anime.imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = anime.displayTitle,
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            // Basic Info
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = anime.displayTitle,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                anime.titleJapanese?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Score: ${anime.displayScore}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    text = anime.displayEpisodes,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                anime.status?.let {
                                    Text(
                                        text = "Status: $it",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }

                    // Trailer
                    if (anime.trailer != null) {
                        item {
                            Column {
                                Text(
                                    text = "Trailer",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                VideoPlayer(
                                    trailer = anime.trailer,
                                    onPlayingStateChange = viewModel::setVideoPlaying
                                )
                            }
                        }
                    }

                    // Synopsis
                    if (!anime.synopsis.isNullOrBlank()) {
                        item {
                            Column {
                                Text(
                                    text = "Synopsis",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = anime.synopsis,
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                                )
                            }
                        }
                    }

                    // Genres
                    if (anime.genres.isNotEmpty()) {
                        item {
                            Column {
                                Text(
                                    text = "Genres",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(anime.genres) { genre ->
                                        AssistChip(
                                            onClick = { },
                                            label = { Text(genre.name) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Characters
                    if (anime.characters.isNotEmpty()) {
                        item {
                            Column {
                                Text(
                                    text = "Characters",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(anime.characters.take(10)) { character ->
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.width(80.dp)
                                        ) {
                                            AsyncImage(
                                                model = character.imageUrl,
                                                contentDescription = character.name,
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .clip(RoundedCornerShape(30.dp)),
                                                contentScale = ContentScale.Crop
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = character.name,
                                                style = MaterialTheme.typography.bodySmall,
                                                maxLines = 2,
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is UiState.Error -> {
                ErrorComponent(
                    message = animeState.message,
                    onRetryClick = { viewModel.loadAnimeDetail(animeId) }
                )
            }

            is UiState.Empty -> {
                ErrorComponent(message = "Anime not found")
            }
        }
    }
}