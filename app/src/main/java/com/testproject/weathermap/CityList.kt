package com.testproject.weathermap

import android.content.Intent
import android.os.Bundle
import android.view.View
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
    val TAG: String = "work"
    lateinit var recyclerView: RecyclerView
    lateinit var city: ArrayList<City>
    lateinit var text: TextInputEditText
    lateinit var searchBtn: FloatingActionButton
    lateinit var searchText: String
    lateinit var cityAdapter: CityAdapter
    lateinit var location: GetLocation
    lateinit var locationList: Locate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)
        city = arrayListOf()
        recyclerView = findViewById(R.id.search_list)
        text = findViewById(R.id.messageField)
        searchBtn = findViewById(R.id.searchBtn)
        location = GetLocation()
        cityAdapter = CityAdapter(this, city, this)
        recyclerView.adapter = cityAdapter
        searchBtn.setOnClickListener(View.OnClickListener {
            searchText = text.text.toString()
            if(isOnline()){
                GlobalScope.launch(Dispatchers.Main) {
                    locationList = location.getLocation(searchText).await()
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