package com.example.jetweather.data.local

import androidx.room.*
import com.example.jetweather.domain.models.entity.UserLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM saved_places")
    fun getAllPlaces():Flow<List<UserLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place:UserLocation)

    @Delete
    suspend fun deletePlace(place:UserLocation)

    @Query("select * from saved_places where lat = :lat ")
    suspend fun getLocationByLat(lat:String):UserLocation?

}