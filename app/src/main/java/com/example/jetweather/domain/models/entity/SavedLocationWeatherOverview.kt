package com.example.jetweather.domain.models.entity

import com.example.jetweather.domain.mapper.HourlyForecast

data class SavedLocationWeatherOverview(
    val location: UserLocation,
    val forecast : HourlyForecast
)
