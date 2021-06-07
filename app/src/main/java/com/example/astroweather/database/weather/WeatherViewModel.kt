package com.example.astroweather.database.weather

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.astroweather.WeatherTable
import com.example.astroweather.database.WeatherDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val allWeathers: LiveData<List<WeatherTable>>
    private val repository: WeatherRepository

    init {
        val weatherDao = WeatherDatabase.getDatabase(application).WeatherDao()
        repository = WeatherRepository(weatherDao)
        allWeathers = repository.getWeatherList()
    }

     fun addWeatherTable(table:WeatherTable) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addWeather(table)
        }
    }

     fun getAllWeathers(): LiveData<List<WeatherTable>> {
        return allWeathers
    }

    fun getAllSavedLocations():Set<String>?{
        val listOfNames= allWeathers.value?.map{it.name}
        return listOfNames?.toHashSet()
    }
    fun getAllFavouriteLocations():Set<String>?{
        val listOfNames= allWeathers.value?.filter{it.isUserFavourite == true}?.map{it.name}
        return listOfNames?.toHashSet()
    }

    fun getLocationByName(name:String):WeatherTable?{
        allWeathers.value?.forEach {
            if(it.name == name)
                return it
        }
        return null
    }
}