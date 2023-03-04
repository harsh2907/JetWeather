package com.example.jetweather.ui.states

import com.example.jetweather.domain.models.entity.UserLocation

data class SearchedLocationState(
    val data:List<UserLocation> = emptyList(),
    val isLoading:Boolean = true,
    val error:String = ""
)