package com.example.astroweather.fragments

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.astroweather.R
import com.example.astroweather.WeatherTable
import com.example.astroweather.database.forecast.ForecastTable

class WeatherExtendedFragment : Fragment(R.layout.fragment_weather_extended) {
    private lateinit var windStrength: TextView
    private lateinit var windDirection: TextView
    private lateinit var humidity: TextView
    private lateinit var visibility: TextView
    private lateinit var clouds: TextView
    private lateinit var population: TextView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        windStrength = view!!.findViewById(R.id.wind_strength)
        windDirection = view!!.findViewById(R.id.wind_direction)
        humidity = view!!.findViewById(R.id.humidity)
        visibility = view!!.findViewById(R.id.visibility)
        clouds = view!!.findViewById(R.id.clouds)
        population = view!!.findViewById(R.id.population)

    }

    fun updateWeatherInfo(weatherTable: WeatherTable) {
        windStrength.text = weatherTable.wind.speed.toString() + " m/s"
        windDirection.text = weatherTable.wind.deg.toString()
        humidity.text = weatherTable.main.humidity.toString() + "%"
        visibility.text = weatherTable.visibility.toString() + " m"
        clouds.text = weatherTable.clouds.all.toString() + "%"

    }

    fun updateForecastInfo(forecastTable: ForecastTable) {
        if (forecastTable?.city?.population != null)
            population.text = forecastTable?.city?.population.toString()
        else
            population.text = "no data"
    }


}