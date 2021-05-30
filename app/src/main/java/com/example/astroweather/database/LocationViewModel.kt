package com.example.astroweather.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val readAllData: LiveData<List<Location>>
    private val repository: LocationRepository

    init {
        val locationDao = LocationDatabase.getDatabase(application).locationDao()
        repository = LocationRepository(locationDao)
        readAllData = repository.getListOfData()

    }

     fun addLocation(locationName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLocation(locationName)

        }
    }

     fun getLocation(locationName: String): Location? {
            return repository.getLocation(locationName)
    }

     fun getListOfData(): LiveData<List<Location>> {

        return repository.getListOfData()
    }
}