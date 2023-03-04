package com.example.jetweather.data.repository

import android.app.Activity
import android.content.Context
import android.location.Location
import com.example.jetweather.data.local.WeatherDao
import com.example.jetweather.data.remote.geoencoding.GeoEncodingResponse
import com.example.jetweather.data.remote.weather.WeatherResponse
import com.example.jetweather.data.utils.Response
import com.example.jetweather.domain.models.geoencoding.GeoEncodingDTO
import com.example.jetweather.domain.repository.WeatherRepository
import com.example.jetweather.domain.location.DefaultLocationManager
import com.example.jetweather.domain.models.weather.daily_forecast.DailyForecastDTO
import com.example.jetweather.domain.models.weather.hourly_forecast.HourlyForecastDTO
import com.example.jetweather.domain.mapper.toDailyHourlyForecast
import com.example.jetweather.domain.models.entity.SavedLocationWeatherOverview
import com.example.jetweather.ui.sharedpreferences.SharedPreferenceUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalTime
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherResponse,
    private val dao: WeatherDao,
    context:Context
) : WeatherRepository {

    private val sharedPref = SharedPreferenceUtils(context)

    override suspend fun getHourlyForecast(
        lat: String,
        long: String
    ): Flow<Response<HourlyForecastDTO>> = flow {
        emit(Response.Loading())
        try {
            val tempUnit = if(sharedPref.getTemp() == 1) "fahrenheit" else null
            val windUnit = if(sharedPref.getWind() == 1) "ms" else null
            val response = weatherApi.getHourlyForecast(lat, long,tempUnit,windUnit)
            emit(Response.Success(response))

        } catch (e: HttpException) {
            emit(Response.Error(message = "Oops, something went wrong"))
        } catch (e: IOException) {
            emit(Response.Error(message = "Couldn't reach server check your internet connection"))
        }
    }

    override suspend fun getDailyForecast(
        lat: String,
        long: String
    ): Flow<Response<DailyForecastDTO>> = flow {
        emit(Response.Loading())
        try {
            val response = weatherApi.getDailyForecast(lat, long)
            emit(Response.Success(response))

        } catch (e: HttpException) {
            emit(Response.Error(message = "Oops, something went wrong"))
        } catch (e: IOException) {
            emit(Response.Error(message = "Couldn't reach server check your internet connection"))
        }
    }

    override suspend fun getWeatherForSavedPlaces(): Flow<Response<List<SavedLocationWeatherOverview>>> =
        flow {
            try {
                emit(Response.Loading())
                val hourIndex = LocalTime.now().hour
                val locations = dao.getAllPlaces().first()

                if(locations.isEmpty()){
                    emit(Response.Success(emptyList()))
                }else{
                    val savedWeather = locations.map { location ->
                        val weather = weatherApi.getHourlyForecast(
                            location.lat.toString(),
                            location.long.toString()
                        ).toDailyHourlyForecast()[0]?.get(hourIndex)!!
                        SavedLocationWeatherOverview(
                            location,
                            weather
                        )
                    }
                    emit(Response.Success(savedWeather))
                }

            } catch (e: HttpException) {
                emit(Response.Error(message = "Oops, something went wrong"))
            } catch (e: IOException) {
                emit(Response.Error(message = "Couldn't reach server check your internet connection"))
            } catch (e: Exception) {
                emit(Response.Error(message = "Oops, something went wrong"))
            }
        }


}