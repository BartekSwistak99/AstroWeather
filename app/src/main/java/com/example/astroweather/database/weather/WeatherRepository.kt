    package com.example.astroweather.database.weather

import androidx.lifecycle.LiveData
import com.example.astroweather.WeatherTable

    class WeatherRepository(private val weatherDao : WeatherDao){

    private val allWeathers :LiveData<List<WeatherTable>> = weatherDao.getAll()
//    private val allLocations:LiveData<List<WeatherTable>> = locationDao.getAll()

    fun addWeather(weatherTable:WeatherTable){
        weatherDao.insert(weatherTable)
    }


    fun getWeatherList():LiveData<List<WeatherTable>>{
        return allWeathers
    }
}
