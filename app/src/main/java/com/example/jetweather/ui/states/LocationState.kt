package com.example.jetweather.ui.states

import com.example.jetweather.domain.models.entity.UserLocation

data class LocationState(
    val location: UserLocation = UserLocation(),
    val isLoading:Boolean = true,
    val error:String = ""
)
