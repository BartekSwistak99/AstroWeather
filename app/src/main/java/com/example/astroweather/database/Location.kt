package com.example.astroweather.database

import androidx.room.*

@Entity(indices = [Index(value = ["city_name"],
    unique = true)])
data class Location(
    @ColumnInfo(name = "city_name" ) val cityName: String?
){
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
    override fun toString(): String{
        return cityName as String
    }
}
