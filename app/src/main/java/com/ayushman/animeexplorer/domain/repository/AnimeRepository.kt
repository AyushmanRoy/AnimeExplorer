package com.ayushman.animeexplorer.domain.repository

import com.ayushman.animeexplorer.domain.model.Anime
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface defining the contract for anime data operations
 * This follows the repository pattern and abstracts data sources
 */
interface AnimeRepository {

    /**
     * Get top anime from network and cache locally
     * @param page Page number for pagination
     * @param limit Number of items per page
     * @return Flow of anime list for reactive UI updates
     */
    fun getTopAnime(page: Int = 1, limit: Int = 20): Flow<List<Anime>>

    /**
     * Get anime details by ID
     * @param animeId The anime ID
     * @return Flow of anime details
     */
    fun getAnimeDetail(animeId: Int): Flow<Anime>

    /**
     * Search anime by query
     * @param query Search query
     * @param page Page number
     * @return Flow of search results
     */
    fun searchAnime(query: String, page: Int = 1): Flow<List<Anime>>

    /**
     * Get cached anime from local database
     * @return Flow of cached anime list
     */
    fun getCachedAnime(): Flow<List<Anime>>

    /**
     * Toggle favorite status of an anime
     * @param animeId The anime ID
     */
    suspend fun toggleFavorite(animeId: Int)

    /**
     * Get favorite anime
     * @return Flow of favorite anime list
     */
    fun getFavoriteAnime(): Flow<List<Anime>>
}