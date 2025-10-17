package com.ayushman.animeexplorer.data.repository

import android.util.Log
import com.ayushman.animeexplorer.data.local.database.dao.AnimeDao
import com.ayushman.animeexplorer.data.local.database.dao.CharacterDao
import com.ayushman.animeexplorer.data.local.database.dao.GenreDao
import com.ayushman.animeexplorer.data.mapper.AnimeMapper
import com.ayushman.animeexplorer.data.remote.api.JikanApiService
import com.ayushman.animeexplorer.domain.model.Anime
import com.ayushman.animeexplorer.domain.repository.AnimeRepository
import com.ayushman.animeexplorer.utils.NetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository implementation following offline-first architecture
 * This class handles data from both local and remote sources
 */
@Singleton
class AnimeRepositoryImpl @Inject constructor(
    private val apiService: JikanApiService,
    private val animeDao: AnimeDao,
    private val genreDao: GenreDao,
    private val characterDao: CharacterDao,
    private val networkUtils: NetworkUtils
) : AnimeRepository {

    companion object {
        private const val TAG = "AnimeRepository"
    }

    /**
     * Get top anime with offline-first approach
     * 1. Emit cached data immediately
     * 2. Fetch fresh data from API if network available
     * 3. Update cache and emit fresh data
     */
    override fun getTopAnime(page: Int, limit: Int): Flow<List<Anime>> = flow {
        try {
            // First, emit cached data for immediate response
            animeDao.getAllAnime().collect { cachedAnime ->
                if (cachedAnime.isNotEmpty()) {
                    // Get genres for cached anime
                    val genreIds = cachedAnime.flatMap { it.genres }.distinct()
                    genreDao.getGenresByIds(genreIds).collect { genres ->
                        val genresMap = genres.groupBy { genre ->
                            cachedAnime.find { anime -> genre.malId in anime.genres }?.malId ?: 0
                        }.filterKeys { it != 0 }

                        val domainAnime = AnimeMapper.mapEntityListToDomainList(cachedAnime, genresMap)
                        emit(domainAnime)
                    }
                }

                // Then try to fetch fresh data if network is available
                if (networkUtils.isNetworkAvailable()) {
                    try {
                        val response = apiService.getTopAnime(page, limit)
                        if (response.isSuccessful) {
                            response.body()?.let { apiResponse ->
                                // Save genres first
                                apiResponse.data.forEach { animeDto ->
                                    animeDto.genres?.let { genresList ->
                                        val genreEntities = AnimeMapper.mapGenreDtoListToEntityList(genresList)
                                        genreDao.insertGenres(genreEntities)
                                    }
                                }

                                // Save anime data
                                val animeEntities = AnimeMapper.mapDtoListToEntityList(apiResponse.data)
                                animeDao.insertAnimeList(animeEntities)

                                Log.d(TAG, "Successfully fetched ${apiResponse.data.size} anime from API")
                            }
                        } else {
                            Log.e(TAG, "API call failed: ${response.code()} ${response.message()}")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching top anime", e)
                        // Don't throw error - offline-first means we continue with cached data
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in getTopAnime", e)
            // Emit empty list as fallback
            emit(emptyList())
        }
    }

    /**
     * Get anime details with offline-first approach
     */
    override fun getAnimeDetail(animeId: Int): Flow<Anime> = flow {
        try {
            // First check cache
            animeDao.getAnimeById(animeId).collect { cachedAnime ->
                cachedAnime?.let { entity ->
                    // Get related data
                    genreDao.getGenresByIds(entity.genres).collect { genres ->
                        characterDao.getCharactersByAnimeId(animeId).collect { characters ->
                            val domainAnime = AnimeMapper.mapEntityToDomain(entity, genres, characters)
                            emit(domainAnime)
                        }
                    }
                }

                // Try to fetch fresh details if network available
                if (networkUtils.isNetworkAvailable()) {
                    try {
                        val response = apiService.getAnimeDetail(animeId)
                        if (response.isSuccessful) {
                            response.body()?.let { detailResponse ->
                                val animeEntity = AnimeMapper.mapDetailDtoToEntity(detailResponse.data)

                                // Preserve favorite status if exists
                                cachedAnime?.let { cached ->
                                    animeEntity.copy(isFavorite = cached.isFavorite)
                                }?.let { updatedEntity ->
                                    animeDao.insertAnime(updatedEntity)
                                } ?: run {
                                    animeDao.insertAnime(animeEntity)
                                }

                                // Save genres
                                detailResponse.data.genres?.let { genresList ->
                                    val genreEntities = AnimeMapper.mapGenreDtoListToEntityList(genresList)
                                    genreDao.insertGenres(genreEntities)
                                }

                                // Save characters
                                detailResponse.data.characters?.let { charactersList ->
                                    characterDao.deleteCharactersByAnimeId(animeId) // Clear old data
                                    val characterEntities = AnimeMapper.mapCharacterDtoListToEntityList(charactersList, animeId)
                                    characterDao.insertCharacters(characterEntities)
                                }

                                Log.d(TAG, "Successfully fetched anime detail for ID: $animeId")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching anime detail", e)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in getAnimeDetail", e)
        }
    }

    /**
     * Search anime with caching
     */
    override fun searchAnime(query: String, page: Int): Flow<List<Anime>> = flow {
        try {
            if (networkUtils.isNetworkAvailable()) {
                val response = apiService.searchAnime(query, page)
                if (response.isSuccessful) {
                    response.body()?.let { searchResponse ->
                        // Save genres first
                        searchResponse.data.forEach { animeDto ->
                            animeDto.genres?.let { genresList ->
                                val genreEntities = AnimeMapper.mapGenreDtoListToEntityList(genresList)
                                genreDao.insertGenres(genreEntities)
                            }
                        }

                        // Convert to domain models for immediate emission
                        val domainAnime = searchResponse.data.map { dto ->
                            val genres = dto.genres?.map { genreDto ->
                                AnimeMapper.mapGenreEntityToDomain(
                                    AnimeMapper.mapGenreDtoToEntity(genreDto)
                                )
                            } ?: emptyList()

                            AnimeMapper.mapEntityToDomain(
                                AnimeMapper.mapDtoToEntity(dto),
                                genres.map { AnimeMapper.mapGenreDtoToEntity(
                                    com.ayushman.animeexplorer.data.remote.dto.GenreDto(it.malId, it.name)
                                )}
                            )
                        }

                        emit(domainAnime)

                        // Cache search results
                        val animeEntities = AnimeMapper.mapDtoListToEntityList(searchResponse.data)
                        animeDao.insertAnimeList(animeEntities)

                        Log.d(TAG, "Search returned ${searchResponse.data.size} results for query: $query")
                    }
                } else {
                    Log.e(TAG, "Search API call failed: ${response.code()}")
                    emit(emptyList())
                }
            } else {
                // Offline search in cached data (simple title matching)
                animeDao.getAllAnime().collect { cachedAnime ->
                    val filteredAnime = cachedAnime.filter { anime ->
                        anime.title.contains(query, ignoreCase = true) ||
                                anime.titleEnglish?.contains(query, ignoreCase = true) == true ||
                                anime.titleJapanese?.contains(query, ignoreCase = true) == true
                    }

                    if (filteredAnime.isNotEmpty()) {
                        val genreIds = filteredAnime.flatMap { it.genres }.distinct()
                        genreDao.getGenresByIds(genreIds).collect { genres ->
                            val genresMap = genres.groupBy { genre ->
                                filteredAnime.find { anime -> genre.malId in anime.genres }?.malId ?: 0
                            }.filterKeys { it != 0 }

                            val domainAnime = AnimeMapper.mapEntityListToDomainList(filteredAnime, genresMap)
                            emit(domainAnime)
                        }
                    } else {
                        emit(emptyList())
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in searchAnime", e)
            emit(emptyList())
        }
    }

    /**
     * Get cached anime from local database
     */
    override fun getCachedAnime(): Flow<List<Anime>> {
        return animeDao.getAllAnime().combine(
            genreDao.getAllGenres()
        ) { animeList, genresList ->
            val genresMap = genresList.groupBy { genre ->
                animeList.find { anime -> genre.malId in anime.genres }?.malId ?: 0
            }.filterKeys { it != 0 }

            AnimeMapper.mapEntityListToDomainList(animeList, genresMap)
        }
    }

    /**
     * Toggle favorite status of an anime
     */
    override suspend fun toggleFavorite(animeId: Int) {
        try {
            animeDao.getAnimeById(animeId).collect { anime ->
                anime?.let {
                    val newFavoriteStatus = !it.isFavorite
                    animeDao.updateFavoriteStatus(animeId, newFavoriteStatus)
                    Log.d(TAG, "Toggled favorite status for anime ID: $animeId to $newFavoriteStatus")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error toggling favorite status", e)
        }
    }

    /**
     * Get favorite anime
     */
    override fun getFavoriteAnime(): Flow<List<Anime>> {
        return animeDao.getFavoriteAnime().combine(
            genreDao.getAllGenres()
        ) { favoriteAnime, genresList ->
            val genresMap = genresList.groupBy { genre ->
                favoriteAnime.find { anime -> genre.malId in anime.genres }?.malId ?: 0
            }.filterKeys { it != 0 }

            AnimeMapper.mapEntityListToDomainList(favoriteAnime, genresMap)
        }
    }
}