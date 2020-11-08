package com.testproject.weathermap.accuweather.location


import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("ID")
    val iD: String,
    @SerializedName("LocalizedName")
    val localizedName: String
)