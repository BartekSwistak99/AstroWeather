package com.example.astroweather.database

import androidx.room.*

@Entity(indices = [Index(value = ["city_name"],
    unique = true)])
data class Location(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "city_name" ) val cityName: String?
)
