package com.ayushman.animeexplorer.di

import android.content.Context
import androidx.room.Room
import com.ayushman.animeexplorer.data.local.database.AnimeDatabase
import com.ayushman.animeexplorer.data.local.database.dao.AnimeDao
import com.ayushman.animeexplorer.data.local.database.dao.CharacterDao
import com.ayushman.animeexplorer.data.local.database.dao.GenreDao
import com.ayushman.animeexplorer.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database-related dependencies
 * This module creates and configures Room database and DAOs
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides Room database instance
     * Uses singleton pattern to ensure only one instance exists
     */
    @Provides
    @Singleton
    fun provideAnimeDatabase(@ApplicationContext context: Context): AnimeDatabase {
        return Room.databaseBuilder(
            context,
            AnimeDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // For development - remove in production
            .build()
    }

    /**
     * Provides AnimeDao from database
     */
    @Provides
    fun provideAnimeDao(database: AnimeDatabase): AnimeDao {
        return database.animeDao()
    }

    /**
     * Provides GenreDao from database
     */
    @Provides
    fun provideGenreDao(database: AnimeDatabase): GenreDao {
        return database.genreDao()
    }

    /**
     * Provides CharacterDao from database
     */
    @Provides
    fun provideCharacterDao(database: AnimeDatabase): CharacterDao {
        return database.characterDao()
    }
}