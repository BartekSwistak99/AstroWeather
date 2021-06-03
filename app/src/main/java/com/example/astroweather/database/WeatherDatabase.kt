package com.example.astroweather.database
///https://developer.android.com/training/data-storage/room#groovy
import android.content.Context
import androidx.room.*
import com.example.astroweather.WeatherTable

@Database(entities = [WeatherTable::class], version = 1)
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
}