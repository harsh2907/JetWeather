package com.example.jetweather.domain.mapper

data class IndexedWeatherInfo(
    val index:Int,
    val hourlyForecast: HourlyForecast
)
