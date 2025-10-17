package com.ayushman.animeexplorer.data.local.database.dao

import androidx.room.*
import com.ayushman.animeexplorer.data.local.database.entities.CharacterEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for character operations
 */
@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters WHERE animeId = :animeId")
    fun getCharactersByAnimeId(animeId: Int): Flow<List<CharacterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("DELETE FROM characters WHERE animeId = :animeId")
    suspend fun deleteCharactersByAnimeId(animeId: Int)
}