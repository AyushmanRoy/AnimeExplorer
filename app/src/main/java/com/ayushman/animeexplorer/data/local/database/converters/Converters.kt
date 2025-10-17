package com.ayushman.animeexplorer.data.local.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Type converters for Room database
 * These help Room store complex data types like Lists
 */
class Converters {

    /**
     * Convert List<Int> to JSON String for storage
     */
    @TypeConverter
    fun fromIntList(value: List<Int>): String {
        return Gson().toJson(value)
    }

    /**
     * Convert JSON String back to List<Int>
     */
    @TypeConverter
    fun toIntList(value: String): List<Int> {
        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }

    /**
     * Convert List<String> to JSON String for storage
     */
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    /**
     * Convert JSON String back to List<String>
     */
    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}