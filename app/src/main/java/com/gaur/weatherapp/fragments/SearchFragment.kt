package com.gaur.weatherapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gaur.weatherapp.databinding.FragmentSearchBinding
import com.gaur.weatherapp.utils.Resource
import com.gaur.weatherapp.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels()

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

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

        setObserver()
        binding.edZipCode.doAfterTextChanged {
            if (it.toString().length >= 5) {
                binding.btnSearch.visibility = View.VISIBLE
            } else {
                binding.btnSearch.visibility = View.GONE
            }
        }


        binding.btnSearch.setOnClickListener {
            getWeather(binding.edZipCode.text.toString().trim())
        }


    }

    fun setObserver(){
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

    override fun onResume() {
        super.onResume()
    }

}