package com.example.jetweather.domain.location

import android.location.Location

data class UserLocation(
    val city:String="",
    val state:String = "",
    val country:String="",
    val lat:String = "",
    val long:String = ""
)

fun Location.toUserLocation() = UserLocation(lat = this.latitude.toString(), long = this.longitude.toString())