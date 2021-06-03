package com.example.astroweather

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.example.astroweather.database.WeatherViewModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.lang.IllegalArgumentException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

abstract class InternetConnection {
    companion object {
        private val gson: Gson = Gson()
        private val API_KEY = "ceb1a4d9de1e4b06b0ade5eb2f894e78"

        //forecast
        //http://api.openweathermap.org/data/2.5/forecast/?q=lodz&appid=ceb1a4d9de1e4b06b0ade5eb2f894e78
        //weather
        //http://api.openweathermap.org/data/2.5/weather?q=lodz&appid=ceb1a4d9de1e4b06b0ade5eb2f894e78
        @Throws(JsonSyntaxException::class,FileNotFoundException::class)
        fun getWeatherByCityName(cityName: String, viewModel: WeatherViewModel)  {
            runBlocking(Dispatchers.IO) {
                launch {
                    val url =
                        URL("https://api.openweathermap.org/data/2.5/weather?q=$cityName&appid=$API_KEY")
                    val reader = InputStreamReader(url.openStream())
                    val result: WeatherTable = Gson().fromJson(reader, WeatherTable::class.java)
                    viewModel.addWeatherTable(result)
                }
            }
        }
        @Throws(JsonSyntaxException::class,FileNotFoundException::class)
        fun getWeatherByCords(lat: Double,lon:Double, viewModel: WeatherViewModel)  {
            runBlocking(Dispatchers.IO) {
                launch {
                    val url =
                        URL("https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$API_KEY")
                    val reader = InputStreamReader(url.openStream())
                    val result: WeatherTable = Gson().fromJson(reader, WeatherTable::class.java)
                    viewModel.addWeatherTable(result)
                }
            }
        }

        //source https://stackoverflow.com/questions/51141970/check-internet-connectivity-android-in-kotlin
        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            }
            return false
        }


    }

}
