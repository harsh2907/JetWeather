package com.example.jetweather.domain.models.weather.hourly_forecast

data class HourlyUnits(
    val apparent_temperature: String,
    val precipitation: String,
    val relativehumidity_2m: String,
    val temperature_2m: String,
    val time: String,
    val weathercode: String,
    val windspeed_10m:String,
    val pressure_msl:String
)