package com.gaur.weatherapp.repository


import com.gaur.weatherapp.model.WeatherResponseDTO
import com.gaur.weatherapp.model.forecast.ForecastDTO
import com.gaur.weatherapp.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class SearchRepository @Inject constructor(private val apiService: ApiService) {


    suspend fun getWeather(zipCode:String): Response<WeatherResponseDTO> {
           return apiService.getWeatherDetails(zipCode)
    }


}