package com.example.jetweather.domain.models.weather.daily_forecast

import com.example.jetweather.domain.utils.WeatherType
import java.time.LocalDateTime

data class DailyForecastDTO(
    val daily: Daily,
    val daily_units: DailyUnits,
    val elevation: Int,
    val generationtime_ms: Double,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
)