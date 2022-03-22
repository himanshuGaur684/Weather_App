package com.gaur.weatherapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaur.weatherapp.model.WeatherResponseDTO
import com.gaur.weatherapp.repository.SearchRepository
import com.gaur.weatherapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrentConditionViewModel @Inject constructor(private val searchRepository: SearchRepository): ViewModel() {

    private val _weather = MutableLiveData<Resource<WeatherResponseDTO>>()
    val weather: LiveData<Resource<WeatherResponseDTO>> = _weather


}