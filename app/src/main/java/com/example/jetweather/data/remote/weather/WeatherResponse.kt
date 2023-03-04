package com.example.jetweather.data.remote.weather

import com.example.jetweather.domain.models.weather.daily_forecast.Daily
import com.example.jetweather.domain.models.weather.daily_forecast.DailyForecastDTO
import com.example.jetweather.domain.models.weather.hourly_forecast.HourlyForecastDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherResponse {

    @GET("/v1/forecast?hourly=temperature_180m,relativehumidity_2m,apparent_temperature,precipitation,weathercode,windspeed_180m,pressure_msl")
    suspend fun getHourlyForecast(
        @Query("latitude") lat:String,
        @Query("longitude") long:String,
        @Query("temperature_unit") tempUnit:String?=null,
        @Query("windspeed_unit") windUnit:String?=null
    ):HourlyForecastDTO

    @GET("/v1/forecast?daily=temperature_2m_max,temperature_2m_min,weathercode,precipitation_sum,sunrise,sunset&timezone=auto")
    suspend fun getDailyForecast(
        @Query("latitude") lat:String,
        @Query("longitude") long:String
    ): DailyForecastDTO



}