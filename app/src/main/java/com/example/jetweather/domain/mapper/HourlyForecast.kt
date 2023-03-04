package com.example.jetweather.domain.mapper

import com.example.jetweather.domain.models.weather.hourly_forecast.HourlyForecastDTO
import com.example.jetweather.domain.models.weather.hourly_forecast.HourlyUnits
import com.example.jetweather.domain.utils.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

data class HourlyForecast(
    val feelsLike: Int,
    val precipitation: Double,
    val relativeHumidity: Int,
    val temperature: Int,
    val time: LocalDateTime,
    val wind: Double,
    val pressure: Double,
    val weatherType: WeatherType,
    val hourlyUnits: HourlyUnits
)

/*
First we make a map of forecast which contains only required entries.
We convert DTO to a map which has index based entries for hourly forecasts
Then grouped it day wise

 */
fun HourlyForecastDTO.toDailyHourlyForecast() = List(this.hourly.time.size) { index ->
    val time = hourly.time[index]
    val weatherCode = hourly.weathercode[index]
    val feelsLike =
        hourly.apparent_temperature[index].let { if (it.roundToInt() < it) it.roundToInt() + 1 else it.roundToInt() }
    val temperature =
        hourly.temperature_180m[index].let { if (it.roundToInt() < it) it.roundToInt() + 1 else it.roundToInt() }
    val relativeHumidity = hourly.relativehumidity_2m[index]
    val precipitation = hourly.precipitation[index]
    val wind = hourly.windspeed_180m[index]
    val pressure = hourly.pressure_msl[index]


    val forecast = HourlyForecast(
        feelsLike = feelsLike,
        precipitation = precipitation,
        relativeHumidity = relativeHumidity,
        temperature = temperature,
        time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
        wind = wind,
        pressure = pressure,
        weatherType = WeatherType.fromWMO(weatherCode),
        hourlyUnits = hourly_units
    )
    IndexedWeatherInfo(
        index,
        forecast
    )
}.groupBy {
    it.index / 24
}.mapValues { entry ->
    entry.value.map { it.hourlyForecast }
}

fun Map<Int, List<HourlyForecast>>.getForecastForDay():List<HourlyForecast> {
    val current = this[0] ?: return emptyList()

    val now = LocalDateTime.now()
    val index = if(now.minute > 30) (now.hour +1)%24 else now.hour

    val currentDayList = current.subList(index,24)
    if(currentDayList.size == 24) return  currentDayList

    val nextDay = this[1] ?: return currentDayList
    val nextDayList = nextDay.subList(0,index)

    return currentDayList + nextDayList

}


