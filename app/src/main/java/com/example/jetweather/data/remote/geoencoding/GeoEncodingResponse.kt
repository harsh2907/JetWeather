package com.example.jetweather.data.remote.geoencoding

import com.example.jetweather.domain.models.geoencoding.GeoEncodingDTO
import com.example.jetweather.domain.utils.GeoEncodingUtil
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoEncodingResponse {

    @GET("/geo/1.0/reverse")
    suspend fun getNameFromCoordinates(
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("limit") limit:Int=10,
        @Query("appid") apiKey:String = GeoEncodingUtil.API_KEY
    ):List<GeoEncodingDTO>

    @GET("/geo/1.0/direct")
    suspend fun getCoordinatesFromName(
        @Query("q") name:String,
        @Query("limit") limit:Int=10,
        @Query("appid") apiKey:String = GeoEncodingUtil.API_KEY
    ):List<GeoEncodingDTO>


}