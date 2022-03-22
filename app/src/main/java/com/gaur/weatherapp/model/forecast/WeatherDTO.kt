package com.gaur.weatherapp.model.forecast

data class WeatherDTO(
    val clouds: Double,
    val deg: Double,
    val dt: Double,
    val feels_like: FeelsLike,
    val gust: Double,
    val humidity: Double,
    val pop: Double,
    val pressure: Int,
    val snow: Double,
    val speed: Double,
    val sunrise: Int,
    val sunset: Int,
    val temp: Temp,
    val weather: List<Weather>
)
