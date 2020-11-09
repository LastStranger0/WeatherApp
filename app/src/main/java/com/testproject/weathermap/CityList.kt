package com.testproject.weathermap

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.testproject.weathermap.accuweather.GetLocation
import com.testproject.weathermap.accuweather.location.Locate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class CityList : AppCompatActivity(), CityAdapter.OnCityListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var city: ArrayList<City>
    private lateinit var text: TextInputEditText
    private lateinit var searchBtn: FloatingActionButton
    private lateinit var searchText: String
    private lateinit var cityAdapter: CityAdapter
    private lateinit var location: GetLocation
    private lateinit var locationList: Locate
    private lateinit var progressBar: ProgressBar
    private lateinit var textProgress: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)
        initialize()

        searchBtn.setOnClickListener(View.OnClickListener {
            progressBar.visibility = View.VISIBLE
            textProgress.visibility = View.VISIBLE
            searchText = text.text.toString()
            if(isOnline()){
                GlobalScope.launch(Dispatchers.Main) {
                    locationList = location.getLocation(searchText).await()
                    progressBar.visibility = View.INVISIBLE
                    textProgress.visibility = View.INVISIBLE
                    for (i in 0 until locationList.size){
                        city.add(i, City(locationList[i].localizedName,
                                locationList[i].country.localizedName,
                                locationList[i].administrativeArea.localizedName,
                                locationList[i].key))
                        cityAdapter.notifyItemInserted(i)
                    }
                }
            }
        })
    }

    private fun initialize(){
        city = arrayListOf()
        recyclerView = findViewById(R.id.search_list)
        text = findViewById(R.id.messageField)
        searchBtn = findViewById(R.id.searchBtn)
        location = GetLocation()
        cityAdapter = CityAdapter(this, city, this)
        recyclerView.adapter = cityAdapter
        progressBar = findViewById(R.id.progressBar)
        textProgress = findViewById(R.id.text_loading)
    }

    fun isOnline(): Boolean {
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

    override fun onCityClick(position: Int) {
        intent = Intent(this, MainActivity::class.java)
        intent.putExtra("key", locationList[position].key)
        intent.putExtra("name", locationList[position].localizedName)
        setResult(RESULT_OK, intent)
        finish()
    }
}