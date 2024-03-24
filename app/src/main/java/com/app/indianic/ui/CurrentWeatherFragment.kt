package com.app.indianic.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.app.indianic.R
import com.app.indianic.base.BaseFragment
import com.app.indianic.base.BaseResponse
import com.app.indianic.databinding.FragmentCurrentWeatherBinding
import com.app.indianic.response.WeatherInfo
import com.app.indianic.utils.Resource
import com.app.indianic.utils.logError
import com.app.indianic.utils.showShortToast
import com.app.indianic.viewmodel.CommonViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [CurrentWeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CurrentWeatherFragment :
    BaseFragment<FragmentCurrentWeatherBinding>(FragmentCurrentWeatherBinding::inflate) {
    private val commonViewModel by viewModels<CommonViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        arguments?.let {
            val lat = it.getDouble("latitude") ?: 0.0
            val long = it.getDouble("longitude") ?: 0.0
            getCurrentWeatherInfo(lat.toDouble(), long.toDouble())
        }

    }

    @SuppressLint("SetTextI18n")
    private fun getCurrentWeatherInfo(lat: Double, long: Double) {
        collectLatestLifecycleFlow(
            commonViewModel.getCurrentWeatherInfo(
                HashMap<String, String>().also {
                    it["lat"] = lat.toString()
                    it["lon"] = long.toString()
                    it["appid"] = getString(
                        R.string.open_weather_map_key
                    )
                }
            )
        ) {
            if (it is Resource.Success) {
                hideProgress()
                val data = it.data as WeatherInfo
                logError("${data.weather}")
                binding?.apply {
                    //tvCountry.text = data.sys.country.toString()
                    tvPlace.text = "${data.name} (${data.sys.country})"
                    if (data.weather.isNotEmpty())
                        tvWeather.text = "Weather: ${data.weather[0].main.toString()}"
                    tvTemp.text = "Temperature: ${data.main.temp.toString()}"
                    tvPressure.text = "Pressure: ${data.main.pressure.toString()}"
                    tvHumidity.text = "Humidity: ${data.main.humidity.toString()}"
                    tvWindSpeed.text = "Wind Speed: ${data.wind.speed.toString()}"
                }

            } else if (it is Resource.Error) {
                hideProgress()
                if (it.data != null) {
                    val error = it.data as BaseResponse
                    logError("$error")
                    if (error.message.isNullOrEmpty()) {
                        requireActivity().showShortToast(getString(R.string.something_went_wrong))
                    }
                } else {
                    requireActivity().showShortToast(getString(R.string.something_went_wrong))
                }
            } else if (it is Resource.Loading) {
                showProgress()
            }

        }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CurrentWeatherFragment1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Double, param2: Double) =
            CurrentWeatherFragment().apply {
                arguments = Bundle().apply {
                    putDouble("latitude", param1)
                    putDouble("longitude", param2)
                }
            }
    }
}