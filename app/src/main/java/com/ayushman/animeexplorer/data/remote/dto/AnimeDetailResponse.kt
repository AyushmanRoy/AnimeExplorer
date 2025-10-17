package com.ayushman.animeexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * API Response for anime details
 */
data class AnimeDetailResponse(
    @SerializedName("data")
    val data: AnimeDetailDto
)

data class AnimeDetailDto(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_english")
    val titleEnglish: String?,
    @SerializedName("title_japanese")
    val titleJapanese: String?,
    @SerializedName("images")
    val images: ImagesDto,
    @SerializedName("synopsis")
    val synopsis: String?,
    @SerializedName("score")
    val score: Double?,
    @SerializedName("episodes")
    val episodes: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("aired")
    val aired: AiredDto?,
    @SerializedName("genres")
    val genres: List<GenreDto>?,
    @SerializedName("trailer")
    val trailer: TrailerDto?,
    @SerializedName("characters")
    val characters: List<CharacterDto>?
)

data class CharacterDto(
    @SerializedName("character")
    val character: CharacterInfoDto,
    @SerializedName("role")
    val role: String?
)

data class CharacterInfoDto(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("images")
    val images: CharacterImagesDto?
)

data class CharacterImagesDto(
    @SerializedName("jpg")
    val jpg: ImageUrlDto
)