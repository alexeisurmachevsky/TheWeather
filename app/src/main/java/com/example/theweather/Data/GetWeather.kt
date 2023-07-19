package com.example.theweather.Data

import android.util.Log
import com.example.theweather.Data.Retrofit.RetrofitServieces
import com.example.theweather.models.CityInfo
import com.example.theweather.models.WModel
import com.example.theweather.models.fdf
import com.example.theweather.models.tws
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GetWeather(val mService: RetrofitServieces) {
    var wModel: WModel? = null
    var weatherList: List<fdf>? = null
    var cityInfo: CityInfo?=null

     suspend fun getCityInfo(cityName: String)= runBlocking{
        val url = "geo/1.0/direct?q=${cityName}&limit=1&appid=02df2d182c5922677742982922a5c7ee"

        val gci = launch{
            try {
                cityInfo = mService.getCityInfo(url).execute().body() as CityInfo
            }catch (e:Exception){
                Log.e("GetCityInfo", e.message.toString())

            }
        }
        gci.join()

    }

    suspend fun getCurrentWeather(lat: Double, lon: Double) = runBlocking{
       val url = "data/2.5/weather?lat=$lat&lon=$lon&appid=02df2d182c5922677742982922a5c7ee"

       val gcw =  launch {
            try {
                wModel = mService.getCurrentWeather(url).execute().body() as WModel
            } catch (e: Exception) {
                Log.e("GetCurrWeatherEx", e.message.toString())
            }
        }
       gcw.join()

    }

     suspend fun getAllDayForecast(lat: Double, lon: Double) = runBlocking{
        val url = "data/2.5/forecast?lat=$lat&lon=$lon&appid=02df2d182c5922677742982922a5c7ee"

        val gaf =  launch {
            try {
                weatherList =( mService.get2WeekForecast(url).execute().body() as tws).list
            }catch (e: Exception){
                Log.e("getAllDayForecastEX", e.message.toString())
            }
        }
        gaf.join()

    }
}