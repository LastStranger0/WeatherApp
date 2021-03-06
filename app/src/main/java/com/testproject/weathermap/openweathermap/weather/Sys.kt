package com.testproject.weathermap.openweathermap.weather


import com.google.gson.annotations.SerializedName

data class Sys(
    val country: String,
    val id: Int,
    val message: Double,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)