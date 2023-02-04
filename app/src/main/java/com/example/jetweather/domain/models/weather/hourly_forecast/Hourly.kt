package com.example.jetweather.domain.models.weather.hourly_forecast

data class Hourly(
    val apparent_temperature: List<Double>,
    val precipitation: List<Double>,
    val relativehumidity_2m: List<Int>,
    val temperature_2m: List<Double>,
    val time: List<String>,
    val weathercode: List<Int>,
    val windspeed_10m:List<Double>,
    val pressure_msl:List<Double>
)
