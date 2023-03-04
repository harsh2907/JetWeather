package com.example.jetweather.domain.repository

import com.example.jetweather.data.utils.Response
import com.example.jetweather.domain.models.entity.UserLocation
import com.example.jetweather.domain.models.geoencoding.GeoEncodingDTO
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun getNameFromCoordinates(
        latitude: String,
        longitude: String
    ): Flow<Response<GeoEncodingDTO>>

    suspend fun getCoordinatesFromName(name: String): Flow<Response<List<UserLocation>>>

}