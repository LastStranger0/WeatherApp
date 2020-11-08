package com.testproject.weathermap.openweathermap

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.testproject.weathermap.openweathermap.weather.WeatherNow
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API = "594824e544cfcb38d6525bccd7c76584"
//api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
interface GetWeatherNow {
    @GET("weather")
    fun getWeather(
            @Query("q") location: String,
            @Query("units") unit: String
    ):Deferred<WeatherNow>

    companion object{
        operator fun invoke(): GetWeatherNow{
            val interseptor = Interceptor { chain ->
                val url = chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter("appid", API)
                        .build()
                val request = chain.request()
                        .newBuilder()
                        .url(url)
                        .build()
                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(interseptor)
                    .build()
            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GetWeatherNow::class.java)

        }
    }
}