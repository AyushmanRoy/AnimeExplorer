package com.ayushman.animeexplorer.data.local.database.dao

import androidx.room.*
import com.ayushman.animeexplorer.data.local.database.entities.GenreEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for genre operations
 */
@Dao
interface GenreDao {

    @Query("SELECT * FROM genres")
    fun getAllGenres(): Flow<List<GenreEntity>>

    @Query("SELECT * FROM genres WHERE malId IN (:genreIds)")
    fun getGenresByIds(genreIds: List<Int>): Flow<List<GenreEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<GenreEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenre(genre: GenreEntity)
}