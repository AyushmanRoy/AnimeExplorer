package com.ayushman.animeexplorer.di

import com.ayushman.animeexplorer.data.repository.AnimeRepositoryImpl
import com.ayushman.animeexplorer.domain.repository.AnimeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for binding repository interfaces to implementations
 * This follows the dependency inversion principle
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds AnimeRepository interface to its implementation
     * This allows us to inject the interface and get the implementation
     */
    @Binds
    @Singleton
    abstract fun bindAnimeRepository(
        animeRepositoryImpl: AnimeRepositoryImpl
    ): AnimeRepository
}