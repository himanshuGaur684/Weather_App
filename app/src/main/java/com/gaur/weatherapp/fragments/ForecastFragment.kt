package com.gaur.weatherapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.gaur.weatherapp.adapter.ForecastWeatherAdapter
import com.gaur.weatherapp.databinding.FragmentForecastBinding
import com.gaur.weatherapp.utils.Resource
import com.gaur.weatherapp.viewmodels.ForecastViewModel
import dagger.hilt.android.AndroidEntryPoint

// https://api.openweathermap.org/data/2.5/weather?lat=57&lon=-2.15&appid=51048e1180bda3dcbd240c9d9051920e

@AndroidEntryPoint
class ForecastFragment : Fragment() {

    private var _binding: FragmentForecastBinding? = null
    private val binding: FragmentForecastBinding
        get() = _binding!!


    private val forecastViewModel: ForecastViewModel by viewModels()
    private val args: ForecastFragmentArgs by navArgs()
    private var forecastAdapter: ForecastWeatherAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForecastBinding.inflate(inflater, container, false)
        return _binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setObserver()

        args.weatherResponse?.let {
            forecastViewModel.getSixteenDays(it.coord.lat , it.coord.lon )
        }


    }

    private fun setObserver() {
        forecastViewModel.forecastList.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is Resource.Loading -> {
                    binding.progress.visibility=View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progress.visibility=View.GONE
                        it.data?.let {
                            forecastAdapter= ForecastWeatherAdapter(it)
                            binding.rvForecast.adapter = forecastAdapter
                        }
                    }
                    is Resource.Error -> {
                        binding.progress.visibility=View.GONE
                        AlertDialog.Builder(requireContext()).setTitle("Error")
                            .setMessage(it.message)
                            .create().show()

                    }

                }
            }
        }
    }


}