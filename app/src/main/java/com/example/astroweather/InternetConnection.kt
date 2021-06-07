package com.example.astroweather

import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.example.astroweather.database.forecast.ForecastTable
import com.example.astroweather.database.forecast.ForecastViewModel
import com.example.astroweather.database.weather.WeatherViewModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.URL


abstract class InternetConnection {
    companion object {
        private val gson: Gson = Gson()
        private val API_KEY = "ceb1a4d9de1e4b06b0ade5eb2f894e78"

        //forecast
        //http://api.openweathermap.org/data/2.5/forecast/?q=lodz&appid=ceb1a4d9de1e4b06b0ade5eb2f894e78
        //todays_weather
        //http://api.openweathermap.org/data/2.5/weather?q=lodz&appid=ceb1a4d9de1e4b06b0ade5eb2f894e78
        @Throws(JsonSyntaxException::class, FileNotFoundException::class)
        fun downloadWeatherByCityName(
            cityName: String,
            viewModel: WeatherViewModel,
            isFavourite: Boolean = false,
            context: Context
        ) {
            runBlocking(Dispatchers.IO) {
                launch {
                    if (isOnline(context)) {
                        val url =
                            URL("https://api.openweathermap.org/data/2.5/weather?q=$cityName&appid=$API_KEY")
                        val reader = InputStreamReader(url.openStream())
                        val result: WeatherTable = Gson().fromJson(reader, WeatherTable::class.java)
                        result.isUserFavourite = isFavourite
                        viewModel.addWeatherTable(result)
                    }
                }
            }
        }

        @Throws(JsonSyntaxException::class, FileNotFoundException::class)
        fun downloadWeatherByCords(
            lat: Double,
            lon: Double,
            viewModel: WeatherViewModel,
            context: Context,
            isFavourite: Boolean = false
        ) {
            runBlocking(Dispatchers.IO) {
                launch {
                    if (isOnline(context)) {
                        val url =
                            URL("https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$API_KEY")
                        val reader = InputStreamReader(url.openStream())
                        val result: WeatherTable = Gson().fromJson(reader, WeatherTable::class.java)
                        result.isUserFavourite = isFavourite
                        viewModel.addWeatherTable(result)
                    }
                }
            }
        }

        @Throws(JsonSyntaxException::class, FileNotFoundException::class)
        fun downloadForecastByCords(
            lat: Double,
            lon: Double,
            viewModel: ForecastViewModel,
            context: Context
        ) {
            runBlocking(Dispatchers.IO) {
                launch {
                    if (isOnline(context)) {
                        val url =
                            URL("https://api.openweathermap.org/data/2.5/forecast?lat=$lat&lon=$lon&appid=$API_KEY")
                        val reader = InputStreamReader(url.openStream())
                        val result: ForecastTable =
                            Gson().fromJson(reader, ForecastTable::class.java)

                        viewModel.addForecastTable(result)
                    }
                }
            }
        }

        @Throws(JsonSyntaxException::class, FileNotFoundException::class)
        fun downloadForecastByCityName(
            cityName: String,
            viewModel: ForecastViewModel,
            context: Context
        ) {
            runBlocking(Dispatchers.IO) {
                launch {
                    if (isOnline(context)) {
                        val url =
                            URL("https://api.openweathermap.org/data/2.5/forecast?q=$cityName&appid=$API_KEY")
                        val reader = InputStreamReader(url.openStream())
                        val result: ForecastTable =
                            Gson().fromJson(reader, ForecastTable::class.java)
                        viewModel.addForecastTable(result)
                    }
                }
            }
        }

        fun ImageView.loadImage(imgCode: String,activity:FragmentActivity?) {
            val img: ImageView = this
            runBlocking(Dispatchers.IO) {
                val bitmap = async{
                    BitmapFactory.decodeStream(URL("http://openweathermap.org/img/wn/$imgCode@2x.png").openStream())
                }.await()

                activity?.runOnUiThread {
                    img.setImageBitmap(bitmap)
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
