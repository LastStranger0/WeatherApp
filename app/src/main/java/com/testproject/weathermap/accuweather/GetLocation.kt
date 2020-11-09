package com.testproject.weathermap.accuweather

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.testproject.weathermap.accuweather.location.Locate
import retrofit2.http.GET
import okhttp3.Interceptor
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

val API_KEY: String = "PFNGW8awukozsQmM26Wnrb0hTYPjac15"

//http://dataservice.accuweather.com/locations/v1/cities/autocomplete?apikey=2BTsJdolWwgqSyCIshzvvrfnyHz31v76&q=minsk

interface GetLocation {
    @GET("autocomplete")
    fun getLocation(
            @Query("q") location: String

    ) : Deferred<Locate>

    companion object{
        operator fun invoke(): GetLocation {
            val interseptor = Interceptor { chain ->
                val url = chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter("apikey", API_KEY)
                        .build()
                val request = chain
                        .request()
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
                    .baseUrl("http://dataservice.accuweather.com/locations/v1/cities/")
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GetLocation::class.java)
        }
    }
}