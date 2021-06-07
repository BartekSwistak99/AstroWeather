package com.example.astroweather.database.forecast

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.astroweather.WeatherTable
import com.example.astroweather.database.WeatherDatabase
import com.example.astroweather.database.weather.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForecastViewModel(application: Application) : AndroidViewModel(application) {
    private val allForecasts: LiveData<List<ForecastTable>>
    private val repository: ForecastRepository

    init {
        val forecastDao = WeatherDatabase.getDatabase(application).ForecastrDao()
        repository = ForecastRepository(forecastDao)
        allForecasts = repository.getForecastList()
    }

    fun addForecastTable(table:ForecastTable) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addForecast(table)
        }
    }

    fun getAllForecasts(): LiveData<List<ForecastTable>> {
        return allForecasts
    }

}