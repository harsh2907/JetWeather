package com.example.jetweather.data.utils

import java.time.LocalDateTime

fun LocalDateTime.to12HourFormat():String{

    var time = (hour + 24)%12
    if(time == 0) time = 12

    val attr = if(hour>=0 && hour <12 ) "am" else "pm"

    return  "$time:00 $attr"
}