package com.ayushman.animeexplorer.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for storing character data
 */
@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey
    val malId: Int,
    val animeId: Int, // Foreign key to anime
    val name: String,
    val imageUrl: String?,
    val role: String?
)