package com.ayushman.animeexplorer.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.ayushman.animeexplorer.data.local.database.converters.Converters
import com.ayushman.animeexplorer.data.local.database.dao.AnimeDao
import com.ayushman.animeexplorer.data.local.database.dao.CharacterDao
import com.ayushman.animeexplorer.data.local.database.dao.GenreDao
import com.ayushman.animeexplorer.data.local.database.entities.AnimeEntity
import com.ayushman.animeexplorer.data.local.database.entities.CharacterEntity
import com.ayushman.animeexplorer.data.local.database.entities.GenreEntity
import com.ayushman.animeexplorer.utils.Constants

/**
 * Room database configuration
 * This is the main database class that holds all tables and DAOs
 */
@Database(
    entities = [
        AnimeEntity::class,
        GenreEntity::class,
        CharacterEntity::class
    ],
    version = Constants.DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AnimeDatabase : RoomDatabase() {

    // Abstract methods to get DAOs
    abstract fun animeDao(): AnimeDao
    abstract fun genreDao(): GenreDao
    abstract fun characterDao(): CharacterDao

    companion object {
        @Volatile
        private var INSTANCE: AnimeDatabase? = null

        /**
         * Get database instance using singleton pattern
         * This ensures we only have one instance of the database
         */
        fun getDatabase(context: Context): AnimeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimeDatabase::class.java,
                    Constants.DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}