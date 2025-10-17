package com.ayushman.animeexplorer.domain.usecase

import com.ayushman.animeexplorer.domain.model.Anime
import com.ayushman.animeexplorer.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for searching anime
 * This handles the business logic for anime search functionality
 */
class SearchAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    /**
     * Execute the use case to search anime
     * @param query Search query
     * @param page Page number
     * @return Flow of search results
     */
    operator fun invoke(query: String, page: Int = 1): Flow<List<Anime>> {
        return repository.searchAnime(query, page)
    }
}