package com.example.jetweather.ui.states

import com.example.jetweather.domain.models.entity.UserLocation
import com.example.jetweather.domain.mapper.DayWiseForecast
import com.example.jetweather.domain.mapper.HourlyForecast

data class UserDataState(
    val userData: UserData?=null,
    val isLoading:Boolean = true,
    val error:String = ""
)

data class UserData(
    val hourlyForecast:Map<Int,List<HourlyForecast>>,
    val dailyForecast:List<DayWiseForecast>,
    val location:UserLocation
)