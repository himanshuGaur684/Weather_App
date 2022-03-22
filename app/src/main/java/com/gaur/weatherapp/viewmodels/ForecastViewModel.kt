package com.gaur.weatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaur.weatherapp.model.forecast.Weather
import com.gaur.weatherapp.model.forecast.WeatherDTO
import com.gaur.weatherapp.repository.ForecastRepository
import com.gaur.weatherapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(private val forecastRepository: ForecastRepository) :
    ViewModel() {


    private val _forecastList = MutableLiveData<Resource<List<WeatherDTO>>>()
    val forecastList: LiveData<Resource<List<WeatherDTO>>> = _forecastList

    fun getSixteenDays(lat: Double, long: Double) = viewModelScope.launch {
        _forecastList.postValue(Resource.Loading(null))
        try {
            Log.d("TAG", "getWeather: $lat $long")
            val response = forecastRepository.getSixteenDaysForecast(lat, long)
            if (response.isSuccessful) {
                val body = response.body()!!

                _forecastList.postValue(Resource.Success(body.list))


            } else {
                val body = response.errorBody()?.string()
                Log.d("TAG", "getWeather: ${response.errorBody()?.string()}")
                try {
                    val json = JSONObject(body)
                    val message = json?.get("message").toString()
                    _forecastList.postValue(Resource.Error(message = message))
                } catch (e: Exception) {
                    _forecastList.postValue(Resource.Error(message = e.message))
                }

            }

        } catch (e: Exception) {
            Log.d("TAG", "getWeather: ${e.printStackTrace()}")
            _forecastList.postValue(Resource.Error(message = e.message))
        }


    }


}