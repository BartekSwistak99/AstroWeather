package com.example.astroweather.fragments

import android.icu.util.TimeZone
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.astroweather.InternetConnection.Companion.isOnline
import com.example.astroweather.InternetConnection.Companion.loadImage
import com.example.astroweather.R
import com.example.astroweather.WeatherTable
import java.lang.Thread.sleep
import kotlin.concurrent.thread


class WeatherFragment() : Fragment(R.layout.fragment_weather) {


    private lateinit var cityName: TextView
    private lateinit var coordinates: TextView
    private lateinit var actualTime: TextClock
    private lateinit var temperature: TextView
    private lateinit var pressure: TextView
    private lateinit var weather: TextView
    private lateinit var weatherImg: ImageView
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        cityName = view!!.findViewById(R.id.date1)
        coordinates = view!!.findViewById(R.id.humidity)
        actualTime = view!!.findViewById(R.id.clock)
        temperature = view!!.findViewById(R.id.temperature)
        pressure = view!!.findViewById(R.id.pressure)
        weather = view!!.findViewById(R.id.weather)
        weatherImg = view!!.findViewById(R.id.weather_img)
    }


    fun updateWeatherInfo(weatherTable: WeatherTable, isCelsius: Boolean) {
        cityName.text = weatherTable.name
        if (cityName.text.isEmpty())
            cityName.text = "no data"
        val str: String = if (weatherTable.coord.lat >= 0) "${weatherTable.coord.lat} N"
        else "${-weatherTable.coord.lat} S"
        val str2: String = if (weatherTable.coord.lon >= 0) "${weatherTable.coord.lon} E"
        else "${-weatherTable.coord.lon} W"
        coordinates.text = "$str $str2"


        val temp: Double =
            if (isCelsius) toCelsius(weatherTable.main.temp)
            else toFahrenheit(weatherTable.main.temp)
        val unit = if (isCelsius) "°C" else "°F"
        temperature.text = "%.2f$unit".format(temp)
        pressure.text = weatherTable.main.pressure.toString() + " hPa"
        weather.text = weatherTable.weather[0].description
        thread {
            var flag = false
            do {

                if (isOnline(context!!) && !flag) {
                    weatherImg.loadImage(weatherTable.weather[0].icon, activity)
                    flag = true
                }
                sleep(1000)
            } while (!flag)


        }
        val gmt = "GMT%+d:00".format(weatherTable.timezone / 3600)
        actualTime.visibility = View.VISIBLE
        actualTime.timeZone = gmt
        actualTime.format12Hour = null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Log.i("timeZone", TimeZone.getTimeZone(gmt).toString())
        }
    }

    companion object {
        fun toCelsius(kelvin: Double): Double {
            return (kelvin - 273.15)
        }

        fun toFahrenheit(kelvin: Double): Double {
            return 9.0 / 5.0 * (kelvin - 273.0) + 32.0;
        }
    }

}