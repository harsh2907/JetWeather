package com.example.jetweather.domain.utils

import com.example.jetweather.BuildConfig
import kotlin.math.roundToInt

object GeoEncodingUtil {
    const val API_KEY = BuildConfig.API_KEY //Replace your api key here
    const val baseUrl = "https://api.openweathermap.org"
}

object WeatherUtil{
    const val baseUrl = "https://api.open-meteo.com"

    fun Double.roundOfToFour():Double{
        return (this * 10000.0).roundToInt() /10000.0
    }
}

