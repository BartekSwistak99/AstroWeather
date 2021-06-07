package com.example.astroweather.database.forecast

import androidx.lifecycle.LiveData

class ForecastRepository(private val forecastDao: ForecastDao) {
    private val allForecast: LiveData<List<ForecastTable>> = forecastDao.getAll()
    fun addForecast(forecastTable: ForecastTable) {
        forecastDao.insert(forecastTable)
    }


    fun getForecastList(): LiveData<List<ForecastTable>> {
        return allForecast
    }
}