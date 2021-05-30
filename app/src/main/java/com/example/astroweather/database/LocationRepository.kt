package com.example.astroweather.database

import androidx.lifecycle.LiveData

class LocationRepository(private val locationDao : LocationDao){

    private val allLocations:LiveData<List<Location>> = locationDao.getAll()

    fun addLocation(location:Location){
        locationDao.insert(location)
    }
    fun addLocation(locationName:String){

        locationDao.insert(Location(locationName))
    }
    fun getLocation(locationName :String):Location? {
        return locationDao.findByName(locationName)
    }
    fun getListOfData():LiveData<List<Location>>{
        return allLocations
    }
}
