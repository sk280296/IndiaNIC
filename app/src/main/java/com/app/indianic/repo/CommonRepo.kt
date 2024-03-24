package com.app.indianic.repo

import com.app.indianic.api.ApiService
import com.app.indianic.response.WeatherForecastResponse
import com.app.indianic.response.WeatherInfo
import retrofit2.Response
import javax.inject.Inject

class CommonRepo @Inject constructor(
    val apiService: ApiService
)  {

    suspend fun getCurrentWeatherInfo(request: Map<String, String>): Response<WeatherInfo> =
        apiService.getCurrentWeatherInfo(request)


    suspend fun getFiveDayWeatherData(request: Map<String, String>): Response<WeatherForecastResponse> =
         apiService.getFiveDayWeatherData(request)


}