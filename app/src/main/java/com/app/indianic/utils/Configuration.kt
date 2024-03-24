package com.app.indianic.utils

object Configuration {

    const val DEV_MODE = false
    private const val DEV_BASE_URL = "https://api.openweathermap.org/"
    private const val PROD_BASE_URL = "https://api.openweathermap.org/"


    @JvmField
    var BASE_URL =
        if (DEV_MODE) DEV_BASE_URL else PROD_BASE_URL


}