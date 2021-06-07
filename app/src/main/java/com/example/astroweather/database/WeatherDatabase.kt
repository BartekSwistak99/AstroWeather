package com.example.astroweather.database
///https://developer.android.com/training/data-storage/room#groovy
import android.content.Context
import androidx.room.*
import com.example.astroweather.WeatherTable
import com.example.astroweather.database.forecast.ForecastDao
import com.example.astroweather.database.forecast.ForecastTable
import com.example.astroweather.database.weather.WeatherDao

@Database(entities = [WeatherTable::class, ForecastTable::class], version = 1)
@TypeConverters(WeatherTypeConverters::class)
abstract class WeatherDatabase: RoomDatabase() {
    companion object{
        @Volatile
        private var database:WeatherDatabase? = null

        fun getDatabase(applicationContext: Context):WeatherDatabase{
            if(database !=null){
                return database as WeatherDatabase
            }
            synchronized(this){

                database =  Room.databaseBuilder(
                    applicationContext,
                    WeatherDatabase::class.java, "database"
                ).build()
                return database  as WeatherDatabase
            }

        }
    }
    abstract fun WeatherDao(): WeatherDao
    abstract fun ForecastrDao(): ForecastDao
}