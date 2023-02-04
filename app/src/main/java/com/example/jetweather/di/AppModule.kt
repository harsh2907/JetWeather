package com.example.jetweather.di

import android.content.Context
import com.example.jetweather.data.remote.geoencoding.GeoEncodingResponse
import com.example.jetweather.data.remote.weather.WeatherResponse
import com.example.jetweather.data.repository.WeatherRepositoryImpl
import com.example.jetweather.domain.repository.WeatherRepository
import com.example.jetweather.domain.utils.GeoEncodingUtil
import com.example.jetweather.domain.location.DefaultLocationManager
import com.example.jetweather.domain.location.LocationClient
import com.example.jetweather.domain.utils.WeatherUtil
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getDefaultLocationManager(@ApplicationContext context: Context): LocationClient {
        return DefaultLocationManager(
            context,
            LocationServices.getFusedLocationProviderClient(context)
        )
    }

   @Singleton
   @Provides
   fun provideGeoEncodingApi():GeoEncodingResponse {
      return Retrofit.Builder()
           .baseUrl(GeoEncodingUtil.baseUrl)
           .addConverterFactory(GsonConverterFactory.create())
           .build()
           .create(GeoEncodingResponse::class.java)
   }

    @Singleton
    @Provides
    fun provideWeatherApi():WeatherResponse {
        return Retrofit.Builder()
            .baseUrl(WeatherUtil.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherResponse::class.java)
    }

    @Singleton
    @Provides
    fun getWeatherRepository(
        geoEncodingApi: GeoEncodingResponse,
        weatherApi:WeatherResponse
    ): WeatherRepository = WeatherRepositoryImpl(geoEncodingApi,weatherApi)
}