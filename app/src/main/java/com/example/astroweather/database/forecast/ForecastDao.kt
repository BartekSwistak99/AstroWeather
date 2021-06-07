package com.example.astroweather.database.forecast

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ForecastDao {
    @Query("SELECT * FROM ForecastTable")
    fun getAll(): LiveData<List<ForecastTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg forecastTables: ForecastTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(forecastTable: ForecastTable)

    @Delete
    fun delete(forecastTable: ForecastTable)
}