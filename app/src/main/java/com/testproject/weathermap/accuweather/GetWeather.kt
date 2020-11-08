package com.testproject.weathermap.accuweather

import com.testproject.weathermap.accuweather.Weather.Weather5day
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.http.GET
import okhttp3.Interceptor
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path
import retrofit2.http.Query

//http://dataservice.accuweather.com/forecasts/v1/daily/5day/28580?apikey=2BTsJdolWwgqSyCIshzvvrfnyHz31v76&metric=true

interface GetWeather {
    @GET("{key}")
    fun getWeather(
            @Path("key") key: String,
            @Query("metric") unit: Boolean
    ) : Deferred<Weather5day>

    companion object{
        operator fun invoke(): GetWeather {
            val interseptor = Interceptor { chain ->
                val url = chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter("apikey", API_KEY)
                        .build()
                val request = chain.request()
                        .newBuilder()
                        .url(url)
                        .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient
                    .Builder()
                    .addInterceptor(interseptor)
                    .build()
            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://dataservice.accuweather.com/forecasts/v1/daily/5day/")
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GetWeather::class.java)
        }
    }
}