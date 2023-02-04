package com.example.jetweather.domain.repository

import android.location.Location
import com.example.jetweather.data.utils.Response
import com.example.jetweather.domain.models.geoencoding.GeoEncodingDTO
import com.example.jetweather.domain.models.weather.daily_forecast.DailyForecastDTO
import com.example.jetweather.domain.models.weather.hourly_forecast.HourlyForecastDTO
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getNameFromCoordinates(latitude:String,longitude:String):Flow<Response<GeoEncodingDTO>>
    suspend fun getCoordinatesFromName(name:String):Flow<Response<List<GeoEncodingDTO>>>

    suspend fun getHourlyForecast(lat:String,long:String):Flow<Response<HourlyForecastDTO>>
    suspend fun getDailyForecast(lat:String,long:String):Flow<Response<DailyForecastDTO>>
}