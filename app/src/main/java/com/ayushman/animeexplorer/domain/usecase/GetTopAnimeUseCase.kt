package com.ayushman.animeexplorer.domain.usecase

import com.ayushman.animeexplorer.domain.model.Anime
import com.ayushman.animeexplorer.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting top anime
 * This encapsulates the business logic for fetching top anime
 */
class GetTopAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    /**
     * Execute the use case to get top anime
     * @param page Page number for pagination
     * @param limit Number of items per page
     * @return Flow of anime list
     */
    operator fun invoke(page: Int = 1, limit: Int = 20): Flow<List<Anime>> {
        return repository.getTopAnime(page, limit)
    }
}