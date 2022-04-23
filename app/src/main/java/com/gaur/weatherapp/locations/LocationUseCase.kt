package com.gaur.weatherapp.locations

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationUseCase {
}


class GetLocationUseCase @Inject constructor(
    private val context: Context,
    private var fusedLocationProviderClient: FusedLocationProviderClient
) : CoroutineScope {


    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main


    @SuppressLint("MissingPermission")
    suspend operator fun invoke(): LocationData = suspendCoroutine {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation.run {
                    Log.d("TAG", "onLocationResult: $this")
                    it.resume(LocationData(this.latitude, this.longitude))
                }
                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }

        val locationRequest = LocationRequest()

        locationRequest.apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 1
            interval = 0
            fastestInterval = 0
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        )

    }

}