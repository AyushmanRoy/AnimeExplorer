package com.ayushman.animeexplorer.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ayushman.animeexplorer.data.local.database.converters.Converters

/**
 * Room entity for storing anime data locally
 * This is our local database representation of anime
 */
@Entity(tableName = "anime")
@TypeConverters(Converters::class)
data class AnimeEntity(
    @PrimaryKey
    val malId: Int,
    val title: String,
    val titleEnglish: String?,
    val titleJapanese: String?,
    val imageUrl: String,
    val synopsis: String?,
    val score: Double?,
    val episodes: Int?,
    val status: String?,
    val aired: String?,
    val genres: List<Int>, // Store genre IDs
    val trailerYoutubeId: String?,
    val trailerUrl: String?,
    val trailerEmbedUrl: String?,
    val trailerImageUrl: String?,
    val isFavorite: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)