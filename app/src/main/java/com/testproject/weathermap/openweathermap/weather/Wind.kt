package com.testproject.weathermap.openweathermap.weather


import com.google.gson.annotations.SerializedName

data class Wind(
    val deg: Int,
    val speed: Double
)