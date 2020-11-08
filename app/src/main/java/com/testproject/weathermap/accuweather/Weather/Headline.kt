package com.testproject.weathermap.accuweather.Weather


import com.google.gson.annotations.SerializedName

data class Headline(
    @SerializedName("Category")
    val category: String,
    @SerializedName("EffectiveDate")
    val effectiveDate: String,
    @SerializedName("EffectiveEpochDate")
    val effectiveEpochDate: Int,
    @SerializedName("EndDate")
    val endDate: String,
    @SerializedName("EndEpochDate")
    val endEpochDate: Int,
    @SerializedName("Link")
    val link: String,
    @SerializedName("MobileLink")
    val mobileLink: String,
    @SerializedName("Severity")
    val severity: Int,
    @SerializedName("Text")
    val text: String
)