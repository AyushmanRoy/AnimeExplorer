package com.ayushman.animeexplorer.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for storing genre data
 */
@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey
    val malId: Int,
    val name: String
)