package com.ayushman.animeexplorer.data.local.database.dao

import androidx.room.*
import com.ayushman.animeexplorer.data.local.database.entities.AnimeEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for anime operations
 * This defines how we interact with the anime table
 */
@Dao
interface AnimeDao {

    /**
     * Get all anime from database
     * @return Flow of anime list for reactive updates
     */
    @Query("SELECT * FROM anime ORDER BY lastUpdated DESC")
    fun getAllAnime(): Flow<List<AnimeEntity>>

    /**
     * Get anime by ID
     * @param malId Anime ID
     * @return Flow of anime entity
     */
    @Query("SELECT * FROM anime WHERE malId = :malId")
    fun getAnimeById(malId: Int): Flow<AnimeEntity?>

    /**
     * Get favorite anime
     * @return Flow of favorite anime list
     */
    @Query("SELECT * FROM anime WHERE isFavorite = 1 ORDER BY lastUpdated DESC")
    fun getFavoriteAnime(): Flow<List<AnimeEntity>>

    /**
     * Insert anime list
     * @param animeList List of anime to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeList(animeList: List<AnimeEntity>)

    /**
     * Insert single anime
     * @param anime Anime to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)

    /**
     * Update anime
     * @param anime Anime to update
     */
    @Update
    suspend fun updateAnime(anime: AnimeEntity)

    /**
     * Delete anime
     * @param anime Anime to delete
     */
    @Delete
    suspend fun deleteAnime(anime: AnimeEntity)

    /**
     * Toggle favorite status
     * @param malId Anime ID
     * @param isFavorite New favorite status
     */
    @Query("UPDATE anime SET isFavorite = :isFavorite WHERE malId = :malId")
    suspend fun updateFavoriteStatus(malId: Int, isFavorite: Boolean)

    /**
     * Clear all anime (for refresh)
     */
    @Query("DELETE FROM anime WHERE isFavorite = 0")
    suspend fun clearNonFavoriteAnime()
}