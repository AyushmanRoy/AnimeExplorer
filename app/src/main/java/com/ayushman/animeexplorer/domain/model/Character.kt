package com.ayushman.animeexplorer.domain.model

/**
 * Domain model representing an Anime Character
 */
data class Character(
    val malId: Int,
    val name: String,
    val imageUrl: String? = null,
    val role: String? = null
)