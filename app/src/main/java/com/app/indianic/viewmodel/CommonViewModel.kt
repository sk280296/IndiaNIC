package com.app.indianic.viewmodel

import androidx.lifecycle.ViewModel
import com.app.indianic.repo.CommonRepo
import com.app.indianic.utils.networkBoundResourceWithoutDb
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    private var repo: CommonRepo
) : ViewModel() {

    fun getCurrentWeatherInfo(request: Map<String,String>) = networkBoundResourceWithoutDb {
        repo.getCurrentWeatherInfo(request)
    }

    fun getFiveDayWeatherData(request: Map<String,String>) = networkBoundResourceWithoutDb {
        repo.getFiveDayWeatherData(request)
    }



}