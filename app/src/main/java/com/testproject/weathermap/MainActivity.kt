package com.testproject.weathermap

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.testproject.weathermap.accuweather.GetWeather
import com.testproject.weathermap.openweathermap.GetWeatherNow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var settings: SharedPreferences
    private lateinit var settingsEditor: SharedPreferences.Editor

    private var key: String = "city"
    private var cityName: String = "city" //if cityName = "city", nothing don't work
    private var ok = 1

    private lateinit var weather: GetWeather
    private lateinit var weatherNow: GetWeatherNow
    private var metric = true

    private lateinit var city: TextView
    private lateinit var temperature: TextView
    private lateinit var typeWeather: TextView
    private lateinit var icon: ImageView

    private lateinit var typeWeatherToday: TextView
    private lateinit var tempToday: TextView
    private lateinit var iconToday: ImageView

    private lateinit var typeWeatherTomorrow: TextView
    private lateinit var tempTomorrow: TextView
    private lateinit var iconTomorrow: ImageView

    private lateinit var typeWeather2day: TextView
    private lateinit var temp2day: TextView
    private lateinit var icon2day: ImageView
    private lateinit var day2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settings = getSharedPreferences("city", MODE_PRIVATE)
        settingsEditor = settings.edit()

        key = settings.getString("key", "city").toString()
        cityName = settings.getString("name", "city").toString()

        weather = GetWeather()
        weatherNow = GetWeatherNow()

        initialize()

        unit()

        if(cityName != "city"){
            city.text = cityName
            if(isOnline()) {
                setValues(weatherNow, weather)
            }
        }


    }


    override fun onResume() {
        super.onResume()
        unit()
        key = settings.getString("key", "city").toString()
        cityName = settings.getString("name", "city").toString()
        if(cityName != "city"){
            setValues(weatherNow, weather)
        }
    }


    private fun setValues(weatherNow: GetWeatherNow, weather: GetWeather){
        GlobalScope.launch(Dispatchers.Main) {
            val weatherAtNow = if (metric)
                weatherNow.getWeather(cityName, "metric").await()
            else
                weatherNow.getWeather(cityName, "imperial").await()
            val weatherList = weather.getWeather(key, metric).await()
            temperature.text = if (metric)
                weatherAtNow.main.temp.toInt().toString() + getString(R.string.C)
            else
                weatherAtNow.main.temp.toInt().toString() + getString(R.string.F)
            typeWeather.text = weatherAtNow.weather[0].main

            tempToday.text = if (metric)
                "${weatherList.dailyForecasts[0].temperature.maximum.value.toInt()}/${weatherList.dailyForecasts[0].temperature.minimum.value.toInt()}${getString(R.string.C)}"
            else
                "${weatherList.dailyForecasts[0].temperature.maximum.value.toInt()}/${weatherList.dailyForecasts[0].temperature.minimum.value.toInt()}${getString(R.string.F)}"
            typeWeatherToday.text = weatherAtNow.weather[0].main

            tempTomorrow.text = if (metric)
                "${weatherList.dailyForecasts[1].temperature.maximum.value.toInt()}/${weatherList.dailyForecasts[1].temperature.minimum.value.toInt()}${getString(R.string.C)}"
            else
                "${weatherList.dailyForecasts[1].temperature.maximum.value.toInt()}/${weatherList.dailyForecasts[1].temperature.minimum.value.toInt()}${getString(R.string.F)}"
            typeWeatherTomorrow.text = weatherList.dailyForecasts[1].day.iconPhrase

            temp2day.text = if (metric)
                "${weatherList.dailyForecasts[2].temperature.maximum.value.toInt()}/${weatherList.dailyForecasts[2].temperature.minimum.value.toInt()}${getString(R.string.C)}"
            else
                "${weatherList.dailyForecasts[2].temperature.maximum.value.toInt()}/${weatherList.dailyForecasts[2].temperature.minimum.value.toInt()}${getString(R.string.F)}"
            typeWeather2day.text = weatherList.dailyForecasts[2].day.iconPhrase

        }
    }

    fun toCityList(view: View) {
        intent = Intent(this, CityList::class.java)
        startActivityForResult(intent, ok)

    }

    private fun isOnline(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            val exitValue = ipProcess.waitFor()
            return exitValue == 0
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
             if (data != null) {
                key = data.getStringExtra("key")
                cityName = data.getStringExtra("name")
                settingsEditor.putString("key", key)
                settingsEditor.putString("name", cityName)
                settingsEditor.apply()
            }



        key = settings.getString("key", "city").toString()
        cityName = settings.getString("name", "city").toString()

        weather = GetWeather()
        weatherNow = GetWeatherNow()

        temperature = findViewById(R.id.temperature)
        city = findViewById(R.id.cityName)
        unit()
        if(cityName != "city"){
            city.text = cityName
            setValues(weatherNow, weather)

        }
    }

    fun toSettings(view: View){
        intent = Intent(this, Settings::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

    private fun unit(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val unit = pref.getString(getString(R.string.iom), "")
        if (unit != null) {
            if (unit.contains("Metric"))
                metric = true
            else if(unit.contains("Imperial"))
                metric = false
        }
    }

    private fun initialize() {
        temperature = findViewById(R.id.temperature)
        city = findViewById(R.id.cityName)
        typeWeather = findViewById(R.id.typeOfWeather)
        icon = findViewById(R.id.weatherIcon)

        typeWeatherToday = findViewById(R.id.typeToday)
        tempToday = findViewById(R.id.temperatureToday)
        iconToday = findViewById(R.id.iconToday)

        typeWeatherTomorrow = findViewById(R.id.typeTomorrow)
        tempTomorrow = findViewById(R.id.temperatureTomorrow)
        iconTomorrow = findViewById(R.id.iconTomorrow)

        typeWeather2day = findViewById(R.id.type2day)
        temp2day = findViewById(R.id.temperature2day)
        icon2day = findViewById(R.id.icon2day)
        day2 = findViewById(R.id.to2day)
    }
}


/*
* TODO: потом сделай так, чтобы картинки обновлялись, а под конец реализуй хрень с днями недели
*  и различной погодой с различными background
*  а под конец реализуй фичу с поиском по gps*/



/*
   fun unit(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        var unit = pref.getString(getString(R.string.iom), "")
        if (unit != null) {
            if (unit.contains("Metric"))
                metric = true
            else if(unit.contains("Imperial"))
                metric = false
        }
    }
    override fun onPause() {
        super.onPause()
        city = findViewById(R.id.cityName)
        val prefEditor = settings.edit()
        prefEditor.putString("city", city.text.toString())
            prefEditor.putString("key", key)
        prefEditor.apply()
    }
    override fun onResume() {
        unit()
        if (isOnline()) {
            GlobalScope.launch(Dispatchers.Main) {
            }
        }
        super.onResume()
    }
    */

/*
*       lateinit var settings: SharedPreferences

    lateinit var key: String

    lateinit var weather: GetWeather
    lateinit var city: TextView
    lateinit var temperature: TextView
    lateinit var typeWeather: TextView
    lateinit var icon: ImageView

    lateinit var typeWeatherToday: TextView
    lateinit var tempToday: TextView
    lateinit var iconToday: ImageView

    lateinit var typeWeatherTomorrow: TextView
    lateinit var tempTomorrow: TextView
    lateinit var iconTomorrow: ImageView

    lateinit var typeWeather2day: TextView
    lateinit var temp2day: TextView
    lateinit var icon2day: ImageView
    lateinit var day2: TextView

    var metric = true
*
*
*
        var arguments = intent.extras

        if (arguments!= null){
            city.text = arguments.getString("name")
            key = arguments.getString("key").toString()
        }

        settings = getSharedPreferences("city", MODE_PRIVATE)
        city = findViewById(R.id.cityName)
        city.text = settings.getString("city", getString(R.string.city))

        weather = GetWeather()

        temperature = findViewById(R.id.temperature)
        typeWeather = findViewById(R.id.typeOfWeather)
        icon = findViewById(R.id.weatherIcon)

        typeWeatherToday = findViewById(R.id.typeToday)
        tempToday = findViewById(R.id.temperatureToday)
        iconToday = findViewById(R.id.iconToday)

        typeWeatherTomorrow = findViewById(R.id.typeTomorrow)
        tempTomorrow = findViewById(R.id.temperatureTomorrow)
        iconTomorrow = findViewById(R.id.iconTomorrow)

        typeWeather2day = findViewById(R.id.type2day)
        temp2day = findViewById(R.id.temperature2day)
        icon2day = findViewById(R.id.icon2day)
        day2 = findViewById(R.id.to2day)

        unit()

        if(isOnline()) {
            GlobalScope.launch(Dispatchers.Main) {
                val weatherList = if(metric)
                    weather.getWeather()
            }
        }*/