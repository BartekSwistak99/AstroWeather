package com.example.astroweather.database

import androidx.room.*

@Dao
interface LocationDao {
    @Query("SELECT * FROM Location")
    fun getAll(): List<Location>

    @Query("SELECT * FROM Location WHERE uid IN (:locationIds)")
    fun loadAllByIds(locationIds: IntArray): List<Location>

    @Query("SELECT * FROM location WHERE city_name LIKE :cityName   LIMIT 1")
    fun findByName(cityName: String): Location

    @Insert
    fun insertAll(vararg locations: Location)
    @Insert
    fun insert(location: Location)
    @Delete
    fun delete(location: Location)
}