package com.app.indianic.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.indianic.R
import com.app.indianic.base.BaseActivity
import com.app.indianic.databinding.ActivityWeatherBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WeatherActivity : BaseActivity<ActivityWeatherBinding>(
    ActivityWeatherBinding::inflate
) {

    private val backPressCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    lateinit var navHostFragment: NavHostFragment
    lateinit var navHostController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, backPressCallback)
        init()

        setListener()
    }


    private fun init() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        navHostController = navHostFragment.navController
        navHostController.navigate(R.id.locationFragment)

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListener() {
        binding.apply {

        }

    }

}