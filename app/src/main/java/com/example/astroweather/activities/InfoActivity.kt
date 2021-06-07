package com.example.astroweather.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.astrocalculator.AstroCalculator
import com.astrocalculator.AstroDateTime
import com.example.astroweather.InternetConnection
import com.example.astroweather.R
import com.example.astroweather.WeatherTable
import com.example.astroweather.database.forecast.ForecastViewModel
import com.example.astroweather.database.weather.WeatherViewModel
import com.example.astroweather.fragments.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Math.abs
import java.lang.Thread.sleep
import java.util.*
import java.util.Calendar.*
import kotlin.concurrent.thread

class InfoActivity : AppCompatActivity() {
    private val UPDATES_REFRESH_INTERVAL: Long = 60 * 1000  //one minute
    private var actualCalendar = getInstance()
    private var refreshCalendar = getInstance()
    private lateinit var location: AstroCalculator.Location
    private lateinit var moonInfo: AstroCalculator.MoonInfo

    private lateinit var astroCalc: AstroCalculator
    private lateinit var sunInfo: AstroCalculator.SunInfo
    private var refreshInterval: Int = 30
    private lateinit var moonFragment: MoonFragment
    private lateinit var sunFragment: SunFragment
    private lateinit var weatherFragment: WeatherFragment
    private lateinit var weatherExtendedFragment: WeatherExtendedFragment
    private lateinit var forecastFragment: ForecastFragment
    private var isCelsius: Boolean = true
    private var cityName: String = "none"
    private lateinit var weathersViewModel: WeatherViewModel
    private lateinit var forecastViewModel: ForecastViewModel
    private var lastRefreshCalendar: Calendar? = null
    private var latitude = 0.0
    private var longitude = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        setSupportActionBar(findViewById(R.id.toolbar))
//        deleteDatabase("database")
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title =
            getString(R.string.app_name)


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { _ ->
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("latitude", location.latitude)
            intent.putExtra("longitude", location.longitude)
            intent.putExtra("refresh", refreshInterval)
            startActivity(intent)
        }
        weathersViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        forecastViewModel = ViewModelProvider(this).get(ForecastViewModel::class.java)
        moonFragment = supportFragmentManager.findFragmentById(R.id.MoonFragment) as MoonFragment
        sunFragment = supportFragmentManager.findFragmentById(R.id.SunFragment) as SunFragment
        weatherFragment =
            supportFragmentManager.findFragmentById(R.id.WeatherFragment) as WeatherFragment
        weatherExtendedFragment =
            supportFragmentManager.findFragmentById(R.id.ExtendedWeatherFragment) as WeatherExtendedFragment
        forecastFragment = supportFragmentManager.findFragmentById(R.id.ForecastFragment) as ForecastFragment

