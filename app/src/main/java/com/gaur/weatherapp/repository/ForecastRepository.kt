package com.gaur.weatherapp.repository

import com.gaur.weatherapp.model.forecast.ForecastDTO
import com.gaur.weatherapp.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class ForecastRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getSixteenDaysForecast(lat:Double,long:Double): Response<ForecastDTO> {
        return apiService.getSixteenDaysForecast(lat, long)
    }

}