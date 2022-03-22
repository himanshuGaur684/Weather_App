package com.gaur.weatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SysDTO(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
):Parcelable