package com.app.indianic.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.app.indianic.base.BaseFragment
import com.app.indianic.databinding.FragmentWeatherDataBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class WeatherDataFragment :
    BaseFragment<FragmentWeatherDataBinding>(FragmentWeatherDataBinding::inflate) {
    private lateinit var pagerAdapter: PagerAdapter

    val args: WeatherDataFragmentArgs by navArgs()

    var lat: Double = 0.0
    var long: Double = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        args.let {
            lat = it.latitude.toDouble()
            long = it.longitude.toDouble()
        }

        binding?.apply {
            tabLayout.addTab(tabLayout.newTab().setText("Current Weather"))
            tabLayout.addTab(tabLayout.newTab().setText("5 Day Forecast"))

            val currentWeatherFragment =
                CurrentWeatherFragment.newInstance(lat, long)
            val fiveDayForecastFragment =
                FiveDayForecastFragment.newInstance(lat, long)

            val fragmentList = arrayListOf<Fragment>()
            fragmentList.addAll(arrayListOf<Fragment>().also {
                it.add(currentWeatherFragment)
                it.add(fiveDayForecastFragment)
            })

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewPager.currentItem = tab?.position ?: 0
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })

            pagerAdapter = PagerAdapter(requireActivity())
            pagerAdapter.setFragmentList(fragmentList)

            viewPager.adapter = pagerAdapter
            viewPager.isUserInputEnabled = false;

            viewPager.unregisterOnPageChangeCallback(mlistener)

            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                viewPager.registerOnPageChangeCallback(
                    mlistener
                )
            }, 1000)


        }

    }

    private val mlistener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WeatherDataFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}