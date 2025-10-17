package com.ayushman.animeexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * API Response for anime list
 * This mirrors the exact structure of Jikan API response
 */
data class AnimeListResponse(
    @SerializedName("data")
    val data: List<AnimeResponseDto>,
    @SerializedName("pagination")
    val pagination: PaginationDto
)

data class AnimeResponseDto(
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
    val trailer: TrailerDto?
)

data class ImagesDto(
    @SerializedName("jpg")
    val jpg: ImageUrlDto,
    @SerializedName("webp")
    val webp: ImageUrlDto?
)

data class ImageUrlDto(
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("small_image_url")
    val smallImageUrl: String?,
    @SerializedName("large_image_url")
    val largeImageUrl: String?
)

data class AiredDto(
    @SerializedName("string")
    val string: String?
)

data class GenreDto(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("name")
    val name: String
)

data class TrailerDto(
    @SerializedName("youtube_id")
    val youtubeId: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("embed_url")
    val embedUrl: String?,
    @SerializedName("images")
    val images: TrailerImagesDto?
)

data class TrailerImagesDto(
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("small_image_url")
    val smallImageUrl: String?,
    @SerializedName("medium_image_url")
    val mediumImageUrl: String?,
    @SerializedName("large_image_url")
    val largeImageUrl: String?,
    @SerializedName("maximum_image_url")
    val maximumImageUrl: String?
)

data class PaginationDto(
    @SerializedName("last_visible_page")
    val lastVisiblePage: Int,
    @SerializedName("has_next_page")
    val hasNextPage: Boolean,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("items")
    val items: ItemsDto
)

data class ItemsDto(
    @SerializedName("count")
    val count: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("per_page")
    val perPage: Int
)