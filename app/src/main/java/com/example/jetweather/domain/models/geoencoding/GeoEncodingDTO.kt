package com.example.jetweather.domain.models.geoencoding

data class GeoEncodingDTO(
    val country: String?,
    val lat: Double,
    val local_names: LocalNames?=null,
    val lon: Double,
    val name: String?,
    val state: String?
)