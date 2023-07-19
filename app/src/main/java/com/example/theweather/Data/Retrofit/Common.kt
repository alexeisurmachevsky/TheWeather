package com.example.theweather.Data.Retrofit

object Common {
    private const val BASE_URL = "https://api.openweathermap.org/"
    val retrofitService: RetrofitServieces
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServieces::class.java)
}