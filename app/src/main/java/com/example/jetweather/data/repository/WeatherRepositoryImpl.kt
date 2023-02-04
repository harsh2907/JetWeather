package com.example.jetweather.data.repository

import android.location.Location
import com.example.jetweather.data.remote.geoencoding.GeoEncodingResponse
import com.example.jetweather.data.remote.weather.WeatherResponse
import com.example.jetweather.data.utils.Response
import com.example.jetweather.domain.models.geoencoding.GeoEncodingDTO
import com.example.jetweather.domain.repository.WeatherRepository
import com.example.jetweather.domain.location.DefaultLocationManager
import com.example.jetweather.domain.models.weather.daily_forecast.DailyForecastDTO
import com.example.jetweather.domain.models.weather.hourly_forecast.HourlyForecastDTO
import com.example.jetweather.domain.mapper.toDailyHourlyForecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val geoEncodingApi:GeoEncodingResponse,
    private val weatherApi:WeatherResponse
) : WeatherRepository {

    override suspend fun getNameFromCoordinates(
        latitude: String,
        longitude: String
    ): Flow<Response<GeoEncodingDTO>> = flow{
        emit(Response.Loading())
        try {
            val response = geoEncodingApi.getNameFromCoordinates(lat = latitude, lon = longitude)
            if(response.isEmpty()){
                emit(Response.Error("Sorry can't fetch location"))
                return@flow
            }

            emit(Response.Success(response[0]))
        }catch (e: HttpException){
            emit(Response.Error(message = "Oops, something went wrong"))
        }
        catch (e: IOException){
            emit(Response.Error(message = "Couldn't reach server check your internet connection"))
        }
    }

    override suspend fun getCoordinatesFromName(name: String): Flow<Response<List<GeoEncodingDTO>>> = flow{
        emit(Response.Loading())
        try {
            val response = geoEncodingApi.getCoordinatesFromName(name =name)
            if(response.isEmpty()){
                emit(Response.Error("Sorry no result found."))
                return@flow
            }

            emit(Response.Success(response))
        }catch (e: HttpException){
            emit(Response.Error(message = "Oops, something went wrong"))
        }
        catch (e: IOException){
            emit(Response.Error(message = "Couldn't reach server check your internet connection"))
        }
    }

    override suspend fun getHourlyForecast(
        lat: String,
        long: String
    ): Flow<Response<HourlyForecastDTO>> = flow {
        emit(Response.Loading())
        try {
            val response = weatherApi.getHourlyForecast(lat, long)
            emit(Response.Success(response))

        }catch (e: HttpException){
            emit(Response.Error(message = "Oops, something went wrong"))
        }
        catch (e: IOException){
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

        }catch (e: HttpException){
            emit(Response.Error(message = "Oops, something went wrong"))
        }
        catch (e: IOException){
            emit(Response.Error(message = "Couldn't reach server check your internet connection"))
        }
    }


}