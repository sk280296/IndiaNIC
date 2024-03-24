package com.app.indianic.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.app.indianic.base.BaseActivity
import com.app.indianic.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            runBlocking(Dispatchers.IO) {
                startActivity(
                    Intent(
                        this@SplashActivity,
                        WeatherActivity::class.java
                    )
                )
                finish()
            }

        }, 2000)
    }

}