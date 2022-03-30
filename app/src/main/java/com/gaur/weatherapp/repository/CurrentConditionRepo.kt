package com.gaur.weatherapp.repository

import com.gaur.weatherapp.model.WeatherResponseDTO
import com.gaur.weatherapp.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class CurrentConditionRepo @Inject constructor(private val apiService: ApiService) {


    suspend fun getCurrentLocationWeather(lat: Double, long: Double): Response<WeatherResponseDTO> {
        return apiService.getCurrentWeather(lat, long)
    }

}