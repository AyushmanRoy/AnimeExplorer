package com.ayushman.animeexplorer.domain.usecase

import com.ayushman.animeexplorer.domain.model.Anime
import com.ayushman.animeexplorer.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting anime details
 * This handles the business logic for fetching detailed anime information
 */
class GetAnimeDetailUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    /**
     * Execute the use case to get anime details
     * @param animeId The anime ID
     * @return Flow of anime details
     */
    operator fun invoke(animeId: Int): Flow<Anime> {
        return repository.getAnimeDetail(animeId)
    }
}