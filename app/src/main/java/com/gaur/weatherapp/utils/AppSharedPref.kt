package com.gaur.weatherapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class AppSharedPref(context: Context) {

    private val sharedPref: SharedPreferences
    private val PREF_NAME = "weathter_pref_name"
    private val LATITUDE = "latitude"
    private val LONGITUDE = "longitude"
    private val NOTIFICATION_STATUS = "notification_status"


    init {
        sharedPref = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
    }


    fun putLatitude(latitude: String) {
        sharedPref.edit().putString(LATITUDE, latitude).apply()
    }

    fun putLongitude(longitude: String) {
        sharedPref.edit().putString(LONGITUDE, longitude).apply()
    }


    fun getLongitude(): String {
        return sharedPref.getString(LONGITUDE, "") ?: ""
    }

    fun getLatitude(): String {
        return sharedPref.getString(LATITUDE, "") ?: ""
    }

    fun isNotificationOn(): Boolean {
        return sharedPref.getBoolean(NOTIFICATION_STATUS, false)
    }

    fun putIsNotificationOn(bool: Boolean) {
        sharedPref.edit().putBoolean(NOTIFICATION_STATUS, bool).apply()
    }

}