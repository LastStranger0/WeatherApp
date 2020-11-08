package com.testproject.weathermap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CityAdapter(context: Context, cites: ArrayList<City>, onCityListener: OnCityListener): RecyclerView.Adapter<CityAdapter.ViewHolder>() {
    private var inflater: LayoutInflater = LayoutInflater.from(context)
    private var cites: ArrayList<City> = cites
    private var onCityListener = onCityListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.list_city, parent, false)
        return ViewHolder(view, onCityListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cites[position]
        holder.city.text = city.CityName
        holder.country.text = city.AboutCity
        holder.key.text = city.Key
        holder.state.text = city.State
    }

    override fun getItemCount(): Int {
        return cites.size
    }

    class ViewHolder(view: View, onCityListener: OnCityListener): RecyclerView.ViewHolder(view), View.OnClickListener{
        val city: TextView = view.findViewById(R.id.name)
        val country: TextView = view.findViewById(R.id.country)
        val key: TextView = view.findViewById(R.id.key)
        val state: TextView = view.findViewById(R.id.state)
        val onCityListener = onCityListener
        init {
            val city: TextView = view.findViewById(R.id.name)
            val country: TextView = view.findViewById(R.id.country)
            val key: TextView = view.findViewById(R.id.key)
            val state: TextView = view.findViewById(R.id.state)
            view.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            onCityListener.onCityClick(adapterPosition)
        }
    }
    interface OnCityListener{
        fun onCityClick(position: Int)
    }
}