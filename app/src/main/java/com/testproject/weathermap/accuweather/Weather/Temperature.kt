package com.testproject.weathermap.accuweather.Weather


import com.google.gson.annotations.SerializedName

data class Temperature(
        @SerializedName("Maximum")
    val maximum: Maximum,
        @SerializedName("Minimum")
    val minimum: Minimum
)