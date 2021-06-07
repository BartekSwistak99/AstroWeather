package com.example.astroweather.database.forecast

import androidx.room.*
import com.example.astroweather.Clouds
import com.example.astroweather.Cords
import com.example.astroweather.Weather
import com.example.astroweather.Wind
import com.example.astroweather.database.WeatherTypeConverters

@Entity(tableName = "ForecastTable",primaryKeys = ["city_id"])
class ForecastTable {
    var cod: String? = null
    var message: Double = 0.0
    var cnt: Int? = null
    @ColumnInfo(name = "forecast_list")
    lateinit var list: List<SingleForecast>
    @Embedded

    var city: City? = null

}

class SingleForecast {
    var dt: Long = 0
    @Embedded
    lateinit var main: ForecastMain
    @TypeConverters(WeatherTypeConverters::class)
    @ColumnInfo(name = "weathers_list")
    lateinit var weather: List<Weather>
    @Embedded
    lateinit var clouds: Clouds
    @Embedded
    lateinit var wind: Wind
    var visibility: Long = 0
    var pop: Double = 0.0
    @Embedded
    var sys: ForecastSys? = null
    var dt_text: String = ""
}

class City {
    @PrimaryKey
    @ColumnInfo(name = "city_id")
    var id :Int = 0
    var name: String? = null
    @Embedded
    lateinit var coord: Cords
    var country: String? = null
    var population:Int? = null
    var timezone: Int = 0
    var sunrise: Long = 0
    var sunset: Long = 0

}

data class ForecastSys(var pod: String? = null)
class ForecastMain {
    var temp: Double = 0.0
    var feels_like: Double = 0.0
    var temp_min: Double = 0.0
    var temp_max: Double = 0.0
    var pressure: Int = 0
    var sea_level: Int = 0
    var grnd_level: Int = 0
    var humidity: Int = 0
    var temp_kf: Double = 0.0
}
