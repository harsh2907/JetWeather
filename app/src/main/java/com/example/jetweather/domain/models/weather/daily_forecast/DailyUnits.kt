package com.example.jetweather.domain.models.weather.daily_forecast

data class DailyUnits(
    val precipitation_sum: String,
    val sunrise: String,
    val sunset: String,
    val temperature_2m_max: String,
    val temperature_2m_min: String,
    val time: String,
    val weathercode: String
)