package com.gaur.weatherapp.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gaur.weatherapp.databinding.FragmentCurrentConditionsBinding
import com.gaur.weatherapp.model.WeatherResponseDTO
import com.gaur.weatherapp.utils.Resource
import com.gaur.weatherapp.viewmodels.CurrentConditionViewModel
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CurrentConditionsFragment : Fragment() {

    private val currentConditionsViewModel: CurrentConditionViewModel by viewModels()


    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    private var _binding: FragmentCurrentConditionsBinding? = null
    private val binding: FragmentCurrentConditionsBinding
        get() = _binding!!

    private val args: CurrentConditionsFragmentArgs by navArgs()


    private val registerActivityForResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

            if (it[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true && it[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                isLocationPermissionGranted()
            }

        }


    private val callback = object : LocationCallback() {
        override fun onLocationAvailability(p0: LocationAvailability) {
            super.onLocationAvailability(p0)
        }

        override fun onLocationResult(result: LocationResult) {
            val lastLocation = result.lastLocation
            Log.d("TAG", "onLocationResult: ${lastLocation?.longitude.toString()}")
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
        _binding = FragmentCurrentConditionsBinding.inflate(inflater, container, false)
        return _binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //isLocationPermissionGranted()
        setObserver()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())


        binding.btnForecast.setOnClickListener {
            findNavController().navigate(
                CurrentConditionsFragmentDirections.actionCurrentConditionsFragmentToForecastFragment(
                    weatherResponse = args.weatherResponse,
                    lat = args.lat,
                    long = args.long,
                    isCurrentLoaction = args.isCurrentLocation
                )
            )
        }


        if (args.isCurrentLocation) {
            getCurrentWeather()
        } else {
            setArgumentedData(args.weatherResponse)
        }


    }

    private fun setArgumentedData(weatherResponse: WeatherResponseDTO?) {
        weatherResponse?.let {
            binding.tvName.text = it.name
            binding.tvTempratureBig.text = formatUptoTwoDecimal(it.main.temp.minus(273)) + "\u2103"
            binding.tvFeelsLike.text =
                "Feels like " + formatUptoTwoDecimal(it.main.feels_like?.minus(273)) + "\u2103"
            binding.tvHigh.text =
                "High " + formatUptoTwoDecimal(it.main.temp_max.minus(273)) + "\u2103"
            binding.tvLow.text =
                "Low " + formatUptoTwoDecimal(it.main.temp_min.minus(273)) + "\u2103"
            binding.tvHumidity.text = "Humidity " + it.main.humidity + " %"
            binding.tvPressure.text = "Pressure " + it.main.pressure + " hPa"
            Glide.with(binding.ivCircle)
                .load("https://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png")
                .into(binding.ivCircle)
        }
    }

    private fun setObserver() {
        currentConditionsViewModel.weather.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("TAG", "onViewCreated: ${it.data.toString()}")
                    it.data?.let { setData(it) }

                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun setData(it: WeatherResponseDTO) {
        binding.tvName.text = it.name
        binding.tvTempratureBig.text = formatUptoTwoDecimal(it.main.temp.minus(273)) + "\u2103"
        binding.tvFeelsLike.text =
            "Feels like " + formatUptoTwoDecimal(it.main.feels_like?.minus(273)) + "\u2103"
        binding.tvHigh.text =
            "High " + formatUptoTwoDecimal(it.main.temp_max.minus(273)) + "\u2103"
        binding.tvLow.text =
            "Low " + formatUptoTwoDecimal(it.main.temp_min.minus(273)) + "\u2103"
        binding.tvHumidity.text = "Humidity " + it.main.humidity + " %"
        binding.tvPressure.text = "Pressure " + it.main.pressure + " hPa"
        Glide.with(binding.ivCircle)
            .load("https://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png")
            .into(binding.ivCircle)
    }

    private fun getCurrentWeather() {
        currentConditionsViewModel.getCurrentWeather(args.lat.toDouble(), args.long.toDouble())
    }


    private fun isLocationPermissionGranted() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            registerActivityForResult.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        } else {
            val location =
                LocationServices.getFusedLocationProviderClient(requireContext()).lastLocation
            location.addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("TAG", "isLocationPermissionGranted: ${it.result}")
                } else {
                    Log.d("TAG", "isLocationPermissionGranted: Error")
                }
            }

        }
    }

    private fun formatUptoTwoDecimal(value: Double): String {
        return "%.2f".format(value)


    }


}