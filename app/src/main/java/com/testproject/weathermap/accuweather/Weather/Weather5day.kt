package com.testproject.weathermap.accuweather.Weather


import com.google.gson.annotations.SerializedName

data class Weather5day(
        @SerializedName("DailyForecasts")
    val dailyForecasts: List<DailyForecast>,
        @SerializedName("Headline")
    val headline: Headline
)