package com.example.theweather.Common
import com.example.theweather.RetrofitServieces
import com.example.theweather.Utilities.RetrofitClient

object Common {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val retrofitService: RetrofitServieces
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServieces::class.java)
}