package com.example.theweather.models

data class twf(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ListInside>,
    val message: Double
)