package com.gaur.weatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaur.weatherapp.model.WeatherResponseDTO
import com.gaur.weatherapp.repository.SearchRepository
import com.gaur.weatherapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository) :
    ViewModel() {


    private val _weather = MutableLiveData<Resource<WeatherResponseDTO>>()
    val weather: LiveData<Resource<WeatherResponseDTO>> = _weather


    fun resetWeatherValue(){
        _weather.postValue( Resource.Isolated())
    }

    fun getWeather(zipCode: String) = viewModelScope.launch {
        _weather.postValue(Resource.Loading(null))
        try {
            Log.d("TAG", "getWeather: $zipCode")
            val response = searchRepository.getWeather(zipCode = zipCode)
            if (response.isSuccessful) {
                _weather.postValue(Resource.Success(response.body()!!))
            } else {
                val body = response.errorBody()?.string()
                Log.d("TAG", "getWeather: ${response.errorBody()?.string()}")
                try {
                    val json = JSONObject(body)
                    val message = json?.get("message").toString()
                    _weather.postValue(Resource.Error(message = message))
                } catch (e: Exception) {
                    _weather.postValue(Resource.Error(message = e.message))
                }

            }

        } catch (e: Exception) {
            Log.d("TAG", "getWeather: ${e.printStackTrace()}")
            _weather.postValue(Resource.Error(message = e.message))
        }


    }


}