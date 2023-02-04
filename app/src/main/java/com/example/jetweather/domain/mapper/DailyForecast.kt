package com.example.jetweather.domain.mapper

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import com.example.jetweather.domain.models.weather.daily_forecast.DailyForecastDTO
import com.example.jetweather.domain.models.weather.daily_forecast.DailyUnits
import com.example.jetweather.domain.utils.WeatherType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

data class DailyForecast(
    val time: LocalDate,
    val maxTemperature: Int,
    val minTemperature: Int,
    val weatherType: WeatherType,
    val dailyUnits: DailyUnits,
    val sunrise: LocalDateTime,
    val sunset: LocalDateTime
)

data class DayWiseForecast(
    val day: String,
    val forecast: DailyForecast
)

fun DailyForecastDTO.toDailyForecasts(): List<DayWiseForecast> {

    return List(daily.time.size) { index ->
        val time = this.daily.time[index]
        val maxTemp = daily.temperature_2m_max[index]
        val minTemp = daily.temperature_2m_min[index]
        val weatherCode = daily.weathercode[index]
        val sunrise = daily.sunrise[index]
        val sunset = daily.sunset[index]

        DailyForecast(
            time = LocalDate.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            maxTemperature = maxTemp.let { if (it.roundToInt() < it) it.roundToInt() + 1 else it.roundToInt() },
            minTemperature = minTemp.let { if (it.roundToInt() < it) it.roundToInt() + 1 else it.roundToInt() },
            weatherType = WeatherType.fromWMO(weatherCode),
            sunrise = LocalDateTime.parse(sunrise, DateTimeFormatter.ISO_DATE_TIME),
            sunset = LocalDateTime.parse(sunset, DateTimeFormatter.ISO_DATE_TIME),
            dailyUnits = daily_units
        )
    }.mapIndexed { index, dailyForecast ->
        val day = when (index) {
            0 -> "Today"
            1 -> "Tomorrow"
            else -> {

                val name = dailyForecast.time.dayOfWeek.name.toLowerCase(Locale.current)
                val a = name[0].uppercase()

                a + name.substring(1)
            }
        }
        DayWiseForecast(
            day = day,
            forecast = dailyForecast
        )
    }
}