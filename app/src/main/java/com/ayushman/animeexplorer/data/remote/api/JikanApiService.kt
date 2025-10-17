package com.ayushman.animeexplorer.data.remote.api

import com.ayushman.animeexplorer.data.remote.dto.AnimeDetailResponse
import com.ayushman.animeexplorer.data.remote.dto.AnimeListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API interface for Jikan API
 * This defines all the endpoints we'll use to fetch anime data
 */
interface JikanApiService {

    /**
     * Get top anime list
     * @param page Page number (default: 1)
     * @param limit Number of items per page (default: 25, max: 25)
     * @return Response containing anime list
     */
    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 25
    ): Response<AnimeListResponse>

    /**
     * Get anime details by ID
     * @param id Anime ID
     * @return Response containing anime details
     */
    @GET("anime/{id}/full")
    suspend fun getAnimeDetail(
        @Path("id") id: Int
    ): Response<AnimeDetailResponse>

    /**
     * Search anime by query
     * @param query Search query
     * @param page Page number
     * @param limit Number of items per page
     * @return Response containing search results
     */
    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 25
    ): Response<AnimeListResponse>

    /**
     * Get anime characters
     * @param id Anime ID
     * @return Response containing character list
     */
    @GET("anime/{id}/characters")
    suspend fun getAnimeCharacters(
        @Path("id") id: Int
    ): Response<AnimeListResponse>
}