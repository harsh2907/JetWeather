package com.example.jetweather.ui.states

import com.example.jetweather.domain.mapper.DayWiseForecast

data class DailyWeatherState(
    val forecasts:List<DayWiseForecast> = emptyList(),
    val isLoading:Boolean = true,
    val error:String = ""
)
