package com.example.jetweather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jetweather.domain.models.entity.UserLocation


@Database(entities = [UserLocation::class], version = 2, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract val dao: WeatherDao

    companion object {
        const val DATABASE_NAME = "SAVED_PLACES"
    }
}