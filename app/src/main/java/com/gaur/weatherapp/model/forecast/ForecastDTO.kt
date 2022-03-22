package com.gaur.weatherapp.model.forecast

data class ForecastDTO(
    val city: City,
    val cnt: Double,
    val cod: String,
    val list: List<WeatherDTO>,
    val message: Double
)