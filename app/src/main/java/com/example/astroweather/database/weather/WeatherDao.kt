package com.example.astroweather.database.weather

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.astroweather.WeatherTable

@Dao
interface WeatherDao {
    @Query("SELECT * FROM WeatherTable")
    fun getAll(): LiveData<List<WeatherTable>>




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg weatherTables: WeatherTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherTable: WeatherTable)

    @Delete
    fun delete(weatherTable: WeatherTable)
}