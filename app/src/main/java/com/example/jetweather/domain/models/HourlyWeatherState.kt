package com.example.jetweather.domain.models

import com.example.jetweather.domain.mapper.HourlyForecast


data class HourlyWeatherState(
    val forecasts:Map<Int,List<HourlyForecast>> = emptyMap(),
    val isLoading:Boolean = true,
    val error:String = ""
)
