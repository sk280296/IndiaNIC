package com.app.indianic.api

import com.app.dolledup.utility.ApiConstants
import com.app.indianic.response.WeatherForecastResponse
import com.app.indianic.response.WeatherInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {

    @GET(ApiConstants.weatherData)
    suspend fun getCurrentWeatherInfo(@QueryMap request: Map<String, String>): Response<WeatherInfo>

    @GET(ApiConstants.fiveDayWeatherData)
    suspend fun getFiveDayWeatherData(@QueryMap request: Map<String, String>): Response<WeatherForecastResponse>

}