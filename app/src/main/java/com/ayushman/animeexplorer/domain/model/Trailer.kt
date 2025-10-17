package com.ayushman.animeexplorer.domain.model

/**
 * Domain model representing an Anime Trailer
 */
data class Trailer(
    val youtubeId: String,
    val url: String,
    val embedUrl: String,
    val imageUrl: String? = null
)