        refreshInterval = intent.getIntExtra("refresh", refreshInterval)
        refreshCalendar.add(Calendar.SECOND, refreshInterval)


        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)
        isCelsius = intent.getBooleanExtra("isCelsius", true)
        cityName = intent.getStringExtra("selectedCity") ?: "none"

        val str: String = if (latitude >= 0) "$latitude N"
        else "${-latitude} S"
        val str2: String = if (longitude >= 0) "$longitude E"
        else "${-longitude} W"
        createUpdatesThread()


        initAstro(latitude, longitude)
        updateMoon()
        updateSun()
        createViewModelsObservers()
        val internetError: TextView = findViewById(R.id.no_internet)
        internetError.visibility = if (InternetConnection.isOnline(this))
            View.INVISIBLE else View.VISIBLE


        thread {
            while (true) {
                Thread.sleep(50)
                val nowCalendar = getInstance()
                if (refreshCalendar.get(Calendar.SECOND) == nowCalendar
                        .get(Calendar.SECOND)
                ) {
                    this@InfoActivity.runOnUiThread {
                        astroCalc = AstroCalculator(astroTimeFacotry(nowCalendar), location)
                        refreshCalendar.add(Calendar.SECOND, refreshInterval)
                        sunInfo = astroCalc.sunInfo
                        moonInfo = astroCalc.moonInfo

                        updateMoon()
                        moonFragment.refreshDisplayedInfo()
                        updateSun()
                        sunFragment.refreshDisplayedInfo()
                        internetError.visibility = if (InternetConnection.isOnline(this))
                            View.INVISIBLE else View.VISIBLE
                        Log.i("refresh", "REFRESHED")
                    }
                }
            }
        }


    }

    private fun initAstro(latitude: Double, longitude: Double) {
        this.location = AstroCalculator.Location(latitude, longitude)
        this.astroCalc = AstroCalculator(astroTimeFacotry(actualCalendar), location)
        this.moonInfo = astroCalc.moonInfo
        this.sunInfo = astroCalc.sunInfo
    }

    private fun updateMoon() {
        moonFragment.updateMoonInfo(
            moonInfo.moonrise.toString().removeSuffix(" GMT+1"),
            moonInfo.moonset.toString().removeSuffix(" GMT+1"),
            moonInfo.nextNewMoon.toString().removeSuffix(" GMT+1"),
            moonInfo.nextFullMoon.toString().removeSuffix(" GMT+1"),
            moonInfo.age.toString()
//            String.format("%.2f", moonInfo.age)
        )
    }

    private fun updateSun() {
        sunFragment.updateSunInfo(
            sunInfo.sunrise.toString().removeSuffix(" GMT+1"),
            sunInfo.azimuthRise.toString(),
//            String.format("%.2f", sunInfo.azimuthRise),
            sunInfo.twilightMorning.toString().removeSuffix(" GMT+1"),
            sunInfo.sunset.toString().removeSuffix(" GMT+1"),
            sunInfo.azimuthSet.toString(),
//            String.format("%.2f", sunInfo.azimuthSet),
            sunInfo.twilightEvening.toString().removeSuffix(" GMT+1")
        )
    }

    private fun astroTimeFacotry(calendar: Calendar): AstroDateTime {
        return AstroDateTime(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND),
            1,
            false
        )
    }

    private fun createViewModelsObservers() {
//        var weatherTable : WeatherTable? = null


        weathersViewModel.getAllWeathers().observe(this,
            androidx.lifecycle.Observer { list ->
                list.forEach {
                    if (cityName != "none" && it.name == cityName) {
                        updateWeatherFragments(it)
                    } else if (cityName == "none" && abs(it.coord.lat - latitude) <= 0.1 && abs(it.coord.lon - longitude) <= 0.1) {
                        cityName = it.name
                        updateWeatherFragments(it)
                    }
                }
            })
        forecastViewModel.getAllForecasts().observe(this,
            androidx.lifecycle.Observer { list ->
                list.forEach {
                    if (cityName != "none" && it.city?.name == cityName) {
                        weatherExtendedFragment.updateForecastInfo(it)
                        Log.i("forecast","should be working...")
                        forecastFragment.updateForecastInfo(it,isCelsius)
                    }
                }
            })
    }

    private fun updateWeatherFragments(weatherTable: WeatherTable) {
        val lastUpdate = findViewById<TextView>(R.id.last_update)
        weatherFragment.updateWeatherInfo(weatherTable, isCelsius)
        weatherExtendedFragment.updateWeatherInfo(weatherTable)
        lastRefreshCalendar = getInstance()
        lastRefreshCalendar?.timeInMillis = weatherTable.dt * 1000
        lastUpdate.text = "%02d.%02d %02d:%02d".format(
            lastRefreshCalendar!!.get(DAY_OF_MONTH),
            lastRefreshCalendar!!.get(MONTH) + 1,
            lastRefreshCalendar?.get(HOUR_OF_DAY),
            lastRefreshCalendar!!.get(MINUTE)
        )
    }

    private fun createUpdatesThread() {
        thread {
            while (true) {
                val isOnline = InternetConnection.isOnline(this)
                if (isOnline && cityName != "none") {
                    InternetConnection.downloadWeatherByCityName(cityName, weathersViewModel,false,this)
                    InternetConnection.downloadForecastByCityName(cityName, forecastViewModel,this)
                } else if (isOnline) {
                    InternetConnection.downloadWeatherByCords(
                        latitude,
                        longitude,
                        weathersViewModel,this
                    )
                    InternetConnection.downloadForecastByCords(
                        latitude,
                        longitude,
                        forecastViewModel,this
                    )
                }
                sleep(UPDATES_REFRESH_INTERVAL)
            }
        }
    }
}

