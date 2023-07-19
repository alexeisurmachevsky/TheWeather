package com.example.theweather.Data.Retrofit

import com.example.theweather.models.CityInfo
import com.example.theweather.models.WModel
import com.example.theweather.models.tws
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface RetrofitServieces {
    @GET
    fun getCurrentWeather(@Url url:String): Call<WModel>
    @GET
    fun get2WeekForecast(@Url url:String): Call<tws>
    @GET
    fun getCityInfo(@Url url:String):Call<CityInfo>
}