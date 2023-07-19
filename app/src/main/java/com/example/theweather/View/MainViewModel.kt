package com.example.theweather.View

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.theweather.Data.GetWeather
import com.example.theweather.Data.Retrofit.RetrofitServieces
import com.example.theweather.Location
import com.example.theweather.models.WModel
import com.example.theweather.models.fdf
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel : ViewModel() {

    var wModel: WModel? = null
    val currentWModel: MutableLiveData<WModel> by lazy {
        MutableLiveData<WModel>()
    }

    var weatherList: List<fdf>? = null
    val currentWList: MutableLiveData<List<fdf>> by lazy {
        MutableLiveData<List<fdf>>()
    }

    fun getForecastByCurrentLocation(
        context: Context, fusedLocationProviderClient: FusedLocationProviderClient,
        activity: Activity, mService: RetrofitServieces
    ) {

        Location(
            context,
            fusedLocationProviderClient,
            activity
        ).getLocation { latitude, longitude ->
            getCurrentWeather(mService, latitude, longitude)
            getAllDayForecast(mService, latitude, longitude)
        }
    }

    fun getForecastByCityName(mService: RetrofitServieces, cityName: String) = runBlocking {
        val gw = GetWeather(mService)
        GlobalScope.launch {
            gw.getCityInfo(cityName)
        }.join()
        getCurrentWeather(mService, gw.cityInfo?.get(0)?.lat!!, gw.cityInfo?.get(0)?.lon!!)
        getAllDayForecast(mService, gw.cityInfo?.get(0)?.lat!!, gw.cityInfo?.get(0)?.lon!!)
    }

    private fun getCurrentWeather(mService: RetrofitServieces, lat: Double, lon: Double) =
        runBlocking {
            GlobalScope.launch {
                val gw = GetWeather(mService)
                gw.getCurrentWeather(lat, lon)
                wModel = gw.wModel
            }.join()
            currentWModel.value = wModel
        }

    private fun getAllDayForecast(mService: RetrofitServieces, lat: Double, lon: Double) =
        runBlocking {
            GlobalScope.launch {
                val gw = GetWeather(mService)
                gw.getAllDayForecast(lat, lon)
                weatherList = gw.weatherList
            }.join()
            currentWList.value = weatherList

        }

}