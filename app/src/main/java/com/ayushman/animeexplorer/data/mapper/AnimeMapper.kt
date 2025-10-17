package com.ayushman.animeexplorer.data.mapper

import com.ayushman.animeexplorer.data.local.database.entities.AnimeEntity
import com.ayushman.animeexplorer.data.local.database.entities.CharacterEntity
import com.ayushman.animeexplorer.data.local.database.entities.GenreEntity
import com.ayushman.animeexplorer.data.remote.dto.AnimeResponseDto
import com.ayushman.animeexplorer.data.remote.dto.AnimeDetailDto
import com.ayushman.animeexplorer.data.remote.dto.CharacterDto
import com.ayushman.animeexplorer.data.remote.dto.GenreDto
import com.ayushman.animeexplorer.domain.model.Anime
import com.ayushman.animeexplorer.domain.model.Character
import com.ayushman.animeexplorer.domain.model.Genre
import com.ayushman.animeexplorer.domain.model.Trailer

/**
 * Mapper class to convert between different data representations
 * This follows the Clean Architecture principle of separating data layers
 */
object AnimeMapper {

    // DTO to Entity mappings (API response to database)

    /**
     * Convert API response DTO to database entity
     */
    fun mapDtoToEntity(dto: AnimeResponseDto): AnimeEntity {
        return AnimeEntity(
            malId = dto.malId,
            title = dto.title,
            titleEnglish = dto.titleEnglish,
            titleJapanese = dto.titleJapanese,
            imageUrl = dto.images.jpg.imageUrl,
            synopsis = dto.synopsis,
            score = dto.score,
            episodes = dto.episodes,
            status = dto.status,
            aired = dto.aired?.string,
            genres = dto.genres?.map { it.malId } ?: emptyList(),
            trailerYoutubeId = dto.trailer?.youtubeId,
            trailerUrl = dto.trailer?.url,
            trailerEmbedUrl = dto.trailer?.embedUrl,
            trailerImageUrl = dto.trailer?.images?.imageUrl,
            isFavorite = false,
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Convert detailed API response to database entity
     */
    fun mapDetailDtoToEntity(dto: AnimeDetailDto): AnimeEntity {
        return AnimeEntity(
            malId = dto.malId,
            title = dto.title,
            titleEnglish = dto.titleEnglish,
            titleJapanese = dto.titleJapanese,
            imageUrl = dto.images.jpg.imageUrl,
            synopsis = dto.synopsis,
            score = dto.score,
            episodes = dto.episodes,
            status = dto.status,
            aired = dto.aired?.string,
            genres = dto.genres?.map { it.malId } ?: emptyList(),
            trailerYoutubeId = dto.trailer?.youtubeId,
            trailerUrl = dto.trailer?.url,
            trailerEmbedUrl = dto.trailer?.embedUrl,
            trailerImageUrl = dto.trailer?.images?.imageUrl,
            isFavorite = false,
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Convert genre DTO to entity
     */
    fun mapGenreDtoToEntity(dto: GenreDto): GenreEntity {
        return GenreEntity(
            malId = dto.malId,
            name = dto.name
        )
    }

    /**
     * Convert character DTO to entity
     */
    fun mapCharacterDtoToEntity(dto: CharacterDto, animeId: Int): CharacterEntity {
        return CharacterEntity(
            malId = dto.character.malId,
            animeId = animeId,
            name = dto.character.name,
            imageUrl = dto.character.images?.jpg?.imageUrl,
            role = dto.role
        )
    }

    // Entity to Domain mappings (database to business logic)

    /**
     * Convert database entity to domain model
     */
    fun mapEntityToDomain(
        entity: AnimeEntity,
        genres: List<GenreEntity> = emptyList(),
        characters: List<CharacterEntity> = emptyList()
    ): Anime {
        return Anime(
            malId = entity.malId,
            title = entity.title,
            titleEnglish = entity.titleEnglish,
            titleJapanese = entity.titleJapanese,
            imageUrl = entity.imageUrl,
            synopsis = entity.synopsis,
            score = entity.score,
            episodes = entity.episodes,
            status = entity.status,
            aired = entity.aired,
            genres = genres.map { mapGenreEntityToDomain(it) },
            characters = characters.map { mapCharacterEntityToDomain(it) },
            trailer = if (entity.trailerYoutubeId != null) {
                Trailer(
                    youtubeId = entity.trailerYoutubeId,
                    url = entity.trailerUrl ?: "",
                    embedUrl = entity.trailerEmbedUrl ?: "",
                    imageUrl = entity.trailerImageUrl
                )
            } else null,
            isFavorite = entity.isFavorite
        )
    }

    /**
     * Convert genre entity to domain model
     */
    fun mapGenreEntityToDomain(entity: GenreEntity): Genre {
        return Genre(
            malId = entity.malId,
            name = entity.name
        )
    }

    /**
     * Convert character entity to domain model
     */
    fun mapCharacterEntityToDomain(entity: CharacterEntity): Character {
        return Character(
            malId = entity.malId,
            name = entity.name,
            imageUrl = entity.imageUrl,
            role = entity.role
        )
    }

    // Convenience methods for list mappings

    fun mapDtoListToEntityList(dtoList: List<AnimeResponseDto>): List<AnimeEntity> {
        return dtoList.map { mapDtoToEntity(it) }
    }

    fun mapGenreDtoListToEntityList(dtoList: List<GenreDto>): List<GenreEntity> {
        return dtoList.map { mapGenreDtoToEntity(it) }
    }

    fun mapCharacterDtoListToEntityList(dtoList: List<CharacterDto>, animeId: Int): List<CharacterEntity> {
        return dtoList.map { mapCharacterDtoToEntity(it, animeId) }
    }

    fun mapEntityListToDomainList(
        entityList: List<AnimeEntity>,
        genresMap: Map<Int, List<GenreEntity>> = emptyMap(),
        charactersMap: Map<Int, List<CharacterEntity>> = emptyMap()
    ): List<Anime> {
        return entityList.map { entity ->
            mapEntityToDomain(
                entity = entity,
                genres = genresMap[entity.malId] ?: emptyList(),
                characters = charactersMap[entity.malId] ?: emptyList()
            )
        }
    }
}