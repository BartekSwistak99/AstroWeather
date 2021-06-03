package com.example.astroweather

import androidx.room.*
import com.example.astroweather.database.WeatherTypeConverters

@Entity(tableName = "WeatherTable")
class WeatherTable {
    @Embedded
    lateinit var coord: Cords

    @TypeConverters(WeatherTypeConverters::class)
    @ColumnInfo(name = "weathers_list")
    lateinit var weather: List<Weather>
    lateinit var base: String
    @Embedded
    lateinit var main: Main
    var visibility: Long = 0
    @Embedded
    lateinit var wind: Wind
    @Embedded
    lateinit var clouds: Clouds
    var dt: Long = 0
    @Embedded
    lateinit var sys: Sys
    var timezone: Int = 0
    @PrimaryKey
    @ColumnInfo(name = "weather_table_id")
    var id: Long = 0
    lateinit var name: String
    var cod: Int = 0
}

data class Cords(var lon: Double, var lat: Double) {
}

data class Weather(
    @ColumnInfo(name = "weather_id")
    var id: Int,
    var main: String,
    var description: String,
    var icon: String
)

data class Main(
    var temp: Double,
    var feels_like: Double,
    var temp_min: Double,
    var temp_max: Double,
    var pressure: Double,
    var humidity: Int
)

data class Wind(
    var speed: Double,
    var deg: Int
)
data class Clouds(
    var all: Int
)
data class Sys(
    var type: Int,
    @ColumnInfo(name = "sys_id")

    var id: Long,
    var country: String,
    var sunrise: Long,
    var sunset: Long
)