    package com.example.astroweather.database

import androidx.lifecycle.LiveData
import com.example.astroweather.WeatherTable

class WeatherRepository(private val locationDao : WeatherDao){

    private val allWeathers :LiveData<List<WeatherTable>> = locationDao.getAll()
//    private val allLocations:LiveData<List<WeatherTable>> = locationDao.getAll()

    fun addWeather(weatherTable:WeatherTable){
        locationDao.insert(weatherTable)
    }


    fun getWeatherList():LiveData<List<WeatherTable>>{
        return allWeathers
    }
}
