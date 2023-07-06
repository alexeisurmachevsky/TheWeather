package com.example.theweather.models

data class tws(
    val cnt: Int,
    val cod: String,
    val list: List<fdf>,
    val message: Int
)