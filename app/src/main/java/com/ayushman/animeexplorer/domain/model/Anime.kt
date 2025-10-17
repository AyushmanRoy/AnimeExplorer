package com.ayushman.animeexplorer.domain.model

/**
 * Domain model representing an Anime
 * This is our pure business logic model, independent of data sources
 */
data class Anime(
    val malId: Int,
    val title: String,
    val titleEnglish: String? = null,
    val titleJapanese: String? = null,
    val imageUrl: String,
    val synopsis: String? = null,
    val score: Double? = null,
    val episodes: Int? = null,
    val status: String? = null,
    val aired: String? = null,
    val genres: List<Genre> = emptyList(),
    val characters: List<Character> = emptyList(),
    val trailer: Trailer? = null,
    val isFavorite: Boolean = false
) {
    // Helper properties for UI
    val displayTitle: String
        get() = titleEnglish ?: title

    val displayScore: String
        get() = score?.let { "★ ${"%.1f".format(it)}" } ?: "N/A"

    val displayEpisodes: String
        get() = episodes?.let { "$it episodes" } ?: "Unknown"
}