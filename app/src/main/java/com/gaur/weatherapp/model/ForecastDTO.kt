package com.gaur.weatherapp.model

data class ForecastDTO(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherDTO>,
    val message: Double
)