package com.gaur.weatherapp.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gaur.weatherapp.databinding.FragmentSearchBinding
import com.gaur.weatherapp.utils.Resource
import com.gaur.weatherapp.viewmodels.SearchViewModel
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels()

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!


    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    private val callback = object : LocationCallback() {
        override fun onLocationAvailability(p0: LocationAvailability) {
            super.onLocationAvailability(p0)
        }

        override fun onLocationResult(result: LocationResult) {
            val lastLocation = result.lastLocation
            Log.d("TAG", "onLocationResult: ${lastLocation?.longitude.toString()}")

            val lat = lastLocation.latitude
            val long = lastLocation.longitude

            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(
                    isCurrentLocation = true,
                    lat = lat.toFloat(),
                    long = long.toFloat()
                )
            )

            super.onLocationResult(result)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return _binding?.root!!
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())


        setObserver()
        binding.edZipCode.doAfterTextChanged {
            if (it.toString().length >= 5) {
                binding.btnSearch.visibility = View.VISIBLE
            } else {
                binding.btnSearch.visibility = View.GONE
            }
        }

        binding.btnCurrentLocation.setOnClickListener {
            onGPS()
        }

        binding.btnSearch.setOnClickListener {
            getWeather(binding.edZipCode.text.toString().trim())
        }


    }

    fun setObserver() {
        searchViewModel.weather.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        Log.d("TAG", "getWeather: ${it.data}")
                        findNavController().navigate(
                            SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(
                                weatherResponse = it.data
                            )
                        )
                        searchViewModel.resetWeatherValue()
                    }
                    is Resource.Error -> {
                        AlertDialog.Builder(requireContext()).setTitle("Error")
                            .setMessage(it.message)
                            .create().show()

                    }
                }
            }
        }
    }

    private fun getWeather(zipCode: String) {
        searchViewModel.getWeather(zipCode)
    }


    private fun onGPS() {
        if (!isLocationEnabled()) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        } else {
            fetchLocation()
        }


    }

    private fun fetchLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    200
                )
                return
            } else {
                requestLocation()
            }


        }
    }


    @Suppress("MissingPermission")
    private fun requestLocation() {
        Log.d("TAG", "requestLocation: ")
        val requestLocation = LocationRequest()
        requestLocation.priority = android.location.LocationRequest.QUALITY_HIGH_ACCURACY
        requestLocation.interval = 0
        requestLocation.fastestInterval = 0
        requestLocation.numUpdates = 1
        fusedLocationProviderClient.requestLocationUpdates(
            requestLocation, callback, Looper.getMainLooper()
        )

    }


    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    override fun onResume() {
        super.onResume()
    }

}