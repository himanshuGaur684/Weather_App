package com.gaur.weatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gaur.weatherapp.R
import com.gaur.weatherapp.databinding.FragmentCurrentConditionsBinding
import com.gaur.weatherapp.viewmodels.CurrentConditionViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CurrentConditionsFragment : Fragment() {

    private val currentConditionsViewModel: CurrentConditionViewModel by viewModels()

    private var _binding: FragmentCurrentConditionsBinding? = null
    private val binding: FragmentCurrentConditionsBinding
        get() = _binding!!

    private val args:CurrentConditionsFragmentArgs by navArgs()

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



        binding.btnForecast.setOnClickListener {
            findNavController().navigate(CurrentConditionsFragmentDirections.actionCurrentConditionsFragmentToForecastFragment(
                args.weatherResponse
            ))
        }


        args.weatherResponse?.let {

            binding.tvName.text= it.name
            binding.tvTempratureBig.text = formatUptoTwoDecimal(it.main.temp.minus(273) )+"\u2103"
            binding.tvFeelsLike.text="Feels like "+formatUptoTwoDecimal(it.main.feels_like?.minus(273) )+"\u2103"
            binding.tvHigh.text="High "+formatUptoTwoDecimal(it.main.temp_max.minus(273) )+"\u2103"
            binding.tvLow.text="Low "+formatUptoTwoDecimal(it.main.temp_min.minus(273) )+"\u2103"
            binding.tvHumidity.text="Humidity "+ it.main.humidity +" %"
            binding.tvPressure.text="Pressure "+it.main.pressure+" hPa"
            Glide.with(binding.ivCircle)
                .load("https://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png")
                .into(binding.ivCircle)

        }


    }


    private fun formatUptoTwoDecimal(value:Double):String{
        return "%.2f".format(value)


    }



}