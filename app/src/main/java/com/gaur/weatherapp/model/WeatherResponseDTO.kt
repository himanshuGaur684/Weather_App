package com.gaur.weatherapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherResponseDTO(
    val base: String,
    val clouds: CloudsDTO,
    val cod: Int,
    val coord: CoordDTO,
    val dt: Int,
    val id: Int,
    val main: MainDTO,
    val name: String,
    val sys: SysDTO,
    val timezone: Int,
    val visibility: Int,
    val weather: List<WeatherDTO>,
    val wind: WindDTO
): Parcelable